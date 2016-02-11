package net.guerra24.voxel.client.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.util.Logger;
import net.guerra24.voxel.universal.network.packets.UpdateNames;
import net.guerra24.voxel.universal.network.packets.Username;
import net.guerra24.voxel.universal.network.packets.WorldTime;

public class DedicatedListener extends Listener {

	private GameResources gm;

	public DedicatedListener(GameResources gm) {
		this.gm = gm;
	}

	@Override
	public void connected(Connection connection) {
		connection.sendTCP(new Username(connection.toString()));
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof WorldTime) {
			WorldTime time = (WorldTime) object;
			gm.getSkyboxRenderer().setTime(time.getTime());
		} else if (object instanceof String) {
			Logger.log((String) object);
		} else if (object instanceof UpdateNames) {
		}
	}

}
