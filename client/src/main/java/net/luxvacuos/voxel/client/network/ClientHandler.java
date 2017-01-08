package net.luxvacuos.voxel.client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.MPWorldState;
import net.luxvacuos.voxel.universal.network.packets.Disconnect;
import net.luxvacuos.voxel.universal.network.packets.Message;
import net.luxvacuos.voxel.universal.network.packets.Time;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private MPWorldState state;

	public ClientHandler(MPWorldState state) {
		this.state = state;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) {
		try {
			if (obj instanceof Message) {
				handleMessage((Message) obj);
			} else if (obj instanceof String) {
				handleString((String) obj);
			} else if (obj instanceof Time) {
				handleTime((Time) obj);
			} else if (obj instanceof Disconnect) {
				handleDisconnect((Disconnect) obj);
			}
		} finally {
			ReferenceCountUtil.release(obj);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleString(String str) {
		Logger.log("[Server]: " + str);
	}

	private void handleMessage(Message msg) {
		Logger.log("[" + msg.getSender() + "]: " + msg.getMessage());
	}

	private void handleTime(Time time) {
		state.getWorldSimulation().setTime(time.getTime());
	}

	private void handleDisconnect(Disconnect disconnect) {
		ClientVariables.exitWorld = true;
	}
}
