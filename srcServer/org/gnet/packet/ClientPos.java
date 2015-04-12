package org.gnet.packet;

public class ClientPos extends Packet {

	private static final long serialVersionUID = 2151235721382762901L;

	public ClientPos(String ClientPos, int dataSlots) {
		super("ClientPos", 1);
	}

}
