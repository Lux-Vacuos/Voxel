package org.gnet;

import net.guerra24.client.launcher.login.Login;
import net.guerra24.voxel.client.engine.util.Logger;

import org.gnet.packet.Packet;
import org.gnet.server.ClientModel;
import org.gnet.server.GNetServer;
import org.gnet.server.ServerEventListener;

public class ServerStart {

	public static String user;
	public static String pass;

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
				Packet loginPacket = new Packet("Login", 1);
				loginPacket.addEntry("login", new Boolean(true));
				client.sendPacket(loginPacket);
			}

			@Override
			protected void clientDisconnected(final ClientModel client) {
			}

			@Override
			protected void packetReceived(final ClientModel client,
					final Packet packet) {
				if (packet.getPacketName().equals("MockLoginPacket")) {
					user = (String) packet.getEntry("username");
					pass = (String) packet.getEntry("pass");
					if (Login.authenticate(user, pass)) {
						Logger.log("Login Succesfull");
					}
				}
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