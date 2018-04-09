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

package net.luxvacuos.voxel.client.world.chunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.rendering.world.chunk.IRenderChunk;
import net.luxvacuos.voxel.client.tasks.MeshGenerateTask;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.chunk.ChunkManager;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class ClientChunkManager extends ChunkManager {

	protected final ExecutorService mesher = Executors.newSingleThreadExecutor();
	protected final Object meshLock = new Object();
	protected List<Future<IRenderChunk>> chunkMeshList;

	public ClientChunkManager(IDimension dim) {
		super(dim);
		this.chunkMeshList = Collections.synchronizedList(new ArrayList<>());
	}

	@Override
	protected RenderChunk makeChunk(IDimension dim, ChunkNode node, ChunkData data) {
		RenderChunk chunk = new RenderChunk(dim, node, data);
		chunk.markMeshRebuild();
		return chunk;
	}

	public final void generateChunkMesh(RenderChunk chunk) {
		synchronized (this.meshLock) {
			chunk.isRebuilding(true);
			this.chunkMeshList.add(this.mesher.submit(new MeshGenerateTask(chunk)));
		}
	}

	@Override
	public void update(float delta) {
		synchronized (this.meshLock) {
			try {
				Iterator<Future<IRenderChunk>> meshed = this.chunkMeshList.iterator();
				Future<IRenderChunk> value;
				while (meshed.hasNext()) {
					value = meshed.next();

					if (value.isCancelled()) {
						meshed.remove();
						continue;
					}

					if (value.isDone()) {
						RenderChunk chunk = (RenderChunk) value.get();
						meshed.remove();
						chunk.isRebuilding(false);
						chunk.needsMeshRebuild(false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.update(delta);
	}

	@Override
	public void dispose() {
		try {
			this.mesher.shutdown();
			this.mesher.awaitTermination(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			Logger.error(e);
		}
		super.dispose();
	}

}