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

import com.badlogic.ashley.core.Entity;

import net.guerra24.voxel.client.resources.Ray;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public abstract class Camera extends Entity {

	protected float pitch;
	protected float yaw;
	protected float roll;
	protected Ray ray;
	protected boolean jump = false;

	protected VelocityComponent velocityComponent;
	protected PositionComponent positionComponent;
	protected CollisionComponent collisionComponent;

	public boolean isMoved = false;
	public float depth = 0;

	public Camera(Matrix4f proj) {
		velocityComponent = new VelocityComponent();
		positionComponent = new PositionComponent();
		collisionComponent = new CollisionComponent();
		this.add(velocityComponent);
		this.add(positionComponent);
		this.add(collisionComponent);
		ray = new Ray(proj, Maths.createViewMatrix(this), new Vector2f(), 0, 0);
	}

	public void updateRay(Matrix4f projectionMatrix, int width, int height, Vector2f pos) {
		ray = new Ray(projectionMatrix, Maths.createViewMatrix(this), pos, width, height);
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

	public Ray getRay() {
		return ray;
	}

	public CollisionComponent getCollisionComponent() {
		return collisionComponent;
	}

}