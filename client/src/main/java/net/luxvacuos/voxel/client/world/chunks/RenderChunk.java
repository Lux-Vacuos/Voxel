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

package net.luxvacuos.voxel.client.world.chunks;

import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.rendering.world.block.ICustomRenderBlock;
import net.luxvacuos.voxel.client.rendering.world.chunk.IRenderChunk;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.chunk.Chunk;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.BlockFace;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class RenderChunk extends Chunk implements IRenderChunk {

	private Tessellator tess;
	private boolean needsMeshRebuild, isRebuilding;

	public RenderChunk(IDimension dim, ChunkNode node, ChunkData data) {
		super(dim, node, data);
		this.tess = new Tessellator(BlocksResources.getMaterial());
	}

	protected void isRebuilding(boolean flag) {
		this.isRebuilding = flag;
	}

	protected void needsMeshRebuild(boolean flag) {
		this.needsMeshRebuild = flag;
	}

	@Override
	public void update(float delta) {
		if(this.data.needsRebuild()) {
			this.needsMeshRebuild = true;
		}

		super.update(delta);
	}

	@Override
	public void render(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation, int shadowMap) {
		if (this.needsMeshRebuild()) {
			this.tess.begin();
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 256; y++) {
						RenderBlock block = (RenderBlock) this.data.getBlockAt(x, y, z);
						if (!block.isTransparent()) {
							if (!block.hasCustomModel()) {
								this.tess.generateCube(this.getX() * 16 + x, y, this.getZ() * 16 + z,
										1, cullFace(block, BlockFace.UP, x, y, z), cullFace(block, BlockFace.DOWN, x, y, z),
										cullFace(block, BlockFace.EAST, x, y, z), cullFace(block, BlockFace.WEST, x, y, z),
										cullFace(block, BlockFace.NORTH, x, y, z), cullFace(block, BlockFace.SOUTH, x, y, z), block);
							} else {
								((ICustomRenderBlock) block).generateCustomModel(this.tess, this.getX() * 16 + x,
										y, this.getZ() * 16 + z, 1, cullFace(block, BlockFace.UP, x, y, z),
										cullFace(block, BlockFace.DOWN, x, y, z), cullFace(block, BlockFace.EAST, x, y, z),
										cullFace(block, BlockFace.WEST, x, y, z), cullFace(block, BlockFace.NORTH, x, y, z),
										cullFace(block, BlockFace.SOUTH, x, y, z));
							}
						}
						
					}
				}
			}
			
			this.tess.end();
			this.needsMeshRebuild(false);
		}
		this.tess.draw(camera, sunCamera, clientWorldSimulation, shadowMap);
	}

	@Override
	public void renderShadow(Camera sunCamera) {
		this.tess.drawShadow(sunCamera);
	}

	@Override
	public void renderOcclusion(Camera camera) {
		this.tess.drawOcclusion(camera);
	}

	@Override
	public void dispose() {
		super.dispose();
		this.tess.cleanUp();
	}

	@Override
	public boolean needsMeshRebuild() {
		return this.needsMeshRebuild && !this.isRebuilding;
	}

	@Override
	public void markMeshRebuild() {
		this.needsMeshRebuild = true;

	}

	@Override
	public Tessellator getTessellator() {
		return this.tess;
	}
	
	private boolean cullFace(RenderBlock block, BlockFace face, int x, int y, int z) {
		RenderBlock b;
		if(this.isBlockOutside(face, x, y, z)) {
			b = ((RenderBlock) this.data.getBlockAt(x + face.getModX(), y + face.getModY(), z + face.getModZ()));
			if (b.getID() == block.getID())
				return false;
			if (b.isTransparent() || b.hasCustomModel() || b.isFluid())
				return true;
		}
		if(!(face == BlockFace.UP || face == BlockFace.DOWN)) {
			int cx = this.getX() * 16 + x;
			int cz = this.getZ() * 16 + z;
			b = ((RenderBlock) this.dim.getBlockAt(cx + face.getModX(), y + face.getModY(), cz + face.getModZ()));
			
			if(b == null || b.getID() == block.getID()) 
				return false;
			if (b.isTransparent())
				return true;
		}
		return false;
	}
	
	private boolean isBlockOutside(BlockFace face, int x, int y, int z) {
		switch(face) {
		case WEST:
			return (x > 1 && x < 16);
		case DOWN:
			return (y > 1 && y < 256);
		case NORTH:
			return (z > 1 && z < 16);
		case EAST:
			return (x > 0 && x < 15);
		case UP:
			return (y > 0 && y < 255);
		case SOUTH:
			return (z > 0 && z < 15);
		default:
			return false;
		}
	}

}
