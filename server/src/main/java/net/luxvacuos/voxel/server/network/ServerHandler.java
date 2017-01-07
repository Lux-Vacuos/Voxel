package net.luxvacuos.voxel.server.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.network.packets.Message;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void handlerAdded(ChannelHandlerContext context) {
		Channel channel = context.channel();
		channels.add(channel);
		Logger.log("Connected:" + channel.remoteAddress().toString());
		Logger.log("There are currently " + channels.size() + " clients connected.");
		channel.writeAndFlush("Welcome to Voxel Server.");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext context) {
		Channel channel = context.channel();
		channels.remove(channel);
		Logger.log("Disconected: " + channel.remoteAddress().toString());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg instanceof Message) {

			} else if (msg instanceof String) {
				Logger.log("Client [" + ctx.channel().remoteAddress() + "]: " + msg);
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
