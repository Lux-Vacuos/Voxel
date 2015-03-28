package voxel.server.core.engine.model.player;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import voxel.client.core.util.Logger;

public class Player {
	private final Channel channel;

	private String username;
	private String password;

	public Player(Channel channel) {
		this.channel = channel;
	}

	public void login(String user, String pass) throws Exception {
		this.username = user;
		this.password = pass;
		playersOnline.add(this);
		Logger.log("Player " + username + " is conected");
	}

	public Channel getChannel() {
		return channel;
	}

	public String getUsername() {
		return username;
	}

	public static List<Player> playersOnline = new ArrayList<Player>();

	public static Player getPlayerbyChannel(Channel channel) {
		for (Player player : playersOnline) {
			if (player.getChannel().equals(channel)) {
				return player;
			}
		}
		return null;
	}
}
