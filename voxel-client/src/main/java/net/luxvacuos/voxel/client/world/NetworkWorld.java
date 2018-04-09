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

package net.luxvacuos.voxel.client.world;

import com.hackhalo2.nbt.tags.TagCompound;

import io.netty.channel.Channel;
import net.luxvacuos.voxel.client.world.dimension.NetworkDimension;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class NetworkWorld extends RenderWorld {

	private Channel channel;
	
	public NetworkWorld(String name, Channel channel) {
		super(name);
		this.channel = channel;
	}
	
	@Override
	protected IDimension createDimension(int id, TagCompound data) {
		return new NetworkDimension(this, data, id, channel);
	}

}
