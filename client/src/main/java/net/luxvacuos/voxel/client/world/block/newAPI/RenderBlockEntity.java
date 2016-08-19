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

package net.luxvacuos.voxel.client.world.block.newAPI;

import net.luxvacuos.igl.vector.Vector8f;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.EntityShader;
import net.luxvacuos.voxel.client.rendering.world.block.IRenderBlockEntity;
import net.luxvacuos.voxel.universal.world.block.BlockEntity;
import net.luxvacuos.voxel.universal.world.utils.BlockFace;

public class RenderBlockEntity extends BlockEntity implements IRenderBlockEntity {

	@Override
	public Vector8f getTexCoords(BlockFace face) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTransparent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCustomModel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasObjModel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(EntityShader shader) {
		// TODO Auto-generated method stub

	}

}
