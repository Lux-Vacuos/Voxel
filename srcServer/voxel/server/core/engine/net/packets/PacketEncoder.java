package voxel.server.core.engine.net.packets;

import org.jboss.netty.buffer.ChannelBuffer;

import voxel.client.core.util.Logger;
import voxel.server.core.engine.model.player.Player;

public class PacketEncoder {
	private Player player;

	public PacketEncoder(Player player) {
		this.player = player;
	}

	public void send(ChannelBuffer buffer) {
		if (player.getChannel() == null || !player.getChannel().isConnected()) {
			Logger.error("Channel null || Not conected");
			return;
		} else {
			player.getChannel().write(buffer);
		}
	}
}
