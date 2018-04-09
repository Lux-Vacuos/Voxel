/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.client.world.dimension;

import com.hackhalo2.nbt.tags.TagCompound;

import io.netty.channel.Channel;
import net.luxvacuos.voxel.universal.network.packets.SetBlock;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.block.IBlock;

public class NetworkDimension extends RenderDimension {

	private Channel channel;

	public NetworkDimension(IWorld world, TagCompound data, int id, Channel channel) {
		super(world, data, id);
		this.channel = channel;
	}
	
	@Override
	public boolean setBlockAt(int x, int y, int z, IBlock block) {
		channel.writeAndFlush(new SetBlock(x, y, z, block.getID()));
		return true;
	}
	
	public boolean setBlockAtFromN(int x, int y, int z, IBlock block){
		return super.setBlockAt(x, y, z, block);
	}

}
