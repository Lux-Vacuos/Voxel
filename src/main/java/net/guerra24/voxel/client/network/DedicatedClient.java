package net.guerra24.voxel.client.network;

import com.jmr.wrapper.client.Client;

import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.util.Logger;

public class DedicatedClient {

	private Client client;
	private DedicatedClientListener dedicatedClientListener;

	public DedicatedClient(GameResources gm) {
		dedicatedClientListener = new DedicatedClientListener(gm);
		client = new Client("localhost", 4059, 4059);
		client.setListener(dedicatedClientListener);
		client.connect();
		if (client.isConnected()) {
			Logger.log("Client has successfully connected");
		}
	}

	public Client getClient() {
		return client;
	}
}
