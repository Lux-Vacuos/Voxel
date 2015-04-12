package org.gnet.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.gnet.packet.Packet;

public class GNetClient {

	private final int port;
	private final String host;
	private ClientEventListener clientEventListener;
	private boolean initialized;
	private boolean binded;
	private Socket clientSocket;
	private InetAddress targetHost;
	private Thread clientThread;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	boolean connected;
	private ServerModel serverModel;
	private boolean debugging;

	public GNetClient(final String host, final int port) {
		this.host = host;
		this.port = port;
		debugging = true;

	}

	private void init() {
		debug("Obtaining target host...");
		try {

			// Obtain our target host.
			targetHost = InetAddress.getByName(host);
			debug("Target host obtained! (" + targetHost + ")");

			// Do some internal initializing.
			clientThread = new Thread() {
				@Override
				public void run() {
					clientLoop();
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

	protected void clientLoop() {
		try {
			// Init client object output stream.
			oos = new ObjectOutputStream(clientSocket.getOutputStream());

			// Run initial oos flush. (Prevents hanging)
			oos.flush();

			// Init client object input stream.
			ois = new ObjectInputStream(clientSocket.getInputStream());

			// Init ServerModel.
			serverModel = new ServerModel(this, oos, ois);

			// Mark connected as true.
			connected = true;

			// Notify about clientConnected
			clientEventListener.clientConnected(serverModel);

			// While the client is connected:
			while (connected) {

				// Try to fetch incoming data.
				Object incoming = null;
				try {

					// If the incoming data != null
					if ((incoming = ois.readObject()) != null) {
						if (incoming instanceof Packet) {
							final Packet p = (Packet) incoming;
							if (p.getPacketName()
									.equals("ClientShutdownPacket")) {
								final boolean value = (Boolean) p
										.getEntry("shutdownClient");
								if (value) {
									debug("ClientShutdownPacket received! (shutting down)");
									clientEventListener
											.clientDisconnected(serverModel);
									shutDown();
								}
							}
							clientEventListener.packetReceived(serverModel, p);
							debug("Incoming packet from server: "
									+ p.getPacketName());
						} else {
							debug("Incoming object from server: " + incoming);
						}
					} else {
						// DATA = NULL
						// (No incoming data at the moment ^__^)
						continue;
					}
				} catch (final SocketException e) {
					if (e.getLocalizedMessage().equals("Connection reset")) {
						debug("The server has shutdown, we've been disconnected as a result.");
						connected = false;
						clientEventListener.clientDisconnected(serverModel);
						shutDown();
						continue;
					} else {
						e.printStackTrace();
					}
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
					clientEventListener.clientDisconnected(serverModel);
					shutDown();
					continue;
				} catch (final IOException e) {
					e.printStackTrace();
					clientEventListener.clientDisconnected(serverModel);
					shutDown();
					continue;
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void shutDown() {

		serverModel.sendPacket(new Packet("ShuttingDown", 0));
		try {
			// Attempt to close the client socket.
			if (clientSocket != null) {
				clientSocket.close();
			}
			// Attempt to close the clients streams.
			if (oos != null && ois != null) {
				oos.close();
				ois.close();
			}
		} catch (final SocketException e) {
			if (e.getLocalizedMessage().equals("Socket closed")) {

				// Client has been disconnected.
				return;
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		connected = false;
		binded = false;
	}

	public void bind() {
		if (!initialized) {
			// Pre-Initialize the client.
			init();
		}
		try {
			debug("Attempting to bind to server on address: " + host + " || "
					+ port);
			clientSocket = new Socket(host, port);

			// If binding has completed.
			if (clientSocket.isBound()) {
				debug("Binding completed.");
				binded = true;
				return;
			} else {
				// Binding has failed :oFS
				debug("Binding failed.");
				binded = false;
				return;
			}
		} catch (final ConnectException e) {
			if (e.getLocalizedMessage().equals("Connection refused: connect")) {
				error("Failed to bind, no server on address!");
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
		debug("Attempting to start client...");
		if (!binded) {
			// Something went wrong :o
			error("Cannot start(), binding failed.");
			return;
		}

		// Start the clients thread.
		clientThread.start();

		debug("Client started!");
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		clientThread.stop();
		debug("Client started!");
	}

	public void addEventListener(final ClientEventListener clientEventListener) {
		this.clientEventListener = clientEventListener;
	}

	void debug(final String msg) {
		if (!debugging) {
			// Let the user handle the message.
			if (clientEventListener != null) {
				clientEventListener.debugMessage(msg);
			} else if (clientEventListener == null) {
				System.out.println("GNetClient2 -> " + msg);
			}
			return;
		} else if (debugging) {
			System.out.println("GNetClient -> " + msg);
			return;
		}
	}

	void error(final String msg) {
		if (!debugging) {
			// Let the user handle the message.
			if (clientEventListener != null) {
				clientEventListener.errorMessage(msg);
			} else if (clientEventListener == null) {
				System.err.println("GNetClient2 -> " + msg);
			}
			return;
		} else if (debugging) {
			System.err.println("GNetClient -> " + msg);
			return;
		}
	}

	public boolean isBinded() {
		return binded;
	}

	public boolean isConnected() {
		return connected;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public void setDebugging(final boolean debugging) {
		this.debugging = debugging;
	}

	public boolean isDebugging() {
		return debugging;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
