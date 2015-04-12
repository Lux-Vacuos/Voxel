package org.gnet.packet;

public final class ClientShutdownPacket extends Packet {

	private static final long serialVersionUID = 1L;

	public ClientShutdownPacket() {
		super("ClientShutdownPacket", 1);
		super.addEntry("shutdownClient", Boolean.TRUE);
	}

}
