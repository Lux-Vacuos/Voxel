/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.world.entities;

import static net.guerra24.voxel.input.Keyboard.KEY_0;
import static net.guerra24.voxel.input.Keyboard.KEY_1;
import static net.guerra24.voxel.input.Keyboard.KEY_2;
import static net.guerra24.voxel.input.Keyboard.KEY_3;
import static net.guerra24.voxel.input.Keyboard.KEY_4;
import static net.guerra24.voxel.input.Keyboard.KEY_5;
import static net.guerra24.voxel.input.Keyboard.KEY_6;
import static net.guerra24.voxel.input.Keyboard.KEY_7;
import static net.guerra24.voxel.input.Keyboard.KEY_8;
import static net.guerra24.voxel.input.Keyboard.KEY_9;
import static net.guerra24.voxel.input.Keyboard.KEY_A;
import static net.guerra24.voxel.input.Keyboard.KEY_D;
import static net.guerra24.voxel.input.Keyboard.KEY_F3;
import static net.guerra24.voxel.input.Keyboard.KEY_J;
import static net.guerra24.voxel.input.Keyboard.KEY_K;
import static net.guerra24.voxel.input.Keyboard.KEY_LCONTROL;
import static net.guerra24.voxel.input.Keyboard.KEY_LSHIFT;
import static net.guerra24.voxel.input.Keyboard.KEY_R;
import static net.guerra24.voxel.input.Keyboard.KEY_S;
import static net.guerra24.voxel.input.Keyboard.KEY_SPACE;
import static net.guerra24.voxel.input.Keyboard.KEY_W;
import static net.guerra24.voxel.input.Keyboard.KEY_Y;
import static net.guerra24.voxel.input.Keyboard.isKeyDown;
import static net.guerra24.voxel.input.Mouse.getDX;
import static net.guerra24.voxel.input.Mouse.getDY;
import static net.guerra24.voxel.input.Mouse.isButtonDown;
import static net.guerra24.voxel.input.Mouse.setCursorPosition;
import static net.guerra24.voxel.input.Mouse.setGrabbed;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import net.guerra24.voxel.api.API;
import net.guerra24.voxel.core.VoxelVariables;
import net.guerra24.voxel.graphics.opengl.Display;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.GuiResources;
import net.guerra24.voxel.util.Maths;
import net.guerra24.voxel.util.vector.Matrix4f;
import net.guerra24.voxel.util.vector.Vector2f;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.util.vector.Vector4f;
import net.guerra24.voxel.world.IWorld;
import net.guerra24.voxel.world.block.Block;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Camera {

	private Vector3f position = new Vector3f(0, 0, 1);
	private float pitch;
	private float yaw;
	private float roll;
	private float speed;
	private float multiplierMouse = 24;
	private float multiplierMovement = 24;
	private int life = 0;
	private byte block = 2;
	private boolean teleporting = false;
	private int teleportingTime = 0;
	private boolean underWater = false;

	private int mouseSpeed = 2;
	private final int maxLookUp = 90;
	private final int maxLookDown = -90;

	public boolean isMoved = false;

	int id = 0;

	public Camera() {
		this.speed = 0.2f;
	}

	public void update(float delta, GameResources gm, GuiResources gi, IWorld world, API api) {
		isMoved = false;
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
		int bz = (int) tempZ;

		if (isKeyDown(KEY_K))
			id = VoxelVariables.DIM_0;
		if (isKeyDown(KEY_J))
			id = VoxelVariables.DIM_1;

		if (world.getGlobalBlock(bx, by, bz + 1) == Block.Portal.getId() && teleporting == false) {
			teleporting = true;
			world.switchDimension(id, gm, api);
		}
		if (teleporting) {
			teleportingTime++;
			if (teleportingTime >= 100) {
				teleporting = false;
				teleportingTime = 0;
			}
		}

		if (world.getGlobalBlock(bx, by + 1, bz) == Block.Water.getId())
			underWater = true;
		else
			underWater = false;

		int xa = world.getGlobalBlock(bx + 1, by, bz);
		int xb = world.getGlobalBlock(bx - 1, by, bz);
		int za = world.getGlobalBlock(bx, by, bz + 1);
		int zb = world.getGlobalBlock(bx, by, bz - 1);

		if (isKeyDown(KEY_W)) {
			if (yaw > 90 && yaw < 270) {
				if (za == 0 || za == Block.Water.getId())
					position.z += -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if ((yaw > 270 && yaw < 360) || (yaw > 0 && yaw < 90)) {
				if (zb == 0 || zb == Block.Water.getId())
					position.z += -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.z += -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}

			if (yaw > 0 && yaw < 180) {
				if (xa == 0 || xa == Block.Water.getId())
					position.x += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if (yaw > 180 && yaw < 360) {
				if (xb == 0 || xb == Block.Water.getId())
					position.x += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.x += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}
			isMoved = true;

		} else if (isKeyDown(KEY_S)) {

			if (yaw > 90 && yaw < 270) {
				if (zb == 0 || zb == Block.Water.getId())
					position.z -= -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if ((yaw > 270 && yaw < 360) || (yaw > 0 && yaw < 90)) {
				if (za == 0 || za == Block.Water.getId())
					position.z -= -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.z -= -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}

			if (yaw > 0 && yaw < 180) {
				if (xb == 0 || xb == Block.Water.getId())
					position.x -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if (yaw > 180 && yaw < 360) {
				if (xa == 0 || xa == Block.Water.getId())
					position.x -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.x -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}

			isMoved = true;
		}

		if (isKeyDown(KEY_D)) {

			if (yaw > 0 && yaw < 180) {
				if (za == 0 || za == Block.Water.getId())
					position.z += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if (yaw > 180 && yaw < 360) {
				if (zb == 0 || zb == Block.Water.getId())
					position.z += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.z += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}

			if (yaw > 90 && yaw < 270) {
				if (xb == 0 || xb == Block.Water.getId())
					position.x += Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if ((yaw > 270 && yaw < 360) || (yaw > 0 && yaw < 90)) {
				if (xa == 0 || xa == Block.Water.getId())
					position.x += Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.x += Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}

			isMoved = true;
		} else if (isKeyDown(KEY_A)) {
			if (yaw > 0 && yaw < 180) {
				if (zb == 0 || zb == Block.Water.getId())
					position.z -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if (yaw > 180 && yaw < 360) {
				if (za == 0 || za == Block.Water.getId())
					position.z -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.z -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}

			if (yaw > 90 && yaw < 270) {
				if (xa == 0 || xa == Block.Water.getId())
					position.x -= Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else if ((yaw > 270 && yaw < 360) || (yaw > 0 && yaw < 90)) {
				if (xb == 0 || xb == Block.Water.getId())
					position.x -= Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			} else {
				position.x -= Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			}
			isMoved = true;
		}
		if (isKeyDown(KEY_SPACE)) {
			gm.getPhysics().getMobManager().getPlayer().jump();
		}
		if (isKeyDown(KEY_LSHIFT)) {
			speed = 0.05f;
		} else {
			speed = 0.2f;
		}
		if (isKeyDown(KEY_LCONTROL)) {
			speed = 1;
		} else {
			speed = 0.2f;
		}
		if (isKeyDown(KEY_Y)) {
			System.out.println(yaw);
			System.out.println(position);
		}

		if (isKeyDown(KEY_1))
			block = 1;
		else if (isKeyDown(KEY_2))
			block = 2;
		else if (isKeyDown(KEY_3))
			block = 3;
		else if (isKeyDown(KEY_4))
			block = 4;
		else if (isKeyDown(KEY_5))
			block = 5;
		else if (isKeyDown(KEY_6))
			block = 6;
		else if (isKeyDown(KEY_7))
			block = 8;
		else if (isKeyDown(KEY_8))
			block = 9;
		else if (isKeyDown(KEY_9))
			block = 10;
		else if (isKeyDown(KEY_0))
			block = 30;
		if (isButtonDown(0)) {
			setBlock(Display.getWidth(), Display.getHeight(), (byte) 0, world, gm);
		} else if (isButtonDown(1)) {
			setBlock(Display.getWidth(), Display.getHeight(), block, world, gm);
			if (block == 9)
				world.lighting(bx, by, bz, 12);
		}

		updatePlayerState(gi);
		updateDebug(world);
	}

	public void moveToPosition(Vector3f pos) {
		if (pos.getX() > position.getX())
			increasePosition(0.01f, 0, 0);
		if (pos.getX() < position.getX())
			increasePosition(-0.01f, 0, 0);
		if (pos.getY() > position.getY())
			increasePosition(0, 0.01f, 0);
		if (pos.getY() < position.getY())
			increasePosition(0, -0.01f, 0);
		if (pos.getZ() > position.getZ())
			increasePosition(0, 0, 0.01f);
		if (pos.getZ() < position.getZ())
			increasePosition(0, 0, -0.01f);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void updatePlayerState(GuiResources gi) {
		if (isKeyDown(KEY_K))
			life += 1;
		setLife(life, gi);
	}

	public void updateDebug(IWorld world) {
		if (isKeyDown(KEY_F3) && isKeyDown(KEY_R))
			for (int zr = -VoxelVariables.genRadius; zr <= VoxelVariables.genRadius; zr++) {
				int zz = world.getzPlayChunk() + zr;
				for (int xr = -VoxelVariables.genRadius; xr <= VoxelVariables.genRadius; xr++) {
					int xx = world.getxPlayChunk() + xr;
					for (int yr = -VoxelVariables.genRadius; yr <= VoxelVariables.genRadius; yr++) {
						int yy = world.getyPlayChunk() + yr;
						if (zr * zr + xr * xr + yr * yr <= VoxelVariables.genRadius * VoxelVariables.genRadius
								* VoxelVariables.genRadius) {
							if (world.hasChunk(world.getChunkDimension(), xx, yy, zz)) {
								world.getChunk(world.getChunkDimension(), xx, yy, zz).dispose();
								world.getChunk(world.getChunkDimension(), xx, yy, zz).needsRebuild = true;
								world.setTempRadius(0);
							}
						}
					}
				}
			}
	}

	private void setBlock(int ww, int wh, byte block, IWorld world, GameResources gm) {
		FloatBuffer p = BufferUtils.createFloatBuffer(1);
		glReadPixels(ww / 2, wh / 2, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, p);

		Vector4f viewport = new Vector4f(0, 0, ww, wh);
		Vector3f wincoord = new Vector3f(ww / 2, wh / 2, p.get(0));
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
		int bz = (int) tempZ;

		world.setGlobalBlock(bx, by, bz, block);
	}

	public void invertPitch() {
		pitch = -pitch;
	}

	public void setMouse() {
		setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		setGrabbed(true);
	}

	public void unlockMouse() {
		setGrabbed(false);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getLife() {
		return life;
	}

	public void setLife(int life, GuiResources gi) {
		if (life >= 0 && life <= 20) {
			float temp = 0;
			for (int templ = 0; templ < life; temp += 0.0173f)
				templ += 1;
			gi.life.setScale(new Vector2f(temp, 0.02f));
			this.life = life;
		}
	}

	public boolean isTeleporting() {
		return teleporting;
	}

	public boolean isUnderWater() {
		return underWater;
	}

}