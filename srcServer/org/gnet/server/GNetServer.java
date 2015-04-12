package org.gnet.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.gnet.packet.ClientShutdownPacket;
import org.gnet.packet.Packet;
import org.gnet.util.UIDGenerator;

public class GNetServer {

	// Server related stuff
	private final String host;
	private final int port;
	private ServerSocket serverSocket;
	private boolean initialized;
	private Thread clientConnectionThread;
	protected boolean connectNewClients;
	private boolean binded;
	private boolean debugging;
	int onlineClients;
	private ArrayList<ClientModel> clients;
	private ServerEventListener serverEventListener;
	private UIDGenerator generator;
	private ServerMonitor serverMonitor;
	private InetAddress targetHost;
	private boolean serverMonitorVisible;

	// ServerMonitor stuff.
	public int sentPackets;
	public int recievedPackets;
	public boolean tcpBound;
	public boolean serverRunning;

	public GNetServer(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	public void shutDownServer() {
		if (clients != null) {
			for (int i = 0; i < clients.size(); i++) {
				clients.get(i).sendPacket(new ClientShutdownPacket());
			}
			connectNewClients = false;
			binded = false;
			initialized = false;
			serverRunning = false;
		}
	}

	private void init() {
		try {
			// Obtain our target host.
			debug("Obtaining target host...");
			targetHost = InetAddress.getByName(host);
			debug("Target host obtained! (" + targetHost + ")");

			// Do some internal initializing.
			clients = new ArrayList<ClientModel>();
			generator = new UIDGenerator(new String("9999"));

			// Initialize our client accepting thread.
			clientConnectionThread = new Thread() {
				@Override
				public void run() {

					while (connectNewClients) {
						connectNewClient();
					}
				}
			};
			// Mark initialized as true.
			initialized = true;
		} catch (final UnknownHostException e) {
			error("Failed to find target host!");
			e.printStackTrace();
			initialized = false;
			return;
		}

	}

	public void bind() {
		if (!initialized) {
			// Pre-Initialize the server.
			init();
		}
		try {
			debug("Attempting to bind to address: " + host + " || " + port);

			serverSocket = new ServerSocket(port, Integer.MAX_VALUE,
					InetAddress.getByName(host));

			// If binding was completed.
			if (serverSocket.isBound()) {
				tcpBound = true;
				debug("Binding completed.");
				binded = true;
				return;
			} else {
				// Binding has failed :o
				debug("Binding failed.");
				binded = false;
				return;
			}
		} catch (final BindException e) {
			if (e.getLocalizedMessage().equals(
					"Address already in use: JVM_Bind")) {
				error("Failed to bind, address already in use!");
				return;
			} else {
				e.printStackTrace();
				return;
			}
		} catch (final UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (final IOException e) {
			e.printStackTrace();
			return;
		}

	}

	public void start() {
		debug("Attempting to start server...");
		if (!binded) {
			// Something went wrong :o
			error("Cannot start(), binding failed.");
			return;
		}

		// Enable loop execution.
		connectNewClients = true;

		// if (!(serverMonitor == null)) {
		//
		// // Start the ServerMonitors rendering/updating thread.
		// serverMonitor.start();
		//
		// // Show the ServerMonitor.
		// serverMonitor.show();
		// }

		// Start our client connection thread.
		clientConnectionThread.start();

		debug("Server started!");
	}

	protected void connectNewClient() {
		try {
			// Try to accept a new client.
			final Socket client = serverSocket.accept();

			// Once request handled create our ClientModel instance.
			final ClientModel clientModel = new ClientModel(this, client);

			// Generate a UID for the client.
			clientModel.uuid = generator.generateUID();

			// Start the clients thread.
			new Thread(clientModel).start();

			// Client is now connected, add to list if not on.
			if (!clients.contains(clientModel)) {
				clients.add(clientModel);
			}
			// client count increased.
			onlineClients++;

			// debug some info.
			debug("A client [" + clientModel.uuid
					+ "] has connected! (online: " + onlineClients + ")");

			// notify user about clientConnected.
			serverEventListener.clientConnected(clientModel);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToAll(final Packet packet) {
		for (final ClientModel p : clients) {
			if (p != null) {
				p.sendPacket(packet);
			}
		}
	}

	public void sendToAllBut(final ClientModel who2NotSend2, final Packet packet) {
		for (final ClientModel p : clients) {
			if (p != null && !p.equals(who2NotSend2)) {
				p.sendPacket(packet);
			}
		}
	}

	public void addEventListener(final ServerEventListener serverEventListener) {
		this.serverEventListener = serverEventListener;
	}

	public void enableServerMonitor() {
		serverMonitor = new ServerMonitor(this);

		// Start the ServerMonitors rendering/updating thread.
		serverMonitor.start();

		// Show the ServerMonitor.
		serverMonitor.show();
		serverMonitorVisible = true;

	}

	public void disableServerMonitor() {
		if (serverMonitor != null) {
			serverMonitor.dispose();
			serverMonitorVisible = false;
		}
	}

	void debug(final String msg) {
		if (!debugging) {
			// Let the user handle the message.
			if (serverEventListener != null) {
				serverEventListener.debugMessage(msg);
				return;
			}
		} else if (debugging) {
			System.out.println("GNetServer -> " + msg);
		}
	}

	void error(final String msg) {
		if (!debugging) {
			// Let the user handle the message.
			if (serverEventListener != null) {
				serverEventListener.errorMessage(msg);
				return;
			}
		} else if (debugging) {
			System.err.println("GNetServer -> " + msg);
		}
	}

	public void setDebugging(final boolean debugging) {
		this.debugging = debugging;
	}

	public boolean isDebugging() {
		return binded;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getOnlineClients() {
		return onlineClients;
	}

	public ArrayList<ClientModel> getClients() {
		return clients;
	}

	public boolean isBindingComplete() {
		return binded;
	}

	public boolean isBinded() {
		return binded;
	}

	public boolean isConnectNewClients() {
		return connectNewClients;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public boolean isServerRunning() {
		return serverRunning;
	}

	public boolean isTcpBound() {
		return tcpBound;
	}

	public int getRecievedPackets() {
		return recievedPackets;
	}

	public int getSentPackets() {
		return sentPackets;
	}

	public ServerMonitor getServerMonitor() {
		return serverMonitor;
	}

	ServerEventListener getServerEventListener() {
		return serverEventListener;
	}

	public boolean isServerMonitorVisible() {
		return serverMonitorVisible;
	}

	void setServerMonitorVisible(final boolean serverMonitorVisible) {
		this.serverMonitorVisible = serverMonitorVisible;
	}

}
