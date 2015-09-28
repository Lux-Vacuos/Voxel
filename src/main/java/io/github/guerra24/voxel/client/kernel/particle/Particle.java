package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.World;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;

public class Particle extends Entity {

	private final float GRAVITY = 0.001f;
	private float lifeTime;
	private float xVel = 0;
	private float yVel = 0;
	private float zVel = 0;

	public Particle(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float lifeTime,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.lifeTime = lifeTime;
	}

	public void update(float delta, GameControllers gm, GuiResources gi, World world) {
		Vector3f normal = new Vector3f(0, 1, 0);
		Vector3f dir = Vector3f.sub(
				new Vector3f(gm.getCamera().getPosition().x, gm.getCamera().getPosition().y,
						gm.getCamera().getPosition().z),
				new Vector3f(super.getPosition().x, super.getPosition().y, super.getPosition().z), null);
		dir = (Vector3f) dir.normalise();
		float angle = (float) Math.toDegrees(Math.acos(Vector3f.dot(normal, dir)));
		Vector3f rotationAxis = Vector3f.cross(normal, dir, null);
		rotationAxis = (Vector3f) rotationAxis.normalise();
		super.setRotX(angle * rotationAxis.x);
		super.setRotY(angle * rotationAxis.y);
		super.setRotZ(angle * rotationAxis.z);
		yVel += GRAVITY * delta;
		super.increasePosition(xVel, yVel, zVel);
		lifeTime--;
	}

	public float getLifeTime() {
		return lifeTime;
	}

}
