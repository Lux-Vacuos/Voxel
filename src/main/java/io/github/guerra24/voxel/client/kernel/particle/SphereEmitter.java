package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

public class SphereEmitter extends ParticleEmitter {
	private static final int MinInclination = 1;
	private static final int MaxInclination = 10;
	private static final int MinAzimuth = 1;
	private static final int MaxAzimuth = 10;
	private static final int MinimumRadius = 0;
	private static final int MaximumRadius = 10;
	private static final int MinSpeed = 0;
	private static final int MaxSpeed = 1;
	private static final int MaxLifetime = 5;
	private static final int MinLifetime = 2;

	public SphereEmitter(Particle particle) {
		float inclination = (float) Math.toRadians(Maths.randInt(
				MinInclination, MaxInclination));
		float azimuth = (float) Math.toRadians(Maths.randInt(MinAzimuth,
				MaxAzimuth));

		float radius = Maths.randInt(MinimumRadius, MaximumRadius);
		float speed = Maths.randInt(MinSpeed, MaxSpeed);
		float lifetime = Maths.randInt(MinLifetime, MaxLifetime);

		float sInclination = (float) Math.sin(inclination);

		float X = (float) (sInclination * Math.cos(azimuth));
		float Y = (float) (sInclination * Math.sin(azimuth));
		float Z = (float) Math.cos(inclination);

		Vector3f pos = new Vector3f(X, Y, Z);

		particle.setM_Position(new Vector3f(pos.x * radius, pos.y * radius,
				pos.z * radius));
		particle.setM_Velocity(new Vector3f(pos.x * speed, pos.y * speed, pos.z
				* speed));

		particle.setM_fLifeTime(lifetime);
		particle.setM_fAge(0);
	}
}
