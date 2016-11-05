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

package net.luxvacuos.voxel.client.resources.models;

import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.world.entities.Camera;

public class Particle {

	private Vector3d position;
	private Vector3d velocity;

	private float gravityEffect;
	private float lifeLeght;
	private float rotation;
	private float scale;
	private float elapsedTime = 0;

	private ParticleTexture texture;

	private Vector2d texOffset0 = new Vector2d();
	private Vector2d texOffset1 = new Vector2d();
	private float blend;
	private float distance;

	public Particle(ParticleTexture texture, Vector3d position, Vector3d velocity, float gravityEffect, float lifeLeght,
			float rotation, float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLeght = lifeLeght;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.getInstance().addParticle(this);
	}

	public boolean update(float delta, Camera camera) {
		velocity.y += -9.8 * gravityEffect * delta;
		Vector3d change = new Vector3d(velocity);
		change.scale(delta);
		Vector3d.add(change, position, position);

		distance = (float) Vector3d.sub(camera.getPosition(), position, null).lengthSquared();

		updateTextureCoord();
		elapsedTime += delta;
		return elapsedTime < lifeLeght;
	}

	private void updateTextureCoord() {
		float lifeFactor = elapsedTime / lifeLeght;
		int stageCount = texture.getNumbreOfRows() * texture.getNumbreOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index0 = (int) Math.floor(atlasProgression);
		int index1 = index0 < stageCount - 1 ? index0 + 1 : index0;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset0, index0);
		setTextureOffset(texOffset1, index1);
	}

	private void setTextureOffset(Vector2d offset, int index) {
		int column = index % texture.getNumbreOfRows();
		int row = index / texture.getNumbreOfRows();
		offset.x = (float) column / texture.getNumbreOfRows();
		offset.y = (float) row / texture.getNumbreOfRows();
	}

	public Vector3d getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector2d getTexOffset0() {
		return texOffset0;
	}

	public Vector2d getTexOffset1() {
		return texOffset1;
	}

	public float getBlend() {
		return blend;
	}

	public float getDistance() {
		return distance;
	}

}
