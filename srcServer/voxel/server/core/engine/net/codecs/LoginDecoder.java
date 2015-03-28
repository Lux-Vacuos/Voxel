package voxel.server.core.engine.net.codecs;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import voxel.client.core.util.Logger;
import voxel.server.core.engine.model.player.Player;

public class LoginDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		ChannelBufferInputStream in = new ChannelBufferInputStream(buffer);
		String username = in.readUTF();
		String password = in.readUTF();

		Logger.log("Username: " + username + " " + "Password: " + password);

		channel.getPipeline().remove("decoder");
		channel.getPipeline().addFirst("decoder", new PacketDecoder());
		
		Player player = new Player((java.nio.channels.Channel) channel);
		player.login(username, password);
		return null;
	}
}
