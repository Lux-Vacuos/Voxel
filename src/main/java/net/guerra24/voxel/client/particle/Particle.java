package net.guerra24.voxel.client.particle;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;

	private float gravityEffect;
	private float lifeLeght;
	private float rotation;
	private float scale;
	private float elapsedTime = 0;

	private ParticleTexture texture;

	private Vector2f texOffset0 = new Vector2f();
	private Vector2f texOffset1 = new Vector2f();
	private float blend;
	private float distance;

	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLeght,
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
		velocity.y += VoxelVariables.GRAVITY * gravityEffect * delta;
		Vector3f change = new Vector3f(velocity);
		change.scale(delta);
		Vector3f.add(change, position, position);

		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();

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

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumbreOfRows();
		int row = index / texture.getNumbreOfRows();
		offset.x = (float) column / texture.getNumbreOfRows();
		offset.y = (float) row / texture.getNumbreOfRows();
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

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector2f getTexOffset0() {
		return texOffset0;
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public float getBlend() {
		return blend;
	}

	public float getDistance() {
		return distance;
	}

}
