package org.gnet.client;

public abstract class ClientEventListener {
	protected abstract void clientConnected(ServerModel server);

	protected abstract void clientDisconnected(ServerModel server);

	protected abstract void packetReceived(ServerModel server,
			org.gnet.packet.Packet packet);

	protected abstract void debugMessage(String msg);

	protected abstract void errorMessage(String msg);
}