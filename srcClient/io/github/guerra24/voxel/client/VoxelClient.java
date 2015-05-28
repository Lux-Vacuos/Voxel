package io.github.guerra24.voxel.client;

import io.github.guerra24.voxel.client.kernel.network.NetworkListener;
import io.github.guerra24.voxel.client.kernel.packets.Packet.Packet0LoginRequest;
import io.github.guerra24.voxel.client.kernel.packets.Packet.Packet1LoginAnswer;
import io.github.guerra24.voxel.client.kernel.packets.Packet.Packet2Message;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.io.IOException;
import java.util.Scanner;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class VoxelClient {

	public Client client;
	public static Scanner scanner;

	public VoxelClient() {
		scanner = new Scanner(System.in);
		client = new Client();
		register();
		NetworkListener nl = new NetworkListener();
		nl.init(client);
		client.addListener(nl);

		client.start();

		try {
			Logger.log("Please Enter the IP");
			client.connect(5000, scanner.nextLine(), 4059);
		} catch (IOException e) {
			e.printStackTrace();
			client.stop();
		}
	}

	private void register() {
		Kryo kryo = client.getKryo();
		kryo.register(Packet0LoginRequest.class);
		kryo.register(Packet1LoginAnswer.class);
		kryo.register(Packet2Message.class);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		VoxelClient voxel = new VoxelClient();
		Log.set(Log.LEVEL_DEBUG);
	}
}
