package net.guerra24.voxel.universal.network.packets;

import java.io.Serializable;

public class Username implements Serializable {

	private static final long serialVersionUID = -4990680947444970866L;

	private String user;

	public Username() {
	}

	public Username(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

}
