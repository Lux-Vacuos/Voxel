package net.guerra24.voxel.universal.network.packets;

import java.io.Serializable;

public class WorldTime implements Serializable {

	private static final long serialVersionUID = 2817182605309483282L;

	private int time;

	public WorldTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

}
