package org.gnet;

import org.gnet.packet.Packet;
import org.gnet.server.ClientModel;
import org.gnet.server.GNetServer;
import org.gnet.server.ServerEventListener;

public class ServerStart {

	/**
	 * Main entry point into the application.
	 * 
	 * @param args
	 *            Arguments passed to the application.
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws InterruptedException {
		// Host to connect to:
		final String host = "127.0.0.1";

		// Port # to connect to the host on:
		final int port = 43594;

		// Setup our server.
		final GNetServer networkedServer = new GNetServer(host, port);

		// Add our event listener to manage events.
		networkedServer.addEventListener(new ServerEventListener() {

			@Override
			protected void clientConnected(final ClientModel client) {
			}

			@Override
			protected void clientDisconnected(final ClientModel client) {
			}

			@Override
			protected void packetReceived(final ClientModel client,
					final Packet packet) {

			}

			@Override
			protected void debugMessage(final String msg) {
			}

			@Override
			protected void errorMessage(final String msg) {
			}
		});

		// Attempt to bind the server.
		networkedServer.bind();

		// Once binded, finally start our server.
		networkedServer.start();

		// Show the ServerMonitor (Developer)
		networkedServer.enableServerMonitor();
	}
}