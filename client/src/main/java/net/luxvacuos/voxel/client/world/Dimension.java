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

package net.luxvacuos.voxel.client.world;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_QUERY_RESULT;
import static org.lwjgl.opengl.GL15.glGetQueryObjectui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Sync;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.ParticlePoint;
import net.luxvacuos.voxel.client.resources.models.ParticleSystem;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.chunks.Chunk;
import net.luxvacuos.voxel.client.world.chunks.ChunkGenerator;
import net.luxvacuos.voxel.client.world.chunks.ChunkKey;
import net.luxvacuos.voxel.client.world.chunks.ChunkNode;
import net.luxvacuos.voxel.client.world.chunks.LightNodeAdd;
import net.luxvacuos.voxel.client.world.chunks.LightNodeRemoval;
import net.luxvacuos.voxel.universal.core.exception.LoadChunkException;
import net.luxvacuos.voxel.universal.core.exception.SaveChunkException;

public abstract class Dimension {

	protected int chunkDim;
	protected ConcurrentMap<ChunkKey, Chunk> chunks;
	protected SimplexNoise noise;
	protected String name;
	protected int seedi;
	protected int playerCX;
	protected int playerCY;
	protected int playerCZ;
	protected DimensionData data;
	protected ChunkGenerator chunkGenerator;
	protected Queue<LightNodeAdd> lightNodeAdds;
	protected Queue<LightNodeRemoval> lightNodeRemovals;

	protected Queue<ChunkNode> addQueue;
	protected Queue<ChunkNode> removeQueue;

	protected ParticleSystem particleSystem;
	protected int renderedChunks = 0;
	protected int loadedChunks = 0;
	protected Engine physicsEngine;
	protected PhysicsSystem physicsSystem;
	protected Thread processThread;
	protected boolean running = true;
	protected Sync sync;

	public Dimension(String name, Random seed, int chunkDim, GameResources gm) {
		this.name = name;
		this.chunkDim = chunkDim;
		data = new DimensionData();
		data.addObject("Seed", seed.nextInt());
		init(gm);
	}

	protected void init(GameResources gm) {
		File filec = new File(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim);
		if (!filec.exists())
			filec.mkdirs();
		if (existDimFile())
			load();
		particleSystem = new ParticleSystem(gm.getTorchTexture(), 2, 1, -0.01f, 4, 0.5f);
		particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		particleSystem.setLifeError(0.8f);
		particleSystem.setScaleError(0.2f);
		particleSystem.setSpeedError(0.2f);
		seedi = (int) data.getObject("Seed");
		noise = new SimplexNoise(256, 0.15f, seedi);
		lightNodeAdds = new LinkedList<>();
		lightNodeRemovals = new LinkedList<>();
		chunks = new ConcurrentHashMap<>();
		chunkGenerator = new ChunkGenerator();
		physicsEngine = new Engine();
		physicsSystem = new PhysicsSystem(this);
		physicsEngine.addSystem(physicsSystem);
		addQueue = new ConcurrentLinkedQueue<>();
		removeQueue = new ConcurrentLinkedQueue<>();
		sync = new Sync();
		processThread = new Thread(() -> {
			while (running) {
				while (!removeQueue.isEmpty()) {
					ChunkNode node = removeQueue.poll();
					Chunk chnk = getChunk(node.cx, node.cy, node.cz);
					saveChunk(chnk);
					removeChunk(chnk);
				}

				while (!addQueue.isEmpty()) {
					ChunkNode node = addQueue.poll();
					Chunk chn = getChunk(node.cx, node.cy, node.cz);
					if (chn == null)
						continue;
					chn.init(this);
				}
				sync.sync(30);
			}
		});
		processThread.setDaemon(true);
		processThread.start();
	}

	protected void load() {
		Input input;
		try {
			input = new Input(new FileInputStream(VoxelVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat"));
			data = GameResources.getInstance().getKryo().readObject(input, DimensionData.class);
			input.close();
		} catch (FileNotFoundException e) {
		}
	}

	protected void save() {
		Output output;
		try {
			output = new Output(new FileOutputStream(VoxelVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat"));
			GameResources.getInstance().getKryo().writeObject(output, data);
			output.close();
		} catch (FileNotFoundException e) {
		}
	}

	public void updateChunksGeneration(GameResources gm, float delta) {
		if (gm.getCamera().getPosition().x < 0)
			playerCX = (int) ((gm.getCamera().getPosition().x - 16) / 16);
		if (gm.getCamera().getPosition().y < 0)
			playerCY = (int) ((gm.getCamera().getPosition().y - 16) / 16);
		if (gm.getCamera().getPosition().z < 0)
			playerCZ = (int) ((gm.getCamera().getPosition().z - 16) / 16);
		if (gm.getCamera().getPosition().x > 0)
			playerCX = (int) ((gm.getCamera().getPosition().x) / 16);
		if (gm.getCamera().getPosition().y > 0)
			playerCY = (int) ((gm.getCamera().getPosition().y) / 16);
		if (gm.getCamera().getPosition().z > 0)
			playerCZ = (int) ((gm.getCamera().getPosition().z) / 16);
		for (int zr = -VoxelVariables.radius; zr <= VoxelVariables.radius; zr++) {
			int zz = playerCZ + zr;
			for (int xr = -VoxelVariables.radius; xr <= VoxelVariables.radius; xr++) {
				int xx = playerCX + xr;
				for (int yr = -VoxelVariables.radius; yr <= VoxelVariables.radius; yr++) {
					int yy = playerCY + yr;

					if (!hasChunk(xx, yy, zz)) {
						addChunk(new Chunk(new ChunkNode(xx, yy, zz), this));
						addTo(new ChunkNode(xx, yy, zz), addQueue);
					} else if (hasChunk(xx, yy, zz)) {
						Chunk chunk = getChunk(xx, yy, zz);
						if (!chunk.loaded)
							continue;
						chunk.update(this, gm.getCamera(), delta);
						if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16,
								chunk.posY + 16, chunk.posZ + 16)) {
							chunk.rebuildMesh(this);
						}
						for (ParticlePoint particlePoint : chunk.getParticlePoints()) {
							particleSystem.generateParticles(particlePoint, delta);
						}
					}

				}
			}
		}
		for (Chunk chunk : chunks.values()) {
			if (Math.abs(chunk.cx - playerCX) > VoxelVariables.radius) {
				chunk.unloadGraphics = true;
				addTo(new ChunkNode(chunk.cx, chunk.cy, chunk.cz), removeQueue);
			} else if (Math.abs(chunk.cz - playerCZ) > VoxelVariables.radius) {
				chunk.unloadGraphics = true;
				addTo(new ChunkNode(chunk.cx, chunk.cy, chunk.cz), removeQueue);
			} else if (Math.abs(chunk.cy - playerCY) > VoxelVariables.radius) {
				chunk.unloadGraphics = true;
				addTo(new ChunkNode(chunk.cx, chunk.cy, chunk.cz), removeQueue);
			}
		}
	}

	public void updateChunksRender(GameResources gm, boolean transparent) {
		if (transparent)
			return;
		if (transparent)
			glEnable(GL_BLEND);
		List<Chunk> chunks_ = new ArrayList<>();
		for (Chunk chunk : chunks.values()) {
			if (chunk != null) {
				chunk.updateGraphics();
				if (chunk.unloadGraphics)
					continue;
				if (chunk.getTess() == null)
					continue;
				if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16, chunk.posY + 16,
						chunk.posZ + 16)) {
					int res = glGetQueryObjectui(chunk.getTess().getOcclusion(), GL_QUERY_RESULT);
					if (res > 0 || transparent) {
						chunks_.add(chunk);
					}
				}
			}
		}
		Maths.sortHighToLow(chunks_);
		renderedChunks = 0;
		for (Chunk chunk : chunks_) {
			chunk.render(gm, transparent);
			renderedChunks++;
		}
		if (transparent)
			glDisable(GL_BLEND);
	}

	public void updateChunksShadow(GameResources gm) {
		for (Chunk chunk : chunks.values()) {
			if (chunk != null)
				if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16, chunk.posY + 16,
						chunk.posZ + 16))
					chunk.renderShadow(gm);
		}
	}

	public void updateChunksOcclusion(GameResources gm) {
		List<Chunk> chunks_ = new ArrayList<Chunk>();
		for (Chunk chunk : chunks.values()) {
			if (chunk != null)
				if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16, chunk.posY + 16,
						chunk.posZ + 16))
					chunks_.add(chunk);
		}
		Maths.sortLowToHigh(chunks_);
		for (Chunk chunk : chunks_) {
			chunk.renderOcclusion(gm);
		}
	}

	public void lighting() {
		while (!lightNodeRemovals.isEmpty()) {
			LightNodeRemoval node = lightNodeRemovals.poll();
			int x = node.x;
			int y = node.y;
			int z = node.z;
			int lightLevel = node.val;
			setupLightRemove(x - 1, y, z, lightLevel);
			setupLightRemove(x + 1, y, z, lightLevel);
			setupLightRemove(x, y, z - 1, lightLevel);
			setupLightRemove(x, y, z + 1, lightLevel);
			setupLightRemove(x, y - 1, z, lightLevel);
			setupLightRemove(x, y + 1, z, lightLevel);
		}
		while (!lightNodeAdds.isEmpty()) {
			LightNodeAdd node = lightNodeAdds.poll();
			int x = node.x;
			int y = node.y;
			int z = node.z;
			int cx = x >> 4;
			int cz = z >> 4;
			int cy = y >> 4;
			Chunk chunk = getChunk(cx, cy, cz);
			int lightLevel = (int) chunk.getTorchLight(x, y, z);
			setupLightAdd(x - 1, y, z, lightLevel);
			setupLightAdd(x + 1, y, z, lightLevel);
			setupLightAdd(x, y, z - 1, lightLevel);
			setupLightAdd(x, y, z + 1, lightLevel);
			setupLightAdd(x, y - 1, z, lightLevel);
			setupLightAdd(x, y + 1, z, lightLevel);
		}
	}

	public void setupLightAdd(int x, int y, int z, int lightLevel) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null)
			if (chunk.getTorchLight(x, y, z) + 2 <= lightLevel) {
				chunk.setTorchLight(x, y, z, lightLevel - 1);
				chunk.rebuild = true;
				lightNodeAdds.add(new LightNodeAdd(x, y, z));
			}
	}

	protected void setupLightRemove(int x, int y, int z, int lightLevel) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			int neighborLevel = (int) chunk.getTorchLight(x, y, z);
			if (neighborLevel != 0 && neighborLevel < lightLevel) {
				chunk.setTorchLight(x, y, z, 0);
				lightNodeRemovals.add(new LightNodeRemoval(x, y, z, neighborLevel));
				chunk.rebuild = true;
			} else if (neighborLevel >= lightLevel) {
				lightNodeAdds.add(new LightNodeAdd(x, y, z));
				chunk.rebuild = true;
			}
		}
	}

	protected void addTo(ChunkNode node, Queue<ChunkNode> queue) {
		if (!queue.contains(node))
			queue.add(node);
	}

	public void saveChunk(Chunk chunk) {
		try {
			Output output = new Output(new FileOutputStream(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim
					+ "/chunk_" + chunk.cx + "_" + chunk.cy + "_" + chunk.cz + ".dat"));
			GameResources.getInstance().getKryo().writeObject(output, chunk);
			output.close();
		} catch (Exception e) {
			throw new SaveChunkException(e);
		}
	}

	public void loadChunk(int cx, int cy, int cz) {
		try {
			Input input = new Input(new FileInputStream(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim
					+ "/chunk_" + cx + "_" + cy + "_" + cz + ".dat"));
			Chunk chunk = GameResources.getInstance().getKryo().readObject(input, Chunk.class);
			input.close();
			if (chunk != null) {
				chunk.onLoad();
				chunk.loaded = true;
				addChunk(chunk);
			}

		} catch (Exception e) {
			throw new LoadChunkException(e);
		}
	}

	public boolean existChunkFile(int cx, int cy, int cz) {
		return new File(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim + "/chunk_" + cx + "_" + cy + "_"
				+ cz + ".dat").exists();
	}

	public boolean existDimFile() {
		return new File(VoxelVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat").exists();
	}

	public Chunk getChunk(int cx, int cy, int cz) {
		return chunks.get(new ChunkKey(cx, cy, cz));
	}

	public boolean hasChunk(int cx, int cy, int cz) {
		return chunks.containsKey(new ChunkKey(cx, cy, cz));
	}

	public void addChunk(Chunk chunk) {
		ChunkKey key = new ChunkKey(chunk.cx, chunk.cy, chunk.cz);
		Chunk old = chunks.get(key);
		if (old != null) {
			removeChunk(old);
		}
		chunks.put(key.clone(), chunk);
		loadedChunks++;
		for (int xx = chunk.cx - 1; xx < chunk.cx + 1; xx++) {
			for (int zz = chunk.cz - 1; zz < chunk.cz + 1; zz++) {
				for (int yy = chunk.cy - 1; yy < chunk.cy + 1; yy++) {
					Chunk chunka = getChunk(xx, yy, zz);
					if (chunka != null) {
						chunka.rebuild = true;
					}
				}
			}
		}
	}

	public void removeChunk(Chunk chunk) {
		if (chunk != null) {
			ChunkKey key = new ChunkKey(chunk.cx, chunk.cy, chunk.cz);
			chunk.dispose();
			chunks.remove(key);
			loadedChunks--;
			for (int xx = chunk.cx - 1; xx < chunk.cx + 1; xx++) {
				for (int zz = chunk.cz - 1; zz < chunk.cz + 1; zz++) {
					for (int yy = chunk.cy - 1; yy < chunk.cy + 1; yy++) {
						Chunk chunka = getChunk(xx, yy, zz);
						if (chunka != null) {
							chunka.rebuild = true;
						}
					}
				}
			}
			chunk = null;
		}
	}

	public BlockBase getGlobalBlock(int x, int y, int z) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null)
			if (chunk.loaded)
				return chunk.getLocalBlock(x, y, z);
		return Block.Air;
	}

	public List<BoundingBox> getGlobalBoundingBox(BoundingBox box) {
		List<BoundingBox> array = new ArrayList<>();
		Vector3f vec = new Vector3f(0, 0, 0);

		for (int i = (int) Math.floor(box.min.x); i < (int) Math.ceil(box.max.x); i++) {
			for (int j = (int) Math.floor(box.min.y); j < (int) Math.ceil(box.max.y); j++) {
				for (int k = (int) Math.floor(box.min.z); k < (int) Math.ceil(box.max.z); k++) {
					vec.set(i, j, k);
					BlockBase block = getGlobalBlock(i, j, k);
					if (block.isCollision())
						array.add(block.getBoundingBox(vec));
				}
			}
		}
		return array;
	}

	public boolean setGlobalBlock(int x, int y, int z, BlockBase id) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			if (chunk.loaded) {
				chunk.setLocalBlock(x, y, z, id);
				return true;
			}
		}
		return false;
	}

	public void addLight(int x, int y, int z, int val) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			if (chunk.loaded) {
				chunk.setTorchLight(x, y, z, val);
				lightNodeAdds.add(new LightNodeAdd(x, y, z));
			}
		}
	}

	public void removeLight(int x, int y, int z, int val) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			if (chunk.loaded) {
				lightNodeRemovals.add(new LightNodeRemoval(x, y, z, (int) chunk.getTorchLight(x, y, z)));
				chunk.setTorchLight(x, y, z, 0);
			}
		}
	}

	public float getLight(int x, int y, int z) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			if (chunk.loaded)
				return chunk.getTorchLight(x, y, z);
		}
		return 0;
	}

	public void clearDimension() {
		running = false;
		Logger.log("Saving Dimension " + chunkDim);
		save();
		for (Chunk chunk : chunks.values()) {
			if (chunk != null) {
				addTo(new ChunkNode(chunk.cx, chunk.cy, chunk.cz), removeQueue);
			}
		}
		while (!removeQueue.isEmpty()) {
			ChunkNode node = removeQueue.poll();
			Chunk chnk = getChunk(node.cx, node.cy, node.cz);
			saveChunk(chnk);
			removeChunk(chnk);
		}

		chunks.clear();
	}

	public void disposeGraphics() {
	}

	public int getLoadedChunks() {
		return loadedChunks;
	}

	public int getRenderedChunks() {
		return renderedChunks;
	}

	public int getDimensionID() {
		return chunkDim;
	}

	public SimplexNoise getNoise() {
		return noise;
	}

	public int getSeed() {
		return seedi;
	}

	public ChunkGenerator getChunkGenerator() {
		return chunkGenerator;
	}

	public Engine getPhysicsEngine() {
		return physicsEngine;
	}

}