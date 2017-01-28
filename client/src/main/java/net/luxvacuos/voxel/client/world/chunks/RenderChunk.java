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
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.rendering.api.opengl.ShadowFBO;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.rendering.world.chunk.IRenderChunk;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.universal.world.chunk.Chunk;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
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
		if (this.data.needsRebuild()) {
			this.needsMeshRebuild = true;
		}

		super.update(delta);
	}

	@Override
	public void render(CameraEntity camera, CameraEntity sunCamera, ClientWorldSimulation clientWorldSimulation, ShadowFBO shadow) {
		this.tess.draw(camera, sunCamera, clientWorldSimulation, shadow);
	}

	@Override
	public void renderShadow(CameraEntity sunCamera) {
		this.tess.drawShadow(sunCamera);
	}

	@Override
	public void renderOcclusion(CameraEntity camera) {
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

}
