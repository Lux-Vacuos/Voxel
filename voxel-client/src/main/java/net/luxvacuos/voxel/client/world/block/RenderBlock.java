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

package net.luxvacuos.voxel.client.world.block;

import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.rendering.world.block.ICustomRenderBlock;
import net.luxvacuos.voxel.client.rendering.world.block.IObjRenderBlock;
import net.luxvacuos.voxel.client.rendering.world.block.IRenderBlock;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.world.block.BlockBase;
import net.luxvacuos.voxel.universal.world.utils.BlockFace;
import net.luxvacuos.voxel.universal.world.utils.Vector8f;

public class RenderBlock extends BlockBase implements IRenderBlock {
	protected final BlockFaceAtlas atlas;

	public RenderBlock(BlockMaterial material, BlockFaceAtlas atlas) {
		super(material);
		this.atlas = atlas;
	}

	public RenderBlock(BlockMaterial material, BoundingBox aabb, BlockFaceAtlas atlas) {
		super(material, aabb);
		this.atlas = atlas;
	}

	@Override
	public Vector8f getTexCoords(BlockFace face) {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords(this.atlas.get(face));
	}

	@Override
	public boolean isTransparent() {
		return this.material.isTransparent();
	}

	@Override
	public boolean isVisible() {
		return this.material.isVisible();
	}

	@Override
	public boolean hasCustomModel() {
		return (this instanceof ICustomRenderBlock);
	}

	@Override
	public boolean hasObjModel() {
		return (this instanceof IObjRenderBlock);
	}

}
