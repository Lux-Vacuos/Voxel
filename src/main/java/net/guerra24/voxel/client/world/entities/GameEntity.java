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

package net.guerra24.voxel.client.world.entities;

import com.badlogic.ashley.core.Entity;

import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class GameEntity extends Entity {

	private TexturedModel model;
	private PositionComponent positionComponent;
	private VelocityComponent velocityComponent;
	private float rotX, rotY, rotZ;
	private float scale;
	private int visibility;

	public GameEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		velocityComponent = new VelocityComponent();
		positionComponent = new PositionComponent();
		positionComponent.position = new Vector3f(position);
		this.add(positionComponent);
		this.add(velocityComponent);
		this.model = model;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public GameEntity(TexturedModel model, Vector3f position,float vx, float vy, float vz, float rotX, float rotY, float rotZ, float scale) {
		velocityComponent = new VelocityComponent();
		positionComponent = new PositionComponent();
		positionComponent.position = new Vector3f(position);
		velocityComponent.x = vx;
		velocityComponent.y = vy;
		velocityComponent.z = vz;
		this.add(positionComponent);
		this.add(velocityComponent);
		this.model = model;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.positionComponent.position.x += dx;
		this.positionComponent.position.y += dy;
		this.positionComponent.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return positionComponent.position;
	}

	public void setPosition(Vector3f position) {
		this.positionComponent.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

}
