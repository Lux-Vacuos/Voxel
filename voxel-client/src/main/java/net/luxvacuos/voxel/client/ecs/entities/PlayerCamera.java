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

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.ecs.ClientComponents;
import net.luxvacuos.lightengine.client.ecs.entities.CameraEntity;
import net.luxvacuos.lightengine.client.input.KeyboardHandler;
import net.luxvacuos.lightengine.client.input.MouseHandler;
import net.luxvacuos.lightengine.client.rendering.api.glfw.Window;
import net.luxvacuos.lightengine.client.resources.CastRay;
import net.luxvacuos.lightengine.client.util.Maths;
import net.luxvacuos.lightengine.universal.core.GlobalVariables;
import net.luxvacuos.lightengine.universal.ecs.components.Rotation;
import net.luxvacuos.lightengine.universal.ecs.components.Velocity;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.ChunkLoader;
import net.luxvacuos.voxel.universal.ecs.entities.IDimensionEntity;
import net.luxvacuos.voxel.universal.tools.ToolTier;
import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class PlayerCamera extends CameraEntity implements IDimensionEntity {

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
	private static final int MAX_INTERATION = 64;
	private static final float PRECISION = 16f;
	private Vector3d normalTMP = new Vector3d();
	private double depthTMP;

	public PlayerCamera(Matrix4d projectionMatrix, String name, String uuid) {
		super(name, uuid);
		this.speed = 1f;

		if (flyMode)
			Components.AABB.get(this).setEnabled(false);
		Components.AABB.get(this).setGravity(!flyMode);
		ClientComponents.PROJECTION_MATRIX.get(this).setProjectionMatrix(projectionMatrix);
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(Maths.createViewMatrix(this));
		this.add(new ChunkLoader((int) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/chunkRadius"))));
		int width = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width"));
		int height = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height"));
		center = new Vector2d(width / 2, height / 2);
		castRay = new CastRay(getProjectionMatrix(), getViewMatrix(), center, width, height);
	}

	@Override
	public void update(float delta) {
		Window window = GraphicalSubsystem.getMainWindow();
		KeyboardHandler kbh = window.getKeyboardHandler();
		MouseHandler mh = window.getMouseHandler();
		Rotation rotation = Components.ROTATION.get(this);
		
		Components.CHUNK_LOADER.get(this).setChunkRadius((int) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/chunkRadius")));

		float mouseDX = mh.getDX() * mouseSpeed * delta;
		float mouseDY = mh.getDY() * mouseSpeed * delta;
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
				vel.setY(6f);
				jump = true;
			}

			if (kbh.isShiftPressed() && !jump)
				speed = 0.2f;
			else if (kbh.isCtrlPressed())
				speed = 2f;
			else
				speed = 1f;

			if (vel.getY() == 0 && !kbh.isKeyPressed(GLFW.GLFW_KEY_SPACE))
				jump = false;
		}
	}

	private void setBlock(IBlock block, IDimension dimension, float delta, MouseHandler mh) {

		if (GlobalVariables.TEST_MODE)
			return;

		Ray ray = castRay.getRay();
		BoundingBox box = new BoundingBox();
		Vector3d org = new Vector3d(ray.origin);
		Vector3d dir = new Vector3d();
		Vector3d incr = new Vector3d(ray.direction);
		incr.div(PRECISION);
		Vector3d pos = new Vector3d();
		int it = 0;
		double bcx = 0, bcy = 0, bcz = 0;
		CAST: while (true) {
			Vector3d.add(dir, incr, dir);
			pos.set(org);
			Vector3d.add(pos, dir, pos);
			box.set(new Vector3(pos.x - 0.1f, pos.y - 0.1f, pos.z - 0.1f),
					new Vector3(pos.x + 0.1f, pos.y + 0.1f, pos.z + 0.1f));
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
		if (pos.z < 0)
			bz = (int) bcz - 1;

		int by = (int) bcy;
		if (pos.y < 0)
			by = (int) bcy - 1;
		blockOutlinePos.set(bx + 0.5, by + 0.5f, bz + 0.5);
		resetTime += 5f * delta;
		if (resetTime >= 1) {
			if (mh.isButtonPressed(0)) {
				IBlock tBlock = dimension.getBlockAt(bx, by, bz);
				if (ToolTier.isSufficient(tool, tBlock.getToolTier())) {
					breakTime += tool.getMultiplier() / tBlock.getToolTier().getMultiplier() * delta;
					if (breakTime > 1) {
						setBlock(bx, by, bz, Blocks.getBlockByName("air"), dimension);
						resetTime = 0;
						breakTime = 0;
					}
				}
			} else if (mh.isButtonPressed(1)) {
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
	public void afterUpdate(float delta) {
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(Maths.createViewMatrix(this));
	}

	@Override
	public void updateDim(float delta, IDimension dim) {
		castRay.update(getProjectionMatrix(), getViewMatrix(), center,
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")),
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")));
		Window window = GraphicalSubsystem.getMainWindow();
		setBlock(Blocks.getBlockByName("stone"), dim, delta, window.getMouseHandler());
	}

	public boolean isUnderWater() {
		return underWater;
	}

	public Vector3d getBlockOutlinePos() {
		return blockOutlinePos;
	}

}
