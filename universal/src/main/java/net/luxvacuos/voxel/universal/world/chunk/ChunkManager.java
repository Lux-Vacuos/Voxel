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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

	protected final Lock loadLock = new ReentrantLock();
	protected final Lock unloadLock = new ReentrantLock();
	protected final ReadWriteLock chunkLock = new ReentrantReadWriteLock();

	private Future<Void> saveTask = null;

	public ChunkManager(IDimension dim) {
		this.dim = dim;
		this.chunkGenerateList = new ArrayList<>();
		this.chunkLoadList = new ArrayList<>();
		this.chunkUnloadList = new ArrayList<>();
		this.loadedChunks = new HashMap<>();
	}

	public void setGenerator(IChunkGenerator generator) {
		this.chunkGenerator = generator;
	}

	public final void loadChunk(ChunkNode node) {
		this.chunkLock.readLock().lock();
		try {
			if (!this.loadedChunks.containsKey(node)) {
				this.loadLock.lock();
				try {
					this.chunkLoadList.add(node);
					this.loadedChunks.put(node, new FutureChunk(this.dim, node, this.executor.submit(new ChunkLoaderTask(this.dim, node))));
				} catch (Exception e) {
					Logger.error(e);
				} finally {
					this.loadLock.unlock();
				}
			}
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			this.chunkLock.readLock().unlock();
		}
	}

	public final void batchLoadChunks(ChunkNode... nodes) {
		this.loadLock.lock();
		this.chunkLock.readLock().lock();
		try {
			for (ChunkNode node : nodes) {
				if (!this.loadedChunks.containsKey(node)) {
					this.chunkLoadList.add(node);
					this.loadedChunks.put(node, new FutureChunk(this.dim, node, this.executor.submit(new ChunkLoaderTask(this.dim, node))));
				}
			}
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			this.chunkLock.readLock().unlock();
			this.loadLock.unlock();
		}
	}

	public final void unloadChunk(ChunkNode node) {
		this.chunkLock.readLock().lock();
		try {
			if (this.loadedChunks.containsKey(node)) {
				IChunk chunk = this.loadedChunks.get(node);

				// Need to release the Read Lock to upgrade to a Write Lock
				this.chunkLock.readLock().unlock();
				this.chunkLock.writeLock().lock();
				try {
					this.loadedChunks.remove(node);
					this.chunkUnloadList.add(this.executor.submit(new ChunkUnloaderTask(chunk)));
					this.chunkLock.readLock().lock(); // Downgrade the Write
					// Lock by acquiring the
					// Read Lock before
					// releasing the Write
					// Lock
				} catch (Exception e) {
					Logger.error(e);
				} finally {
					this.chunkLock.writeLock().unlock(); // Remove Write access,
					// Hold Read access
				}
			}
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			this.chunkLock.readLock().unlock(); // Release Read access
		}
	}

	public final boolean isChunkLoaded(ChunkNode node) {
		this.chunkLock.readLock().lock();
		try {
			return this.loadedChunks.containsKey(node);
		} catch (Exception e) {
			Logger.error(e);
			return false;
		} finally {
			this.chunkLock.readLock().unlock();
		}
	}

	public void saveChunks() {
		this.chunkLock.readLock().lock();
		try {
			this.saveTask = this.executor
					.submit(new ChunkSaveTask(Collections.unmodifiableCollection(this.loadedChunks.values())));
		} catch (Exception e) {

		} finally {
			this.chunkLock.readLock().unlock();
		}

	}

	public void update(float delta) {
		// Check if the save task is done
		try {
			if (this.saveTask != null && this.saveTask.isDone()) {
				this.saveTask = null;
				Logger.log("Chunks saved");
			}
		} catch (Exception e) { // XXX: this needs to be done better, but for
			// debugging, fail fast works for now
			Logger.error("ChunkSaveTask threw an error during exectuion! Shutting down to try and protect data...");
			this.executor.shutdownNow();
			throw new RuntimeException("ChunkLoaderTask threw an error during exectuion! Shutting down to try and protect data...", e);
		}

		// Check if any of the ChunkLoaderTasks completed
		// Lock the Loader Lock to make sure the underlying List does not get
		// modified by anything other than the Iterator
		this.loadLock.lock();
		if (!this.chunkLoadList.isEmpty()) {
			try {
				Iterator<ChunkNode> iterator = this.chunkLoadList.iterator();
				while (iterator.hasNext()) {
					ChunkNode node = iterator.next();
					IChunk chunk = this.loadedChunks.get(node);
					if(chunk instanceof FutureChunk) {
						if(((FutureChunk)chunk).isDone()) {
							iterator.remove();
							
							if(chunk.getChunkData().shouldGenerate()) {
								this.chunkGenerateList
								.add(this.executor.submit(new ChunkGenerateTask(chunk, this.chunkGenerator)));
								continue;
							}
							
							IChunk replacement = this.makeChunk(this.dim, chunk.getNode(), chunk.getChunkData());
							this.chunkLock.writeLock().lock(); // Lock the Chunk
							// Writer lock
							try {
								this.loadedChunks.put(replacement.getNode(), replacement);
							} catch (Exception e) {
								//TODO: Add Exception handling
							} finally {
								this.chunkLock.writeLock().unlock();
							}
						}
					} else {
						iterator.remove();
					}
				}
			} catch (Exception e) { // XXX: this needs to be done better, but
				// for debugging, fail fast works for now
				Logger.error(
						"ChunkLoaderTask threw an error during exectuion! Shutting down to try and protect data...");
				this.executor.shutdownNow();
				throw new RuntimeException("ChunkLoaderTask threw an error during exectuion! Shutting down to try and protect data...", e);
			} finally {
				this.loadLock.unlock();
			}
		}

		Iterator<Future<IChunk>> iterator;
		Future<IChunk> value;

		this.unloadLock.lock();
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
		} finally {
			this.unloadLock.unlock();
		}

		iterator = this.chunkGenerateList.iterator();
		this.chunkLock.writeLock().lock();
		try {
			while (iterator.hasNext()) {
				value = iterator.next();
				if (value.isCancelled()) {
					iterator.remove();
					continue;
				}

				if (value.isDone()) {
					IChunk chunk = value.get();
					iterator.remove();
					IChunk replacement = this.makeChunk(this.dim, chunk.getNode(), chunk.getChunkData());
					this.loadedChunks.put(replacement.getNode(), replacement);
				}
			}
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			this.chunkLock.writeLock().unlock();
		}

		this.chunkLock.readLock().lock();;
		try {
			for (IChunk chunk : loadedChunks.values()) {
				if(chunk instanceof FutureChunk)
					if(!((FutureChunk)chunk).isDone())
						continue;
				
				chunk.update(delta);
			}
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			this.chunkLock.readLock().unlock();;
		}

	}

	protected Chunk makeChunk(IDimension dim, ChunkNode node, ChunkData data) {
		return new Chunk(dim, node, data);
	}

	public final Collection<IChunk> getLoadedChunks() {
		this.chunkLock.readLock().lock();
		try {
			List<IChunk> collection = new ArrayList<>(this.loadedChunks.values());
			return Collections.unmodifiableCollection(collection);
		} catch (Exception e) {
			Logger.error(e);
			return null;
		} finally {
			this.chunkLock.readLock().unlock();
		}
	}
	
	public IChunk getChunkAt(ChunkNode node){
		this.chunkLock.readLock().lock();
		try {
			if(this.loadedChunks.containsKey(node)) {
				return this.loadedChunks.get(node);
			} else {
				this.loadLock.lock();
				try {
					this.chunkLoadList.add(node);
					this.loadedChunks.put(node, new FutureChunk(this.dim, node, this.executor.submit(new ChunkLoaderTask(this.dim, node))));
					return this.loadedChunks.get(node);
				} catch (Exception e) {
					Logger.error(e);
					return null;
				} finally {
					this.loadLock.unlock();
				}
			}
		} catch (Exception e) {
			Logger.error(e);
			return null;
		} finally {
			this.chunkLock.readLock().unlock();
		}
	}

	@Override
	public void dispose() {
		this.chunkLock.readLock().lock();
		try {
			if(!this.loadedChunks.isEmpty()) {
				this.saveChunks();
			}
			
			this.executor.shutdown();
			this.executor.awaitTermination(30, TimeUnit.SECONDS);
		} catch (Exception e) {

		} finally {
			this.chunkLock.readLock().unlock();
		}
	}

}
