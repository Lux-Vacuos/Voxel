package voxel.server.core.engine;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import voxel.client.core.util.Logger;
import voxel.server.core.engine.net.PipelineFactory;

public class Server {
	private static final String BIND_ADDRESS = "127.0.0.1";
	private static final int PORT = 25565;

	public Server() {
		try {
			Logger.log("Starting Server on " + BIND_ADDRESS + ":" + PORT);
			Startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Startup() throws Exception {
		Logger.log("Initializing Server");
		ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory());
		serverBootstrap.bind(new InetSocketAddress(BIND_ADDRESS, PORT));
		Logger.log("Server Initialized");
	}

	public static void main(String[] params) {
		new Server();
	}
}
