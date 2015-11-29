package net.guerra24.voxel.client.particle;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;

	private float gravityEffect;
	private float lifeLeght;
	private float rotation;
	private float scale;
	private float elapsedTime = 0;

	public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLeght, float rotation,
			float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLeght = lifeLeght;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.getInstance().addParticle(this);
	}

	public boolean update(float delta) {
		velocity.y += VoxelVariables.GRAVITY * gravityEffect * delta;
		Vector3f change = new Vector3f(velocity);
		change.scale(delta);
		Vector3f.add(change, position, position);
		elapsedTime += delta;
		return elapsedTime < lifeLeght;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

}
