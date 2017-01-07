package net.luxvacuos.voxel.client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.network.packets.Message;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			if (msg instanceof Message) {
				Message mg = (Message) msg;
				Logger.log("[" + mg.getSender() + "]: " + mg.getMessage());
			} else if (msg instanceof String) {
				Logger.log("[Server]:" + msg);
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
