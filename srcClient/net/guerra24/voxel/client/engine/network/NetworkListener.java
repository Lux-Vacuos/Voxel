package net.guerra24.voxel.client.engine.network;

import net.guerra24.client.launcher.login.LoginDialog;
import net.guerra24.voxel.client.engine.network.packets.Packet.Packet0LoginRequest;
import net.guerra24.voxel.client.engine.network.packets.Packet.Packet1LoginAnswer;
import net.guerra24.voxel.client.engine.util.Logger;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener {
	private Client client;

	public void init(Client client) {
		this.client = client;
	}

	@Override
	public void connected(Connection arg0) {
		Logger.logS(LoginDialog.getUsername() + " has Connected");
		Packet0LoginRequest loginRequest = new Packet0LoginRequest();
		loginRequest.user = LoginDialog.getUsername();
		loginRequest.pass = LoginDialog.getPassword();
		client.sendTCP(loginRequest);
	}

	@Override
	public void disconnected(Connection arg0) {
		Logger.logS(LoginDialog.getUsername() + " has Disconected");
	}

	@Override
	public void received(Connection c, Object o) {
		if (o instanceof Packet1LoginAnswer) {
			boolean answer = ((Packet1LoginAnswer) o).accepted;
			if (answer) {

			} else {
				c.close();
			}
		}
	}

}