package net.guerra24.voxel.client;

import java.io.IOException;
import java.util.Scanner;

import net.guerra24.voxel.client.engine.network.NetworkListener;
import net.guerra24.voxel.client.engine.network.packets.Packet.Packet0LoginRequest;
import net.guerra24.voxel.client.engine.network.packets.Packet.Packet1LoginAnswer;
import net.guerra24.voxel.client.engine.network.packets.Packet.Packet2Message;
import net.guerra24.voxel.client.engine.util.Logger;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class VoxelClient {

	public Client client;
	public Scanner scanner;

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
		new Client();
		Log.set(Log.LEVEL_DEBUG);
	}
}
