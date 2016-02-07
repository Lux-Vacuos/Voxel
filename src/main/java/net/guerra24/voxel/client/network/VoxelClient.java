package net.guerra24.voxel.client.network;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.network.packets.WorldTime;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class VoxelClient {

	private int port;
	private String url;
	private Client client;

	public VoxelClient(GameResources gm) {
		init(gm);
	}

	public void init(GameResources gm) {
		client = new Client();
		client.start();
		client.addListener(new DedicatedListener(gm));
		Kryo kryo = client.getKryo();
		kryo.register(Vector3f.class);
		kryo.register(WorldTime.class);
	}

	public void connect(int port, String url) {
		this.port = port;
		this.url = url;
		try {
			VoxelVariables.onServer = true;
			client.connect(10000, this.url, this.port);
		} catch (IOException e) {
			VoxelVariables.onServer = false;
			e.printStackTrace();
		}
	}

	public void dispose() {
		client.stop();
	}

	public Client getClient() {
		return client;
	}

}
