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

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.world.block.BlockBase;

public class BlockTorch extends BlockBase {

	public BlockTorch() {
		transparent = true;
		customModel = true;
	}

	@Override
	public byte getId() {
		return 9;
	}

	@Override
	public BoundingBox getBoundingBox(Vector3f pos) {
		return new BoundingBox(new Vector3(pos.x + 0.3f, pos.y, pos.z + 0.3f),
				new Vector3(pos.x + 0.7f, pos.y + 1f, pos.z + 0.7f));
	}

	@Override
	public void generateCustomModel(Tessellator tess, float x, float y, float z, float globalScale, boolean top,
			boolean bottom, boolean left, boolean right, boolean front, boolean back, float lightTop, float lightBottom,
			float lightLeft, float lightRight, float lightFront, float lightBack) {
		tess.generateCube(x + 0.35f * globalScale, y, z + 0.35f * globalScale, globalScale - 0.7f * globalScale,
				globalScale - 0.3f * globalScale, globalScale - 0.7f * globalScale, true, true, true, true, true, true,
				this, lightTop, lightBottom, lightLeft, lightRight, lightFront, lightBack);
		tess.generateCube(x + 0.3f * globalScale, y + 0.7f * globalScale, z + 0.3f * globalScale,
				globalScale - 0.6f * globalScale, globalScale - 0.7f * globalScale, globalScale - 0.6f * globalScale,
				true, true, true, true, true, true, this, lightTop, lightBottom, lightLeft, lightRight, lightFront,
				lightBack);

	}
}
