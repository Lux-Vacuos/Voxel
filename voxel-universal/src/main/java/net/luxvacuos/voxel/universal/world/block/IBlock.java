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

package net.luxvacuos.voxel.universal.world.block;

import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.BlockNode;

public interface IBlock {

	public String getName();

	public void setPosition(int x, int y, int z);

	public int getBlockX();

	public int getBlockY();

	public int getBlockZ();

	public void setPosition(BlockNode node);

	public BlockNode getPosition();

	public void setMetadata(int metadata);

	public int getMetadata();
	
	public void setChunk(IChunk chunk);

	public IChunk getChunk();

	public IDimension getDimension();

	public boolean hasBlockEntity();

	public IBlockEntity createBlockEntity();

}
