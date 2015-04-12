package net.guerra24.voxel.client;

import net.guerra24.client.launcher.login.LoginDialog;
import net.guerra24.voxel.client.engine.util.Logger;

import org.gnet.client.ClientEventListener;
import org.gnet.client.GNetClient;
import org.gnet.client.ServerModel;
import org.gnet.packet.Packet;

public class Start {

	private static boolean log = false;

	/**
	 * Main entry point into the application.
	 * 
	 * @param args
	 *            Arguments passed to the application.
	 */
	public static void main(final String[] args) {

		// Host to connect to:
		final String host = "127.0.0.1";

		// Port # to connect to the host on:
		final int port = 43594;

		// Setup our client.
		final GNetClient networkedClient = new GNetClient(host, port);

		// Add our event listener to manage events.
		networkedClient.addEventListener(new ClientEventListener() {

			@Override
			protected void clientConnected(final ServerModel server) {
				Packet loginPacket = new Packet("MockLoginPacket", 2);
				loginPacket.addEntry("username",
						new String(LoginDialog.getUsername()));
				loginPacket.addEntry("pass",
						new String(LoginDialog.getPassword()));
				server.sendPacket(loginPacket);
			}

			@Override
			protected void clientDisconnected(final ServerModel server) {
			}

			@Override
			protected void packetReceived(final ServerModel server,
					final Packet packet) {
				if (packet.getPacketName().equals("Login")) {
					Boolean login = (Boolean) packet.getEntry("login");
					log = login.booleanValue();
					Logger.log("Login Succesfull");
				}
				if (log) {
					Logger.log("Starting Game");
					StartClient.StartGame();
				}
			}

			@Override
			protected void debugMessage(final String msg) {
			}

			@Override
			protected void errorMessage(final String msg) {
			}
		});

		// Attempt to bind the client.
		networkedClient.bind();

		// Once binded, finally start our client.
		networkedClient.start();
	}
}