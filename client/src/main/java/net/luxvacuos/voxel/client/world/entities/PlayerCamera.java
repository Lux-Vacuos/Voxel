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

import static net.luxvacuos.voxel.client.input.Keyboard.KEY_A;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_D;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_LSHIFT;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_S;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_SPACE;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_T;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_W;
import static net.luxvacuos.voxel.client.input.Keyboard.isKeyDown;
import static net.luxvacuos.voxel.client.input.Mouse.getDX;
import static net.luxvacuos.voxel.client.input.Mouse.getDY;
import static net.luxvacuos.voxel.client.input.Mouse.isButtonDown;
import static net.luxvacuos.voxel.client.input.Mouse.setCursorPosition;
import static net.luxvacuos.voxel.client.input.Mouse.setGrabbed;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Display;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Inventory;
import net.luxvacuos.voxel.client.ui.menu.ItemGui;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.entities.components.ArmourComponent;
import net.luxvacuos.voxel.client.world.entities.components.LifeComponent;
import net.luxvacuos.voxel.client.world.entities.components.VelocityComponent;
import net.luxvacuos.voxel.client.world.items.EmptyArmour;

public class PlayerCamera extends Camera {

	private float speed;
	private float multiplierMouse = 14;
	private boolean underWater = false;
	private int mouseSpeed = 2;
	private final int maxLookUp = 90;
	private final int maxLookDown = -90;
	private Vector2f center;
	private int clickTime;
	private Vector3f normalVector = new Vector3f();
	private Inventory inventory;
	private int yPos;
	private ItemGui block;
	private boolean hit;

	public PlayerCamera(Matrix4f proj, Display display) {
		super(proj, new Vector3f(-0.25f, -1.4f, -0.25f), new Vector3f(0.25f, 0.2f, 0.25f));
		center = new Vector2f(display.getDisplayWidth() / 2, display.getDisplayHeight() / 2);
		this.speed = 3f;
		inventory = new Inventory(11, 11, 300, 0);
		super.add(new LifeComponent(20));
		super.add(new ArmourComponent());
		super.getComponent(ArmourComponent.class).armour = new EmptyArmour();
	}

	public void update(float delta, GameResources gm, Dimension world) {
		isMoved = false;

		if (super.getComponent(LifeComponent.class).life <= 0) {
			try {
				gm.getWorldsHandler().getActiveWorld().dispose(gm);
			} catch (Exception e) {
				e.printStackTrace();
			}
			gm.getCamera().setPosition(new Vector3f(0, 0, 1));
			gm.getCamera().setPitch(0);
			gm.getCamera().setYaw(0);
			gm.getGlobalStates().setState(GameState.MAINMENU);
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

		normalVector.set((float) Math.cos(yaw) * (float) Math.cos(pitch), (float) Math.sin(yaw),
				(float) Math.cos(yaw) * (float) Math.sin(pitch));

		Vector3f v = this.getPosition();

		float tempx = (v.x);
		int tempX = (int) tempx;
		if (v.x < 0) {
			tempx = (v.x);
			tempX = (int) tempx - 1;
		}

		float tempz = (v.z);
		int tempZ = (int) tempz;
		if (v.z > 0) {
			tempz = (v.z);
			tempZ = (int) tempz + 1;
		}

		float tempy = (v.y);
		int tempY = (int) tempy - 1;

		int bx = (int) tempX;
		int by = (int) tempY;
		int bz = (int) tempZ - 1;

		if (world.getGlobalBlock(bx, by + 1, bz) == Block.Water.getId())
			underWater = true;
		else
			underWater = false;

		if (isKeyDown(KEY_W)) {
			this.getComponent(VelocityComponent.class).velocity.z += -Math.cos(Math.toRadians(yaw)) * speed;
			this.getComponent(VelocityComponent.class).velocity.x += Math.sin(Math.toRadians(yaw)) * speed;
			isMoved = true;

		} else if (isKeyDown(KEY_S)) {
			this.getComponent(VelocityComponent.class).velocity.z -= -Math.cos(Math.toRadians(yaw)) * speed;
			this.getComponent(VelocityComponent.class).velocity.x -= Math.sin(Math.toRadians(yaw)) * speed;
			isMoved = true;
		}

		if (isKeyDown(KEY_D)) {
			this.getComponent(VelocityComponent.class).velocity.z += Math.sin(Math.toRadians(yaw)) * speed;
			this.getComponent(VelocityComponent.class).velocity.x += Math.cos(Math.toRadians(yaw)) * speed;
			isMoved = true;
		} else if (isKeyDown(KEY_A)) {
			this.getComponent(VelocityComponent.class).velocity.z -= Math.sin(Math.toRadians(yaw)) * speed;
			this.getComponent(VelocityComponent.class).velocity.x -= Math.cos(Math.toRadians(yaw)) * speed;
			isMoved = true;
		}
		if (isKeyDown(KEY_SPACE) && !jump) {
			this.getComponent(VelocityComponent.class).velocity.y = 5;
			jump = true;
		}
		if (this.getComponent(VelocityComponent.class).velocity.y == 0)
			jump = false;
		if (isKeyDown(KEY_LSHIFT)) {
			speed = 0.8f;
		} else {
			speed = 3;
		}
		/*
		 * if (isKeyDown(Keyboard.KEY_Y)) {
		 * gm.getWorldsHandler().getActiveWorld().switchDimension(0, gm); } if
		 * (isKeyDown(Keyboard.KEY_O)) {
		 * gm.getWorldsHandler().getActiveWorld().switchDimension(1, gm); }
		 */
		if (isKeyDown(KEY_T)) {
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
					.addEntity(new GuineaPig(getPosition()));
		}

		if (isKeyDown(Keyboard.KEY_8)) {
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
					.addEntity(Block.Torch.getDrop(gm, getPosition()));
		}

		if (clickTime > 0)
			clickTime--;
		hit = false;
		if (clickTime == 0)
			if (isButtonDown(0)) {
				clickTime = 8;
				setBlock(gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight(),
						new ItemGui(new Vector3f(), Block.Air), world, gm);
				hit = true;
			} else if (isButtonDown(1)) {
				clickTime = 8;
				setBlock(gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight(), block, world, gm);
			}

		updateRay(gm.getRenderer().getProjectionMatrix(), gm.getDisplay().getDisplayWidth(),
				gm.getDisplay().getDisplayHeight(), center);
	}

	private void setBlock(int ww, int wh, ItemGui block, Dimension world, GameResources gm) {
		Vector4f viewport = new Vector4f(0, 0, ww, wh);
		Vector3f wincoord = new Vector3f(ww / 2, wh / 2, depth);
		Vector3f objcoord = new Vector3f();
		Matrix4f mvp = new Matrix4f();
		Matrix4f.mul(gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(this), mvp);
		objcoord = mvp.unproject(wincoord, viewport, objcoord);

		float tempx = (objcoord.x);
		int tempX = (int) tempx;
		if (objcoord.x < 0) {
			tempx = (objcoord.x);
			tempX = (int) tempx - 1;
		}

		float tempz = (objcoord.z);
		int tempZ = (int) tempz;
		if (objcoord.z > 0) {
			tempz = (objcoord.z);
			tempZ = (int) tempz + 1;
		}

		float tempy = (objcoord.y);
		int tempY = (int) tempy;

		int bx = (int) tempX;
		int by = (int) tempY;
		int bz = (int) tempZ - 1;

		if (block.getBlock().getId() == Block.Torch.getId())
			world.addLight(bx, by, bz, 14);
		if (block.getBlock().getId() == Block.Air.getId() && world.getGlobalBlock(bx, by, bz) != Block.Air.getId())
			world.getPhysicsEngine().addEntity(Block.getBlock(world.getGlobalBlock(bx, by, bz)).getDrop(gm,
					new Vector3f(bx + 0.5f, by + 0.5f, bz + 0.5f)));
		if (block.getBlock().getId() == Block.Air.getId() && world.getGlobalBlock(bx, by, bz) == Block.Torch.getId())
			world.removeLight(bx, by, bz, 0);
		if (block.getBlock().getId() != 0)
			block.setTotal(block.getTotal() - 1);
		world.setGlobalBlock(bx, by, bz, block.getBlock().getId());
	}

	public int getyPos() {
		return yPos;
	}

	public void invertPitch() {
		pitch = -pitch;
	}

	public void setMouse(Display display) {
		setCursorPosition(display.getDisplayWidth() / 2, display.getDisplayHeight() / 2);
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
