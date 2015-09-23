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

package io.github.guerra24.voxel.client.kernel.world.entities;

import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_1;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_2;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_3;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_4;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_5;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_6;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_7;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_8;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_A;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_D;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_F3;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_K;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_LCONTROL;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_LSHIFT;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_R;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_S;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_SPACE;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_W;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.KEY_Y;
import static io.github.guerra24.voxel.client.kernel.input.Keyboard.isKeyDown;
import static io.github.guerra24.voxel.client.kernel.input.Mouse.getDX;
import static io.github.guerra24.voxel.client.kernel.input.Mouse.getDY;
import static io.github.guerra24.voxel.client.kernel.input.Mouse.isButtonDown;
import static io.github.guerra24.voxel.client.kernel.input.Mouse.setCursorPosition;
import static io.github.guerra24.voxel.client.kernel.input.Mouse.setGrabbed;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.Display;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector2f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.World;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Camera implements IEntity {

	private Vector3f position = new Vector3f(-2, 0, -1);
	private float pitch;
	private float yaw;
	private float speed;
	private float multiplierMouse = 24;
	private float multiplierMovement = 24;
	private int life = 0;
	private byte block = 2;

	private static int mouseSpeed = 2;
	private static final int maxLookUp = 90;
	private static final int maxLookDown = -90;

	public boolean isMoved = false;

	public Camera() {
		this.speed = 0.2f;
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi) {
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
		if (isKeyDown(KEY_W)) {
			position.z += -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			position.x += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;

		} else if (isKeyDown(KEY_S)) {

			position.z -= -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			position.x -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;
		}

		if (isKeyDown(KEY_D)) {
			position.z += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			position.x += Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;
		} else if (isKeyDown(KEY_A)) {
			position.z -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			position.x -= Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;
		}
		if (isKeyDown(KEY_SPACE)) {
			// position.y += delta * speed
			// * multiplierMovement;
			gm.getPhysics().getMobManager().getPlayer().jump();
		}
		if (isKeyDown(KEY_LSHIFT)) {
			// position.y -= delta * speed
			// * multiplierMovement;
			speed = 0.05f;
		} else {
			speed = 0.2f;
		}
		if (isKeyDown(KEY_LCONTROL)) {
			speed = 1;
		} else {
			speed = 0.2f;
		}

		if (isKeyDown(KEY_Y))
			System.out.println(position);
		updatePlayerState(gi);
	}

	public void updatePlayerState(GuiResources gi) {
		if (isKeyDown(KEY_K))
			life += 1;
		setLife(life, gi);
	}

	public void updatePicker() {

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
		if (isButtonDown(0)) {
			// Vector3f pos = calculatePicker();
			// Kernel.world.setGlobalBlock(Kernel.world.dim, (int) pos.x, (int)
			// pos.y, (int) pos.z, (byte) 0);
		} else if (isButtonDown(1)) {
			// Vector3f pos = calculatePicker();
			// Kernel.world.setGlobalBlock(Kernel.world.dim, (int) pos.x, (int)
			// pos.y, (int) pos.z, block);
		}
	}

	public void updateDebug(World world) {
		if (isKeyDown(KEY_F3) && isKeyDown(KEY_R))
			for (int zr = -KernelConstants.genRadius; zr <= KernelConstants.genRadius; zr++) {
				int zz = world.getzPlayChunk() + zr;
				for (int xr = -KernelConstants.genRadius; xr <= KernelConstants.genRadius; xr++) {
					int xx = world.getxPlayChunk() + xr;
					if (zr * zr + xr * xr <= KernelConstants.genRadius * KernelConstants.genRadius) {
						if (world.hasChunk(world.dim, xx, zz)) {
							world.getChunk(world.dim, xx, zz).clear();
							world.tempRadius = 0;
						}
					}
				}
			}
	}
	
	public void invertPitch(){
		pitch = -pitch;
	}

	public void setMouse() {
		setGrabbed(true);
		setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
	}

	@Override
	public Entity getEntity() {
		return null;
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

}