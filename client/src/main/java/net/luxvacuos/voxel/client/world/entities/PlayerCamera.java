/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.world.entities;

//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_A;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_D;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_LSHIFT;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_S;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_SPACE;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_T;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_W;
//import static net.luxvacuos.voxel.client.input.Keyboard.isKeyDown;
import static net.luxvacuos.voxel.client.input.Mouse.getDX;
import static net.luxvacuos.voxel.client.input.Mouse.getDY;
import static net.luxvacuos.voxel.client.input.Mouse.isButtonDown;
import static net.luxvacuos.voxel.client.input.Mouse.setCursorPosition;
import static net.luxvacuos.voxel.client.input.Mouse.setGrabbed;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.igl.vector.Vector4d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.input.Mouse;
//import net.luxvacuos.voxel.client.network.VoxelClient;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.resources.DRay;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Inventory;
import net.luxvacuos.voxel.client.ui.menu.ItemGui;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.block.BlockEntity;
import net.luxvacuos.voxel.client.world.entities.components.ArmourComponent;
import net.luxvacuos.voxel.client.world.items.EmptyArmour;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.Health;
import net.luxvacuos.voxel.universal.ecs.components.Scale;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;

public class PlayerCamera extends Camera {

	private float speed;
	private float multiplierMouse = 14;
	private boolean underWater = false;
	private int mouseSpeed = 2;
	private final int maxLookUp = 90;
	private final int maxLookDown = -90;
	private Vector2d center;
	private int clickTime;
	private Vector3d normalVector = new Vector3d();
	private Inventory inventory;
	private int yPos;
	private ItemGui block;
	private boolean hit;
	private boolean died = false;
	private boolean flyMode = false;

	private static List<BoundingBox> blocks = new ArrayList<>();
	private static Vector3 tmp = new Vector3();

	public PlayerCamera(Matrix4d proj, Window window) {
		this.add(new Velocity());
		this.add(new Scale());
		this.add(new AABB(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f))
				.setBoundingBox(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f)));
		center = new Vector2d(window.getWidth() / 2, window.getHeight() / 2);
		dRay = new DRay(proj, Maths.createViewMatrix(this), center, 0, 0);
		this.speed = 1f;
		inventory = new Inventory(11, 11, window.getWidth() / 2 - 200, 0);
		super.add(new Health(20));
		super.add(new ArmourComponent());
		super.getComponent(ArmourComponent.class).armour = new EmptyArmour();
		flyMode = true;

		if (flyMode)
			Components.AABB.get(this).setEnabled(false);
	}

	public void update(float delta, GameResources gm, Dimension world) {
		isMoved = false;
		Window window = gm.getGameWindow();
		KeyboardHandler kbh = window.getKeyboardHandler();

		if (Components.HEALTH.get(this).get() <= 0 && !died) {
			died = true;
			try {
				gm.getWorldsHandler().getActiveWorld().dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gm.getCamera().setPosition(new Vector3d(0, 0, 1));
			gm.getCamera().setPitch(0);
			gm.getCamera().setYaw(0);
			unlockMouse();
			// gm.getGlobalStates().setState(GameState.MAINMENU);
			StateMachine.setCurrentState(StateNames.MAIN_MENU);
		}

		float mouseDX = getDX() * delta * mouseSpeed * 0.16f * multiplierMouse;
		float mouseDY = getDY() * delta * mouseSpeed * 0.16f * multiplierMouse;
		if (yaw + mouseDX >= 360) {
			yaw = yaw + mouseDX - 360;
		} else if (yaw + mouseDX < 0) {
			yaw = 360 - yaw + mouseDX;
		} else {
			yaw += mouseDX;
		}
		if (pitch - mouseDY >= maxLookDown && pitch - mouseDY <= maxLookUp) {
			pitch += -mouseDY;
		} else if (pitch - mouseDY < maxLookDown) {
			pitch = maxLookDown;
		} else if (pitch - mouseDY > maxLookUp) {
			pitch = maxLookUp;
		}

		yPos += Mouse.getDWheel();
		if (yPos > 10)
			yPos = 0;
		if (yPos < 0)
			yPos = 10;

		block = inventory.getItems()[0][yPos];

		if (yaw < 270 && yaw > 90)
			normalVector.z = 1;
		else
			normalVector.z = -1;

		if (yaw > 0 && yaw < 180)
			normalVector.x = 1;
		else
			normalVector.x = -1;

		normalVector.y = pitch / -90;

		Vector3d v = this.getPosition();

		double tempx = (v.x);
		int tempX = (int) tempx;
		if (v.x < 0) {
			tempx = (v.x);
			tempX = (int) tempx - 1;
		}

		double tempz = (v.z);
		int tempZ = (int) tempz;
		if (v.z > 0) {
			tempz = (v.z);
			tempZ = (int) tempz + 1;
		}

		double tempy = (v.y);
		int tempY = (int) tempy - 1;

		int bx = (int) tempX;
		int by = (int) tempY;
		int bz = (int) tempZ - 1;

		if (world.getGlobalBlock(bx, by + 1, bz).getId() == Block.Water.getId())
			underWater = true;
		else
			underWater = false;

		Velocity vel = Components.VELOCITY.get(this);

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_W)) {
			vel.setZ(vel.getZ() + -Math.cos(Math.toRadians(this.yaw)) * this.speed);
			vel.setX(vel.getX() + Math.sin(Math.toRadians(this.yaw)) * this.speed);
			isMoved = true;
		} else if (kbh.isKeyPressed(GLFW.GLFW_KEY_S)) {
			vel.setZ(vel.getZ() - -Math.cos(Math.toRadians(this.yaw)) * this.speed);
			vel.setX(vel.getX() - Math.sin(Math.toRadians(this.yaw)) * this.speed);
			isMoved = true;
		}

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_D)) {
			vel.setZ(vel.getZ() + Math.sin(Math.toRadians(this.yaw)) * this.speed);
			vel.setX(vel.getX() + Math.cos(Math.toRadians(this.yaw)) * this.speed);
			isMoved = true;
		} else if (kbh.isKeyPressed(GLFW.GLFW_KEY_A)) {
			vel.setZ(vel.getZ() - Math.sin(Math.toRadians(this.yaw)) * this.speed);
			vel.setX(vel.getX() - Math.cos(Math.toRadians(this.yaw)) * this.speed);
			isMoved = true;
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
			else
				speed = 1f;

			if (vel.getY() == 0)
				jump = false;

		}
		/*
		 * if (flyMode) { if (isKeyDown(KEY_SPACE)) vel.setY(5f * speed); } else
		 * { if (isKeyDown(KEY_SPACE) && !jump) { vel.setY(5f); jump = true; }
		 * if (vel.getY() == 0) jump = false; } if (flyMode) { if
		 * (isKeyDown(KEY_LSHIFT)) vel.setY(-5f * speed); } else { if
		 * (isKeyDown(KEY_LSHIFT)) speed = 0.2f; else speed = 1f;
		 * 
		 * } if (flyMode) { if (isKeyDown(Keyboard.KEY_LCONTROL)) speed = 6f;
		 * else speed = 1f; } else { if (isKeyDown(Keyboard.KEY_LCONTROL)) speed
		 * = 2f; else speed = 1f;
		 * 
		 * }
		 */

		/*
		 * if (isKeyDown(Keyboard.KEY_Y)) {
		 * gm.getWorldsHandler().getActiveWorld().switchDimension(0, gm); } if
		 * (isKeyDown(Keyboard.KEY_O)) {
		 * gm.getWorldsHandler().getActiveWorld().switchDimension(1, gm); }
		 */
		if (ClientVariables.debug) {
			// TODO: Readd the below debug keys
		}
		/*
		 * if (isKeyDown(KEY_T)) {
		 * gm.getWorldsHandler().getActiveWorld().getActiveDimension().
		 * getPhysicsEngine() .addEntity(new GuineaPig(new
		 * Vector3d(getPosition()))); }
		 */

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_1)) {
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
					.addEntity(Block.Indes.getDrop(getPosition()));
		}
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_2)) {
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
			.addEntity(Block.Wood.getDrop(getPosition()));
		}
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_3)) {
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
			.addEntity(Block.Ice.getDrop(getPosition()));
		}

		if (clickTime > 0)
			clickTime--;
		hit = false;

		setBlock(window.getWidth(), window.getHeight(), world);
		updateRay(gm.getRenderer().getProjectionMatrix(), window.getWidth(), window.getHeight(), center);

		super.update(delta);
	}

	@Override
	public void render() {
	}

	private void setBlock(int ww, int wh, Dimension world) {
		Vector4d viewport = new Vector4d(0, 0, ww, wh);
		Vector3d wincoord = new Vector3d(ww / 2, wh / 2, depth);
		Vector3d objcoord = new Vector3d();
		Matrix4d mvp = new Matrix4d();
		Matrix4d.mul(GameResources.getInstance().getRenderer().getProjectionMatrix(), Maths.createViewMatrix(this),
				mvp);
		objcoord = mvp.unproject(wincoord, viewport, objcoord);
		double bcx = 0, bcy = 0, bcz = 0;
		blocks = world
				.getGlobalBoundingBox(new BoundingBox(new Vector3(objcoord.x - 0.1, objcoord.y - 0.1, objcoord.z - 0.1),
						new Vector3(objcoord.x + 0.1, objcoord.y + 0.1, objcoord.z + 0.1)));
		for (BoundingBox boundingBox : blocks) {
			if (Maths.intersectRayBounds(getDRay().getRay(), boundingBox, tmp)) {
				bcx = boundingBox.getCenterX();
				bcy = boundingBox.getCenterY();
				bcz = boundingBox.getCenterZ();

				break;
			}

		}

		double tempx = (bcx);
		int tempX = (int) tempx;
		if (objcoord.x < 0) {
			tempx = (bcx);
			tempX = (int) tempx - 1;
		}

		double tempz = (bcz);
		int tempZ = (int) tempz;
		if (objcoord.z > 0) {
			tempz = (bcz);
			tempZ = (int) tempz + 1;
		}

		double tempy = (bcy);
		int tempY = (int) tempy;
		if (objcoord.y < 0) {
			tempy = (bcz);
			tempY = (int) tempy - 1;
		}

		int bx = (int) tempX;
		int by = (int) tempY;
		int bz = (int) tempZ - 1;
		if (clickTime == 0)
			if (isButtonDown(0)) {
				clickTime = 8;
				setBlock(bx, by, bz, new ItemGui(new Vector3d(), Block.Air), world);
				hit = true;
			} else if (isButtonDown(1)) {
				clickTime = 8;
				setBlock(bx, by, bz, block, world);
			}
	}

	private void setBlock(int bx, int by, int bz, ItemGui block, Dimension world) {

		if (block.getBlock().getId() == Block.Torch.getId())
			world.addLight(bx, by, bz, 14);
		if (block.getBlock().getId() == Block.Air.getId()
				&& world.getGlobalBlock(bx, by, bz).getId() != Block.Air.getId())
			world.getPhysicsEngine()
					.addEntity(world.getGlobalBlock(bx, by, bz).getDrop(new Vector3d(bx + 0.5f, by + 0.5f, bz + 0.5f)));
		if (block.getBlock().getId() == Block.Air.getId()
				&& world.getGlobalBlock(bx, by, bz).getId() == Block.Torch.getId())
			world.removeLight(bx, by, bz, 0);
		if (block.getBlock().getId() != 0) {
			block.setTotal(block.getTotal() - 1);
			bx += (int) (normal.x * 1.5f);
			by += (int) (normal.y * 1.5f);
			bz += (int) (normal.z * 1.5f);
		}
		if (block.getBlock() instanceof BlockEntity) {
			try {
				world.setGlobalBlock(bx, by, bz, block.getBlock().getClass()
						.getDeclaredConstructor(Integer.class, Integer.class, Integer.class).newInstance(bx, by, bz));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return;
		}
		world.setGlobalBlock(bx, by, bz, block.getBlock());
	}

	public int getyPos() {
		return yPos;
	}

	public void invertPitch() {
		pitch = -pitch;
	}

	public void setMouse() {
		setCursorPosition(GameResources.getInstance().getGameWindow().getWidth() / 2,
				GameResources.getInstance().getGameWindow().getHeight() / 2);
		setGrabbed(true);
	}

	public void unlockMouse() {
		setGrabbed(false);
	}

	public boolean isUnderWater() {
		return underWater;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public boolean isHit() {
		return hit;
	}

}
