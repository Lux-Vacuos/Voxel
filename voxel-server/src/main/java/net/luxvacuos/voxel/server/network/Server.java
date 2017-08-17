/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.server.core.states.MPWorldState;
import net.luxvacuos.voxel.universal.network.AbstractNettyNetworkHandler;

public class Server extends AbstractNettyNetworkHandler {

	private EventLoopGroup bossGroup;
	private int port;

	public Server(int port) {
		this.port = port;
	}

	public void run(MPWorldState state) {
		Logger.log("Starting Netty Server");
		bossGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel channel) throws Exception {
							ChannelPipeline pipeline = channel.pipeline();
							pipeline.addLast("decoder", new ObjectDecoder(
									ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())));
							pipeline.addLast("encoder", new ObjectEncoder());
							pipeline.addLast("handler", new ServerHandler(state));
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			future = b.bind(port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void end() {
		workGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		try {
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
