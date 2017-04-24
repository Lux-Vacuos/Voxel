/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.ecs.entities;

import static net.luxvacuos.voxel.client.input.Mouse.getDX;
import static net.luxvacuos.voxel.client.input.Mouse.getDY;
import static net.luxvacuos.voxel.client.input.Mouse.setCursorPosition;
import static net.luxvacuos.voxel.client.input.Mouse.setGrabbed;
import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.ecs.ClientComponents;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.resources.CastRay;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.ChunkLoader;
import net.luxvacuos.voxel.universal.ecs.components.Health;
import net.luxvacuos.voxel.universal.ecs.components.Player;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;
import net.luxvacuos.voxel.universal.tools.ToolTier;
import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class PlayerCamera extends CameraEntity {

	private boolean jump = false;
	private float speed;
	private boolean underWater = false;
	private int mouseSpeed = 8;
	private final int maxLookUp = 90;
	private final int maxLookDown = -90;
	private boolean flyMode = false;
	private Vector2d center;
	private Vector3d blockOutlinePos = new Vector3d();
	private ToolTier tool = ToolTier.ZERO;

	private float breakTime, resetTime;
	private static Vector3 tmp = new Vector3();

	private static List<BoundingBox> blocks = new ArrayList<>();
	private static final int MAX_INTERATION = 8;
	private static final float PRECISION = 16f;
	private Vector3d normalTMP = new Vector3d();
	private double depthTMP;

	public PlayerCamera(Matrix4d projectionMatrix, Window window) {
		this.add(new Player());
		this.add(new Velocity());
		this.add(new Scale());
		this.add(new AABB(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f))
				.setBoundingBox(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f)));
		this.speed = 1f;
		this.add(new Health(20));
		this.add(new ChunkLoader((int) REGISTRY.getRegistryItem("/Voxel/Settings/World/chunkRadius")));

		if (flyMode)
			Components.AABB.get(this).setEnabled(false);

		ClientComponents.PROJECTION_MATRIX.get(this).setProjectionMatrix(projectionMatrix);
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(Maths.createViewMatrix(this));
		center = new Vector2d(window.getWidth() / 2, window.getHeight() / 2);
		castRay = new CastRay(getProjectionMatrix(), getViewMatrix(), center, window.getWidth(), window.getHeight());
	}

	@Override
	public void update(float delta, IDimension dimension) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		KeyboardHandler kbh = window.getKeyboardHandler();
		Rotation rotation = Components.ROTATION.get(this);

		float mouseDX = getDX() * mouseSpeed * delta;
		float mouseDY = getDY() * mouseSpeed * delta;
		if (rotation.getY() + mouseDX >= 360)
			rotation.setY(rotation.getY() + mouseDX - 360);
		else if (rotation.getY() + mouseDX < 0)
			rotation.setY(360 - rotation.getY() + mouseDX);
		else
			rotation.setY(rotation.getY() + mouseDX);

		if (rotation.getX() - mouseDY >= maxLookDown && rotation.getX() - mouseDY <= maxLookUp)
			rotation.setX(rotation.getX() - mouseDY);
		else if (rotation.getX() - mouseDY < maxLookDown)
			rotation.setX(maxLookDown);
		else if (rotation.getX() - mouseDY > maxLookUp)
			rotation.setX(maxLookUp);

		Velocity vel = Components.VELOCITY.get(this);

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_W)) {
			vel.setZ(vel.getZ() + -Math.cos(Math.toRadians(rotation.getY())) * this.speed);
			vel.setX(vel.getX() + Math.sin(Math.toRadians(rotation.getY())) * this.speed);
		} else if (kbh.isKeyPressed(GLFW.GLFW_KEY_S)) {
			vel.setZ(vel.getZ() - -Math.cos(Math.toRadians(rotation.getY())) * this.speed);
			vel.setX(vel.getX() - Math.sin(Math.toRadians(rotation.getY())) * this.speed);
		}

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_D)) {
			vel.setZ(vel.getZ() + Math.sin(Math.toRadians(rotation.getY())) * this.speed);
			vel.setX(vel.getX() + Math.cos(Math.toRadians(rotation.getY())) * this.speed);
		} else if (kbh.isKeyPressed(GLFW.GLFW_KEY_A)) {
			vel.setZ(vel.getZ() - Math.sin(Math.toRadians(rotation.getY())) * this.speed);
			vel.setX(vel.getX() - Math.cos(Math.toRadians(rotation.getY())) * this.speed);
		}

		this.speed = (kbh.isCtrlPressed() ? (this.flyMode ? 6f : 2f) : 1f);

		if (this.flyMode) {
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_SPACE))
				vel.setY(5f * this.speed);
			else if (kbh.isShiftPressed())
				vel.setY(-5f * this.speed);
		} else {
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_SPACE) && !jump) {
				vel.setY(5f);
				jump = true;
			}

			if (kbh.isShiftPressed() && !jump)
				speed = 0.2f;
			else if (kbh.isCtrlPressed())
				speed = 2f;
			else
				speed = 1f;

			if (vel.getY() == 0)
				jump = false;
		}
	}

	private void setBlock(IBlock block, IDimension dimension, float delta) {

		Ray ray = castRay.getRay();
		BoundingBox box = new BoundingBox();
		Vector3d org = new Vector3d(ray.origin);
		Vector3d dir = new Vector3d(ray.direction);
		Vector3d dir1 = new Vector3d(ray.direction);
		dir.div(PRECISION);
		Vector3d pos = new Vector3d();
		int it = 0;
		double bcx = 0, bcy = 0, bcz = 0;
		CAST: while (true) {
			Vector3d.add(dir, dir1, dir);
			pos.set(org);
			Vector3d.add(pos, dir, pos);
			box.set(new Vector3(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1),
					new Vector3(pos.x + 0.1, pos.y + 0.1, pos.z + 0.1));
			blocks = dimension.getGlobalBoundingBox(box);
			for (BoundingBox boundingBox : blocks) {
				if (Maths.intersectRayBounds(ray, boundingBox, tmp)) {
					placeDirection(box.min, box.max, boundingBox.min, boundingBox.max);
					bcx = boundingBox.getCenterX();
					bcy = boundingBox.getCenterY();
					bcz = boundingBox.getCenterZ();
					break CAST;
				}
			}
			it++;
			if (it > MAX_INTERATION)
				break;
		}
		int bx = (int) bcx;
		if (pos.x < 0)
			bx = (int) bcx - 1;

		int bz = (int) bcz;
		if (pos.z > 0)
			bz = (int) bcz;

		int by = (int) bcy;
		if (pos.y < 0)
			by = (int) bcy - 1;
		blockOutlinePos.set(bx + 0.5, by + 0.5f, bz + 0.5);
		resetTime += 10 * delta;
		if (resetTime >= 1) {
			if (Mouse.isButtonDown(0)) {
				IBlock tBlock = dimension.getBlockAt(bx, by, bz);
				if (ToolTier.isSufficient(tool, tBlock.getToolTier())) {
					breakTime += tool.getMultiplier() / tBlock.getToolTier().getMultiplier() * delta;
					if (breakTime > 1) {
						setBlock(bx, by, bz, Blocks.getBlockByName("air"), dimension);
						resetTime = 0;
						breakTime = 0;
					}
				}
			} else if (Mouse.isButtonDown(1)) {
				setBlock(bx, by, bz, block, dimension);
				resetTime = 0;
			} else {
				breakTime = 0;
			}
		}
	}

	private void setBlock(int bx, int by, int bz, IBlock block, IDimension dimension) {
		if (block.getID() != 0) {
			bx -= (int) normalTMP.x;
			by -= (int) normalTMP.y;
			bz -= (int) normalTMP.z;
		}
		dimension.setBlockAt(bx, by, bz, block);
	}

	private boolean placeDirection(final Vector3 mina, final Vector3 maxa, final Vector3 minb, final Vector3 maxb) {
		final Vector3d faces[] = { new Vector3d(-1, 0, 0), new Vector3d(1, 0, 0), new Vector3d(0, -1, 0),
				new Vector3d(0, 1, 0), new Vector3d(0, 0, -1), new Vector3d(0, 0, 1) };
		double distances[] = { (maxb.x - mina.x), (maxa.x - minb.x), (maxb.y - mina.y), (maxa.y - minb.y),
				(maxb.z - mina.z), (maxa.z - minb.z) };
		for (int i = 0; i < 6; i++) {
			if (distances[i] < 0.0f)
				return false;
			if ((i == 0) || (distances[i] < depthTMP)) {
				normalTMP = faces[i];
				depthTMP = distances[i];
			}

		}
		return true;
	}

	@Override
	public void afterUpdate(float delta, IDimension dimension) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(Maths.createViewMatrix(this));
		castRay.update(getProjectionMatrix(), getViewMatrix(), center, window.getWidth(), window.getHeight());
		setBlock(Blocks.getBlockByName("stone"), dimension, delta);
	}

	public void setMouse() {
		setCursorPosition(ClientInternalSubsystem.getInstance().getGameWindow().getWidth() / 2,
				ClientInternalSubsystem.getInstance().getGameWindow().getHeight() / 2);
		setGrabbed(true);
	}

	public void unlockMouse() {
		setGrabbed(false);
	}

	public boolean isUnderWater() {
		return underWater;
	}

	public Vector3d getBlockOutlinePos() {
		return blockOutlinePos;
	}

}
