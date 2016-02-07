package net.guerra24.voxel.client.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.network.packets.WorldTime;

public class DedicatedListener extends Listener {

	private GameResources gm;

	public DedicatedListener(GameResources gm) {
		this.gm = gm;
	}

	@Override
	public void connected(Connection connection) {
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof WorldTime) {
			WorldTime time = (WorldTime) object;
			gm.getSkyboxRenderer().setTime(time.getTime());
		}
	}

}
