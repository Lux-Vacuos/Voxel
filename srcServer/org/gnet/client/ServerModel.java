package org.gnet.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import org.gnet.packet.Packet;

public class ServerModel {

	final ObjectOutputStream oos;
	final ObjectInputStream ois;
	private final GNetClient client;

	public ServerModel(final GNetClient client, final ObjectOutputStream oos,
			final ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
		this.client = client;
	}

	public void sendPacket(final Packet packet) {
		if (oos != null && client.isBinded() && client.connected) {
			try {
				oos.writeObject(packet);
				oos.flush();
				client.debug("Packet sent to server: " + packet.getPacketName());

			} catch (final SocketException e) {
				if (e.getLocalizedMessage().equals(
						"Connection reset by peer: socket write error")
						|| e.getLocalizedMessage().equals("Socket closed")) {
					// client has been disconnected.
					return;
				} else {
					e.printStackTrace();
				}
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}