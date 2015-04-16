package net.guerra24.voxel.client.engine.network.packets;

public class Packet {
	public static class Packet0LoginRequest {
		public String user;
		public String pass;
	}

	public static class Packet1LoginAnswer {
		public boolean accepted = false;
	}

	public static class Packet2Message {
		public String message;
	}
}