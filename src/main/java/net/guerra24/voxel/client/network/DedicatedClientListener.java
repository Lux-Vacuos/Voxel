package net.guerra24.voxel.client.network;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.listener.SocketListener;

import net.guerra24.voxel.client.resources.GameResources;

public class DedicatedClientListener implements SocketListener {

	private final GameResources gm;

	public DedicatedClientListener(GameResources gm) {
		this.gm = gm;
	}

	@Override
	public void connected(Connection con) {
	}

	@Override
	public void disconnected(Connection con) {
	}

	@Override
	public void received(Connection con, Object obj) {
	}

}
