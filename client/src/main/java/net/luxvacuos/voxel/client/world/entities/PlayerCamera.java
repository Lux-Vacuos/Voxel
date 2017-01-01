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

package net.luxvacuos.voxel.client.world.entities;

import static net.luxvacuos.voxel.client.input.Mouse.getDX;
import static net.luxvacuos.voxel.client.input.Mouse.getDY;
import static net.luxvacuos.voxel.client.input.Mouse.setCursorPosition;
import static net.luxvacuos.voxel.client.input.Mouse.setGrabbed;

import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.Health;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;

public class PlayerCamera extends Camera {

	private boolean jump = false;
	private float speed;
	private boolean underWater = false;
	private int mouseSpeed = 8;
	private final int maxLookUp = 90;
	private final int maxLookDown = -90;
	private boolean flyMode = false;

	public PlayerCamera(Matrix4d projectionMatrix, Window window) {
		this.projectionMatrix = projectionMatrix;
		this.add(new Velocity());
		this.add(new Scale());
		this.add(new AABB(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f))
				.setBoundingBox(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f)));
		this.speed = 1f;
		super.add(new Health(20));
		if (flyMode)
			Components.AABB.get(this).setEnabled(false);
		this.viewMatrix = Maths.createViewMatrix(this);
	}

	@Override
	public void update(float delta) {
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
			else
				speed = 1f;

			if (vel.getY() == 0)
				jump = false;

		}
	}

	@Override
	public void afterUpdate(float delta) {
		viewMatrix = Maths.createViewMatrix(this);
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

}
