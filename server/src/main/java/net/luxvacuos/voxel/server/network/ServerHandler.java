package net.luxvacuos.voxel.server.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.lightengine.universal.ecs.entities.PlayerEntity;
import net.luxvacuos.voxel.server.core.states.MPWorldState;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.network.packets.ClientConnect;
import net.luxvacuos.voxel.universal.network.packets.ClientDisconnect;
import net.luxvacuos.voxel.universal.network.packets.Message;
import net.luxvacuos.voxel.universal.network.packets.SetBlock;
import net.luxvacuos.voxel.universal.world.block.Blocks;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private Map<UUID, PlayerEntity> players;

	private MPWorldState mpWorldState;

	public ServerHandler(MPWorldState state) {
		this.mpWorldState = state;
		players = new HashMap<>();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext context) {
		Channel channel = context.channel();
		channel.writeAndFlush("Welcome to Voxel Server.");
		channels.add(channel);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext context) {
		Channel channel = context.channel();
		channels.remove(channel);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		try {
			if (obj instanceof Message) {
				handleMessage((Message) obj);
			} else if (obj instanceof String) {
				handleString(ctx, (String) obj);
			} else if (obj instanceof SetBlock) {
				handleSetBlock((SetBlock) obj);
			} else if (obj instanceof ClientConnect) {
				handleConnect((ClientConnect) obj);
			} else if (obj instanceof ClientDisconnect) {
				handleDisconnect((ClientDisconnect) obj);
			}
		} finally {
			ReferenceCountUtil.release(obj);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleConnect(ClientConnect con) {
		PlayerEntity p = new PlayerEntity(con.getName(), con.getUuid().toString());
		Components.POSITION.get(p).set(0, 240, 0);
		mpWorldState.getWorld().getActiveDimension().getEntitiesManager().addEntity(p);
		players.put(con.getUuid(), p);
		channels.writeAndFlush(new Message("Server", "Connected " + con.getName()));
		Logger.log("Connected " + con.getName());
	}

	private void handleDisconnect(ClientDisconnect con) {
		mpWorldState.getWorld().getActiveDimension().getEntitiesManager().removeEntity(players.get(con.getUuid()));
		players.remove(con.getUuid());
		channels.writeAndFlush(new Message("Server", "Disconected " + con.getName()));
		Logger.log("Disconected " + con.getName());
	}

	private void handleString(ChannelHandlerContext ctx, String str) {
		Logger.log("Client [" + ctx.channel().id().asLongText() + "]: " + str);
	}

	private void handleMessage(Message msg) {
		Logger.log("[" + msg.getSender() + "]: " + msg.getMessage());
	}

	private void handleSetBlock(SetBlock obj) {
		mpWorldState.getWorld().getActiveDimension().setBlockAt(obj.getX(), obj.getY(), obj.getZ(),
				Blocks.getBlockByID(obj.getBlock()));
		channels.writeAndFlush(obj);
	}

}
