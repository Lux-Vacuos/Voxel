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

public class BlockBase implements IBlock {

	private String name;
	private BlockNode node;
	private int metadata;
	private IChunk chunk;

	public BlockBase(BlockNode node, String name) {
		this.node = node;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setPosition(int x, int y, int z) {
		this.node.setX(x);
		this.node.setY(y);
		this.node.setZ(z);
	}

	@Override
	public int getBlockX() {
		return node.getX();
	}

	@Override
	public int getBlockY() {
		return node.getY();
	}

	@Override
	public int getBlockZ() {
		return node.getZ();
	}

	@Override
	public void setPosition(BlockNode node) {
		this.node = node;
	}

	@Override
	public BlockNode getPosition() {
		return node;
	}

	@Override
	public void setMetadata(int metadata) {
		this.metadata = metadata;
	}

	@Override
	public int getMetadata() {
		return metadata;
	}

	@Override
	public void setChunk(IChunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public IChunk getChunk() {
		return chunk;
	}

	@Override
	public IDimension getDimension() {
		if (this.chunk != null)
			return this.chunk.getDimension();
		return null;
	}

	@Override
	public boolean hasBlockEntity() {
		return false;
	}

	@Override
	public IBlockEntity createBlockEntity() {
		return null;
	}

}
