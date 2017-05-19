package net.luxvacuos.voxel.client.network;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.MPWorldState;
import net.luxvacuos.voxel.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.client.ui.menus.MainMenu;
import net.luxvacuos.voxel.client.world.dimension.NetworkDimension;
import net.luxvacuos.voxel.universal.network.packets.Disconnect;
import net.luxvacuos.voxel.universal.network.packets.Message;
import net.luxvacuos.voxel.universal.network.packets.SetBlock;
import net.luxvacuos.voxel.universal.network.packets.Time;
import net.luxvacuos.voxel.universal.world.block.Blocks;

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
			} else if (obj instanceof SetBlock) {
				handleSetBlock((SetBlock) obj);
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
		float borderSize = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		float titleBarHeight = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
		int height = (int) REGISTRY.getRegistryItem("/Voxel/Display/height");
		RootComponentWindow mainMenu = new MainMenu(borderSize + 10, height - titleBarHeight - 10,
				(int) REGISTRY.getRegistryItem("/Voxel/Display/width") - borderSize * 2f - 20,
				height - titleBarHeight - borderSize - 20);
		GraphicalSubsystem.getWindowManager().addWindow(mainMenu);
		ClientVariables.exitWorld = true;
	}

	private void handleSetBlock(SetBlock obj) {
		NetworkDimension dim = (NetworkDimension) state.getWorld().getActiveDimension();
		dim.setBlockAtFromN(obj.getX(), obj.getY(), obj.getZ(), Blocks.getBlockByID(obj.getBlock()));
	}
}
