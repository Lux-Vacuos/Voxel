/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.world.entities;

import static net.guerra24.voxel.client.input.Keyboard.KEY_0;
import static net.guerra24.voxel.client.input.Keyboard.KEY_1;
import static net.guerra24.voxel.client.input.Keyboard.KEY_2;
import static net.guerra24.voxel.client.input.Keyboard.KEY_3;
import static net.guerra24.voxel.client.input.Keyboard.KEY_4;
import static net.guerra24.voxel.client.input.Keyboard.KEY_5;
import static net.guerra24.voxel.client.input.Keyboard.KEY_6;
import static net.guerra24.voxel.client.input.Keyboard.KEY_7;
import static net.guerra24.voxel.client.input.Keyboard.KEY_8;
import static net.guerra24.voxel.client.input.Keyboard.KEY_9;
import static net.guerra24.voxel.client.input.Keyboard.KEY_A;
import static net.guerra24.voxel.client.input.Keyboard.KEY_D;
import static net.guerra24.voxel.client.input.Keyboard.KEY_I;
import static net.guerra24.voxel.client.input.Keyboard.KEY_LCONTROL;
import static net.guerra24.voxel.client.input.Keyboard.KEY_LSHIFT;
import static net.guerra24.voxel.client.input.Keyboard.KEY_O;
import static net.guerra24.voxel.client.input.Keyboard.KEY_S;
import static net.guerra24.voxel.client.input.Keyboard.KEY_SPACE;
import static net.guerra24.voxel.client.input.Keyboard.KEY_T;
import static net.guerra24.voxel.client.input.Keyboard.KEY_W;
import static net.guerra24.voxel.client.input.Keyboard.isKeyDown;
import static net.guerra24.voxel.client.input.Mouse.getDX;
import static net.guerra24.voxel.client.input.Mouse.getDY;
import static net.guerra24.voxel.client.input.Mouse.isButtonDown;
import static net.guerra24.voxel.client.input.Mouse.setCursorPosition;
import static net.guerra24.voxel.client.input.Mouse.setGrabbed;

import com.badlogic.ashley.core.Entity;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.input.Keyboard;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.Ray;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.universal.resources.UniversalResources;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;
import net.guerra24.voxel.universal.util.vector.Vector4f;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Camera extends Entity {

	private float pitch;
	private float yaw;
	private float roll;
	private float speed;
	private float multiplierMouse = 24;
	private float multiplierMovement = 1;
	private byte block = 2;
	private boolean underWater = false;
	private int mouseSpeed = 2;
	private final int maxLookUp = 90;
	private final int maxLookDown = -90;
	private Ray ray;
	private Vector2f center;
	private boolean jump = false;

	private VelocityComponent velocityComponent;
	private PositionComponent positionComponent;
	private CollisionComponent collisionComponent;

	public boolean isMoved = false;
	public float depth = 0;

	int id = 0;

	public Camera(Matrix4f proj, Display display) {
		this.speed = 3f;
		center = new Vector2f(display.getDisplayWidth() / 2, display.getDisplayHeight() / 2);
		velocityComponent = new VelocityComponent();
		velocityComponent.y = -9.8f;
		positionComponent = new PositionComponent();
		collisionComponent = new CollisionComponent();
		this.add(velocityComponent);
		this.add(positionComponent);
		this.add(collisionComponent);
		ray = new Ray(proj, Maths.createViewMatrix(this), center, display.getDisplayWidth(),
				display.getDisplayHeight());
	}

	public void update(float delta, GameResources gm, IWorld world) {
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

		if (world.getGlobalBlock(bx, by + 1, bz) == Block.Water.getId())
			underWater = true;
		else
			underWater = false;

		if (isKeyDown(KEY_W)) {
			velocityComponent.z += -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			velocityComponent.x += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;

		} else if (isKeyDown(KEY_S)) {
			velocityComponent.z -= -Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			velocityComponent.x -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;
		}

		if (isKeyDown(KEY_D)) {
			velocityComponent.z += Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			velocityComponent.x += Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;
		} else if (isKeyDown(KEY_A)) {
			velocityComponent.z -= Math.sin(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			velocityComponent.x -= Math.cos(Math.toRadians(yaw)) * delta * speed * multiplierMovement;
			isMoved = true;
		}
		if (isKeyDown(KEY_SPACE) && !jump) {
			velocityComponent.y = 200 * delta;
			jump = true;
		}
		if (velocityComponent.y == 0)
			jump = false;
		if (isKeyDown(KEY_LSHIFT)) {
			multiplierMovement = 1 * delta;
		} else {
			multiplierMovement = 120 * delta;
		}
		if (isKeyDown(KEY_LCONTROL)) {
			multiplierMovement = 200 * delta;
		} else {
			multiplierMovement = 120 * delta;
		}

		velocityComponent.x = Maths.clamp(velocityComponent.x, -2, 2);
		velocityComponent.z = Maths.clamp(velocityComponent.z, -2, 2);

		if (isKeyDown(Keyboard.KEY_Y)) {
			System.out.println(positionComponent.toString());
			System.out.println(velocityComponent.toString());
		}

		if (isKeyDown(KEY_T)) {
			gm.getPhysicsEngine()
					.addEntity(new GameEntity(UniversalResources.player,
							new Vector3f(this.getPosition().x, this.getPosition().y + 2, this.getPosition().z),
							velocityComponent.x, velocityComponent.y, velocityComponent.z, 0, 0, 0, 1));
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
			block = 13;
		if (isButtonDown(0)) {
			setBlock(gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight(), (byte) 0, world, gm);
		} else if (isButtonDown(1)) {
			setBlock(gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight(), block, world, gm);
		}

		updateDebug(world);
		updateRay(gm, false);
	}

	public void updateRay(GameResources gm, boolean invert) {
		if (invert)
			ray = new Ray(gm.getRenderer().getProjectionMatrix(), Matrix4f.invert(Maths.createViewMatrix(this), null),
					center, gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight());
		else
			ray = new Ray(gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(this), center,
					gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight());
	}

	public void updateDebug(IWorld world) {
		if (isKeyDown(KEY_I)) {
			VoxelVariables.radius++;
		}
		if (isKeyDown(KEY_O)) {
			VoxelVariables.radius--;
		}
	}

	private void setBlock(int ww, int wh, byte block, IWorld world, GameResources gm) {
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
		int bz = (int) tempZ;
		world.setGlobalBlock(bx, by, bz, block);
		if (block == 9)
			world.lighting(bx, by, bz, 14);
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

	public Vector3f getPosition() {
		return positionComponent.position;
	}

	public void setPosition(Vector3f position) {
		this.positionComponent.position = position;
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

	public boolean isUnderWater() {
		return underWater;
	}

	public Ray getRay() {
		return ray;
	}

	public CollisionComponent getCollisionComponent() {
		return collisionComponent;
	}

}