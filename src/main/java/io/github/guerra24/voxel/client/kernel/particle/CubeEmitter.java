package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

public class CubeEmitter {
	private static final int MinWidth = 0;
	private static final int MaxWidth = 0;
	private static final int MinHeight = 0;
	private static final int MaxHeight = 0;
	private static final int MinDepth = 0;
	private static final int MaxDepth = 0;
	private static final int MinLifetime = 2;
	private static final int MaxLifetime = 5;
	private static final int MinSpeed = 1;
	private static final int MaxSpeed = 5;

	public CubeEmitter(Particle particle) {
		float X = Maths.randInt(MinWidth, MaxWidth);
		float Y = Maths.randInt(MinHeight, MaxHeight);
		float Z = Maths.randInt(MinDepth, MaxDepth);

		float lifetime = Maths.randInt(MinLifetime, MaxLifetime);
		float speed = Maths.randInt(MinSpeed, MaxSpeed);

		Vector3f pos = new Vector3f(X, Y, Z);

		particle.setM_Position(pos);
		pos.normalise();
		particle.setM_Velocity(new Vector3f(pos.x * speed, pos.y * speed, pos.z
				* speed));

		particle.setM_fLifeTime(lifetime);
		;
		particle.setM_fAge(0);
	}
}
