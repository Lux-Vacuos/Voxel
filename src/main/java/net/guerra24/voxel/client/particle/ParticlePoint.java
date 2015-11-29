package net.guerra24.voxel.client.particle;

import net.guerra24.voxel.universal.util.vector.Vector3f;

public class ParticlePoint {
	private Vector3f pos;

	public ParticlePoint(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getPos() {
		return pos;
	}
}
