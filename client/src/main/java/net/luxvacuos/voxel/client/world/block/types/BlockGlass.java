/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.world.block.types;

import net.luxvacuos.igl.vector.Vector8f;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.block.BlocksResources;

public class BlockGlass extends BlockBase {

	public BlockGlass() {
		transparent = true;
	}

	@Override
	public byte getId() {
		return 8;
	}

	@Override
	public Vector8f texCoordsUp() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Glass");
	}

	@Override
	public Vector8f texCoordsDown() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Glass");
	}

	@Override
	public Vector8f texCoordsFront() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Glass");
	}

	@Override
	public Vector8f texCoordsBack() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Glass");
	}

	@Override
	public Vector8f texCoordsRight() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Glass");
	}

	@Override
	public Vector8f texCoordsLeft() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Glass");
	}

}
