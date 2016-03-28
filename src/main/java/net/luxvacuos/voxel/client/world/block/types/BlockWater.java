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
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.block.BlocksResources;

public class BlockWater extends BlockBase {

	public BlockWater() {
		transparent = true;
		customModel = true;
		affectedByGravity = true;
		collision = false;
	}

	@Override
	public byte getId() {
		return 7;
	}

	@Override
	public Vector8f texCoordsUp() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Water");
	}

	@Override
	public Vector8f texCoordsDown() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Water");
	}

	@Override
	public Vector8f texCoordsFront() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Water");
	}

	@Override
	public Vector8f texCoordsBack() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Water");
	}

	@Override
	public Vector8f texCoordsRight() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Water");
	}

	@Override
	public Vector8f texCoordsLeft() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Water");
	}

	@Override
	public void generateCustomModel(Tessellator tess, float x, float y, float z, float globalScale, boolean top,
			boolean bottom, boolean left, boolean right, boolean front, boolean back, float lightTop, float lightBottom,
			float lightLeft, float lightRight, float lightFront, float lightBack) {
		if (!top)
			tess.generateCube(x, y, z, globalScale, globalScale, globalScale, top, bottom, left, right, front, back,
					this, lightTop, lightBottom, lightLeft, lightRight, lightFront, lightBack);
		else
			tess.generateCube(x, y, z, globalScale, globalScale - 0.2f * globalScale, globalScale, top, bottom, left,
					right, front, back, this, lightTop, lightBottom, lightLeft, lightRight, lightFront, lightBack);
	}

}
