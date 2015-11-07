package net.guerra24.voxel.universal.network.packets;

import java.io.Serializable;

import net.guerra24.voxel.universal.util.vector.Vector3f;

public class NetworkPosition implements Serializable {
	
	private static final long serialVersionUID = 6432280294007171803L;
	
	private Vector3f pos;

	public NetworkPosition(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
}
