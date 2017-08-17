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

package net.luxvacuos.voxel.client.world.block.types;

import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.rendering.world.block.ICustomRenderBlock;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.universal.material.BlockMaterial;

public class GlassPaneBlock extends RenderBlock implements ICustomRenderBlock {

	public GlassPaneBlock(BlockMaterial material, BlockFaceAtlas atlas) {
		super(material, atlas);
	}

	@Override
	public void generateCustomModel(Tessellator tess, double x, double y, double z, float globalScale, boolean top,
			boolean bottom, boolean left, boolean right, boolean front, boolean back) {
		if (!left && right)
			tess.generateCube(x + 0.4f * globalScale, y, z + 0.4f * globalScale, 0.6f * globalScale, globalScale,
					0.2f * globalScale, top, bottom, left, right, true, true, this);
		if (left && !right)
			tess.generateCube(x, y, z + 0.4f * globalScale, 0.6f * globalScale, globalScale, 0.2f * globalScale, top,
					bottom, left, right, true, true, this);
		if (!left && !right)
			tess.generateCube(x, y, z + 0.4f * globalScale, globalScale, globalScale, 0.2f * globalScale, top, bottom,
					left, right, true, true, this);
		if (!front && back)
			tess.generateCube(x + 0.4f * globalScale, y, z, 0.2f * globalScale, globalScale, 0.6f * globalScale, top,
					bottom, true, true, front, back, this);
		if (front && !back)
			tess.generateCube(x + 0.4f * globalScale, y, z + 0.4f * globalScale, 0.2f * globalScale, globalScale,
					0.6f * globalScale, top, bottom, true, true, front, back, this);
		if (!front && !back)
			tess.generateCube(x + 0.4f * globalScale, y, z, 0.2f * globalScale, globalScale, globalScale, top, bottom,
					true, true, front, back, this);
		if (right && left && front && back)
			tess.generateCube(x + 0.4f * globalScale, y, z + 0.4f * globalScale, 0.2f * globalScale, globalScale,
					0.2f * globalScale, top, bottom, true, true, front, back, this);
	}

}
