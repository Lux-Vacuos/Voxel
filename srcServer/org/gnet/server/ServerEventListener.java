package org.gnet.server;

public abstract class ServerEventListener {
	protected abstract void clientConnected(ClientModel client);

	protected abstract void clientDisconnected(ClientModel client);

	protected abstract void packetReceived(ClientModel client,
			org.gnet.packet.Packet packet);

	protected abstract void debugMessage(String msg);

	protected abstract void errorMessage(String msg);
}