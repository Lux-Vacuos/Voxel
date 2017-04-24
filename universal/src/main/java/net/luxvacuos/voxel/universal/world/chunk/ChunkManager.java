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

package net.luxvacuos.voxel.universal.world.chunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.utils.Array;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.resources.IDisposable;
import net.luxvacuos.voxel.universal.tasks.ChunkGenerateTask;
import net.luxvacuos.voxel.universal.tasks.ChunkLoaderTask;
import net.luxvacuos.voxel.universal.tasks.ChunkSaveTask;
import net.luxvacuos.voxel.universal.tasks.ChunkUnloaderTask;
import net.luxvacuos.voxel.universal.world.chunk.generator.FlatChunkGenerator;
import net.luxvacuos.voxel.universal.world.chunk.generator.IChunkGenerator;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class ChunkManager implements IDisposable {
	protected final IDimension dim;
	protected final ExecutorService executor = Executors.newFixedThreadPool(GlobalVariables.chunkmanager_threads);
	protected IChunkGenerator chunkGenerator = new FlatChunkGenerator();

	protected List<ChunkNode> chunkLoadList;
	protected List<Future<IChunk>> chunkGenerateList;
	protected List<Future<IChunk>> chunkUnloadList;
	protected volatile Map<ChunkNode, IChunk> loadedChunks;

	protected final Object loadLock = new Object();
	protected final Object unloadLock = new Object();
	protected final Object saveLock = new Object();

	private Future<Void> saveTask = null;

	public ChunkManager(IDimension dim) {
		this.dim = dim;
		this.chunkGenerateList = Collections.synchronizedList(new ArrayList<>());
		this.chunkLoadList = Collections.synchronizedList(new ArrayList<>());
		this.chunkUnloadList = Collections.synchronizedList(new ArrayList<>());
		this.loadedChunks = new ConcurrentHashMap<>();
	}

	public void setGenerator(IChunkGenerator generator) {
		this.chunkGenerator = generator;
	}

	public final void loadChunk(ChunkNode node) {
		if (!this.loadedChunks.containsKey(node)) {
			synchronized (this.loadLock) {
				this.chunkLoadList.add(node);
				this.loadedChunks.put(node,
						new FutureChunk(this.dim, node, this.executor.submit(new ChunkLoaderTask(this.dim, node))));
			}
		}
	}

	public final void batchLoadChunks(ChunkNode... nodes) {
		synchronized (this.loadLock) {
			for (ChunkNode node : nodes) {
				if (!this.loadedChunks.containsKey(node)) {
					this.chunkLoadList.add(node);
					this.loadedChunks.put(node,
							new FutureChunk(this.dim, node, this.executor.submit(new ChunkLoaderTask(this.dim, node))));
				}
			}
		}
	}

	public final void unloadChunk(ChunkNode node) {
		if (this.loadedChunks.containsKey(node)) {
			synchronized (this.unloadLock) {
				try {
					IChunk chunk = this.loadedChunks.get(node);

					// Make sure that the chunk is fully loaded before trying to
					// unload it
					if (chunk instanceof FutureChunk)
						return;

					this.loadedChunks.remove(node);
					this.chunkUnloadList.add(this.executor.submit(new ChunkUnloaderTask(chunk)));
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		}
	}

	public final boolean isChunkLoaded(ChunkNode node) {
		return this.loadedChunks.containsKey(node);
	}

	public void saveChunks() {
		synchronized (this.saveLock) {
			List<IChunk> trueLoadedChunks = new ArrayList<>();
			for (IChunk chunk : this.loadedChunks.values()) {
				if (chunk instanceof FutureChunk)
					continue;
				trueLoadedChunks.add(chunk);
			}

			this.saveTask = this.executor
					.submit(new ChunkSaveTask(Collections.unmodifiableCollection(trueLoadedChunks)));
		}

	}

	public void update(float delta) {
		// Check if the save task is done
		synchronized (this.saveLock) {
			if (this.saveTask != null && this.saveTask.isDone()) {
				this.saveTask = null;
				Logger.log("Chunks saved");
			}
		}

		// Check if any of the ChunkLoaderTasks completed
		// Lock the Loader Lock to make sure the underlying List does not get
		// modified by anything other than the Iterator
		synchronized (this.loadLock) {
			if (!this.chunkLoadList.isEmpty()) {
				try {
					Iterator<ChunkNode> iterator = this.chunkLoadList.iterator();
					while (iterator.hasNext()) {
						ChunkNode node = iterator.next();
						IChunk chunk = this.loadedChunks.get(node);
						if (chunk instanceof FutureChunk) {
							if (((FutureChunk) chunk).isDone()) {
								iterator.remove();

								if (chunk.getChunkData().shouldGenerate()) {
									this.chunkGenerateList.add(
											this.executor.submit(new ChunkGenerateTask(chunk, this.chunkGenerator)));
									continue;
								}

								IChunk replacement = this.makeChunk(this.dim, chunk.getNode(), chunk.getChunkData());
								this.loadedChunks.put(replacement.getNode(), replacement);
							}
						} else {
							iterator.remove();
						}
					}
				} catch (Exception e) {
					this.executor.shutdownNow();
					throw new RuntimeException(
							"ChunkLoaderTask threw an error during exectuion! Shutting down to try and protect data...",
							e);
				}
			}
		}

		Iterator<Future<IChunk>> iterator;
		Future<IChunk> value;

		synchronized (this.unloadLock) {
			try {
				iterator = this.chunkUnloadList.iterator();
				while (iterator.hasNext()) {
					value = iterator.next();
					if (value.isCancelled()) {
						iterator.remove();
						continue;
					}

					if (value.isDone()) {
						IChunk chunk = value.get();
						iterator.remove();
						chunk.dispose();
					}
				}
			} catch (Exception e) {
				Logger.error(e);
			}
		}

		iterator = this.chunkGenerateList.iterator();
		while (iterator.hasNext()) {
			value = iterator.next();
			if (value.isCancelled()) {
				iterator.remove();
				continue;
			}

			if (value.isDone()) {
				try {
					IChunk chunk = value.get();
					iterator.remove();
					IChunk replacement = this.makeChunk(this.dim, chunk.getNode(), chunk.getChunkData());
					this.loadedChunks.put(replacement.getNode(), replacement);
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		}

		for (IChunk chunk : loadedChunks.values()) {
			if (chunk instanceof FutureChunk)
				if (!((FutureChunk) chunk).isFutureDone())
					continue;

			chunk.update(delta);
		}

	}

	protected Chunk makeChunk(IDimension dim, ChunkNode node, ChunkData data) {
		return new Chunk(dim, node, data);
	}

	public final Collection<IChunk> getLoadedChunks() {
		List<IChunk> collection = new ArrayList<>(this.loadedChunks.values());
		return Collections.unmodifiableCollection(collection);
	}

	public IChunk getChunkAt(ChunkNode node) {
		if (this.loadedChunks.containsKey(node))
			return this.loadedChunks.get(node);
		else
			return null;

	}

	@Override
	public void dispose() {
		try {
			Array<ChunkNode> toRemove = new Array<>();
			if (!this.loadedChunks.isEmpty()) {
				Logger.log("Saving Chunks...");
				this.saveChunks();
				for (IChunk chunk : this.loadedChunks.values()) {
					toRemove.add(chunk.getNode());
				}
			}
			
			for (ChunkNode node : toRemove) {
				this.unloadChunk(node);
			}
			this.executor.shutdown();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

}
