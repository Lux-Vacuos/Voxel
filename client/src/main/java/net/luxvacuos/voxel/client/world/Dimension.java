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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.ParticlePoint;
import net.luxvacuos.voxel.client.resources.models.ParticleSystem;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.chunks.Chunk;
import net.luxvacuos.voxel.client.world.chunks.ChunkGenerator;
import net.luxvacuos.voxel.client.world.chunks.ChunkKey;
import net.luxvacuos.voxel.client.world.chunks.ChunkNodeRemoval;
import net.luxvacuos.voxel.client.world.chunks.LightNodeAdd;
import net.luxvacuos.voxel.client.world.chunks.LightNodeRemoval;
import net.luxvacuos.voxel.universal.core.exception.LoadChunkException;
import net.luxvacuos.voxel.universal.core.exception.SaveChunkException;

public abstract class Dimension {

	private int chunkDim;
	private Map<ChunkKey, Chunk> chunks;
	private SimplexNoise noise;
	private String name;
	private int seedi;
	private DimensionData data;
	private ChunkGenerator chunkGenerator;
	private Queue<LightNodeAdd> lightNodeAdds;
	private Queue<LightNodeRemoval> lightNodeRemovals;
	private Queue<ChunkNodeRemoval> chunkNodeRemovals;
	private ParticleSystem particleSystem;
	private DimensionService dimensionService;
	private int renderedChunks = 0;
	private int loadedChunks = 0;
	private Engine physicsEngine;
	private PhysicsSystem physicsSystem;
	public static int CHUNKS_LOADED_PER_FRAME = 1;

	public Dimension(String name, Random seed, int chunkDim, GameResources gm) {
		this.name = name;
		this.chunkDim = chunkDim;
		data = new DimensionData();
		data.addObject("Seed", seed.nextInt());

		File filec = new File(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim);
		if (!filec.exists())
			filec.mkdirs();
		if (existDimFile())
			try {
				load(gm);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		init(gm);
	}

	private void init(GameResources gm) {
		particleSystem = new ParticleSystem(gm.getTorchTexture(), 2, 1, -0.01f, 4, 0.5f);
		particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		particleSystem.setLifeError(0.8f);
		particleSystem.setScaleError(0.2f);
		particleSystem.setSpeedError(0.2f);
		seedi = (int) data.getObject("Seed");
		noise = new SimplexNoise(256, 0.15f, seedi);
		lightNodeAdds = new LinkedList<>();
		lightNodeRemovals = new LinkedList<>();
		chunkNodeRemovals = new LinkedList<>();
		chunks = new HashMap<>();
		chunkGenerator = new ChunkGenerator();
		dimensionService = new DimensionService();
		physicsEngine = new Engine();
		physicsSystem = new PhysicsSystem(this);
		physicsEngine.addSystem(physicsSystem);
	}

	private void load(GameResources gm) throws FileNotFoundException {
		Input input = new Input(new FileInputStream(VoxelVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat"));
		data = gm.getKryo().readObject(input, DimensionData.class);
		input.close();
	}

	private void save(GameResources gm) throws FileNotFoundException {
		Output output = new Output(
				new FileOutputStream(VoxelVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat"));
		gm.getKryo().writeObject(output, data);
		output.close();
	}

	public void updateChunksGeneration(GameResources gm, float delta) throws Exception {
		int chunkLoaded = 0;
		for (float zr = -VoxelVariables.radius * 16f; zr <= VoxelVariables.radius * 16f; zr += 16f) {
			float cz = (float) (gm.getCamera().getPosition().getZ() + zr);
			for (float xr = -VoxelVariables.radius * 16f; xr <= VoxelVariables.radius * 16f; xr += 16f) {
				float cx = (float) (gm.getCamera().getPosition().getX() + xr);
				for (float yr = -VoxelVariables.radius * 16f; yr <= VoxelVariables.radius * 16f; yr += 16f) {
					float cy = (float) (gm.getCamera().getPosition().getY() + yr);
					int xx = (int) (cx / 16f);
					int yy = (int) (cy / 16f);
					int zz = (int) (cz / 16f);

					if (!hasChunk(xx, yy, zz) && chunkLoaded < CHUNKS_LOADED_PER_FRAME) {
						if (existChunkFile(xx, yy, zz))
							loadChunk(xx, yy, zz, gm);
						else if (VoxelVariables.generateChunks)
							addChunk(new Chunk(xx, yy, zz));
						chunkLoaded++;
					} else if (hasChunk(xx, yy, zz)) {
						Chunk chunk = getChunk(xx, yy, zz);
						chunk.update(this, gm.getCamera(), delta);
						if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16,
								chunk.posY + 16, chunk.posZ + 16)) {
							chunk.rebuild(dimensionService, this);
						}
						for (ParticlePoint particlePoint : chunk.getParticlePoints()) {
							particleSystem.generateParticles(particlePoint, delta);
						}
					}

				}
			}
		}

		for (Chunk chunk : chunks.values()) {
			float dist = (float) Vector3f.sub(new Vector3f(gm.getCamera().getPosition()).div(16),
					new Vector3f(chunk.cx, chunk.cy, chunk.cz), null).lengthSquared();
			if (dist > new Vector3f((float) VoxelVariables.radius, (float) VoxelVariables.radius + 0.1f,
					(float) VoxelVariables.radius).lengthSquared()) {
				chunkNodeRemovals.add(new ChunkNodeRemoval(chunk));
			}
		}

		while (!chunkNodeRemovals.isEmpty()) {
			ChunkNodeRemoval node = chunkNodeRemovals.poll();
			saveChunk(node.chunk, gm);
			removeChunk(node.chunk);
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
				chunk.needsRebuild = true;
				lightNodeAdds.add(new LightNodeAdd(x, y, z));
			}
	}

	private void setupLightRemove(int x, int y, int z, int lightLevel) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			int neighborLevel = (int) chunk.getTorchLight(x, y, z);
			if (neighborLevel != 0 && neighborLevel < lightLevel) {
				chunk.setTorchLight(x, y, z, 0);
				lightNodeRemovals.add(new LightNodeRemoval(x, y, z, neighborLevel));
				chunk.needsRebuild = true;
			} else if (neighborLevel >= lightLevel) {
				lightNodeAdds.add(new LightNodeAdd(x, y, z));
				chunk.needsRebuild = true;
			}
		}
	}

	public void saveChunk(int cx, int cy, int cz, GameResources gm) throws SaveChunkException {
		try {
			Output output = new Output(new FileOutputStream(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim
					+ "/chunk_" + cx + "_" + cy + "_" + cz + ".dat"));
			gm.getKryo().writeObject(output, getChunk(cx, cy, cz));
			output.close();
		} catch (Exception e) {
			throw new SaveChunkException(e);
		}
	}

	public void saveChunk(Chunk chunk, GameResources gm) throws SaveChunkException {
		try {
			Output output = new Output(new FileOutputStream(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim
					+ "/chunk_" + chunk.cx + "_" + chunk.cy + "_" + chunk.cz + ".dat"));
			gm.getKryo().writeObject(output, chunk);
			output.close();
		} catch (Exception e) {
			throw new SaveChunkException(e);
		}
	}

	public void loadChunk(int cx, int cy, int cz, GameResources gm) throws LoadChunkException {
		try {
			Input input = new Input(new FileInputStream(VoxelVariables.WORLD_PATH + name + "/dimension_" + chunkDim
					+ "/chunk_" + cx + "_" + cy + "_" + cz + ".dat"));
			Chunk chunk = gm.getKryo().readObject(input, Chunk.class);
			input.close();
			if (chunk != null) {
				chunk.onLoad();
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
		ChunkKey key = ChunkKey.alloc(cx, cy, cz);
		Chunk chunk;
		chunk = chunks.get(key);
		key.free();
		return chunk;
	}

	public boolean hasChunk(int cx, int cy, int cz) {
		ChunkKey key = ChunkKey.alloc(cx, cy, cz);
		boolean contains;
		contains = chunks.containsKey(key);
		key.free();
		return contains;
	}

	public void addChunk(Chunk chunk) {
		ChunkKey key = ChunkKey.alloc(chunk.cx, chunk.cy, chunk.cz);
		Chunk old = chunks.get(key);
		if (old != null) {
			removeChunk(old);
		}
		chunks.put(key.clone(), chunk);
		key.free();
		loadedChunks++;
		for (int xx = chunk.cx - 1; xx < chunk.cx + 1; xx++) {
			for (int zz = chunk.cz - 1; zz < chunk.cz + 1; zz++) {
				for (int yy = chunk.cy - 1; yy < chunk.cy + 1; yy++) {
					Chunk chunka = getChunk(xx, yy, zz);
					if (chunka != null) {
						chunka.needsRebuild = true;
					}
				}
			}
		}
	}

	public void removeChunk(Chunk chunk) {
		if (chunk != null) {
			ChunkKey key = ChunkKey.alloc(chunk.cx, chunk.cy, chunk.cz);
			chunk.dispose();
			chunks.remove(key);
			key.free();
			loadedChunks--;
			for (int xx = chunk.cx - 1; xx < chunk.cx + 1; xx++) {
				for (int zz = chunk.cz - 1; zz < chunk.cz + 1; zz++) {
					for (int yy = chunk.cy - 1; yy < chunk.cy + 1; yy++) {
						Chunk chunka = getChunk(xx, yy, zz);
						if (chunka != null) {
							chunka.needsRebuild = true;
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
			return chunk.getLocalBlock(x, y, z);
		else
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
			chunk.setLocalBlock(x, y, z, id);
			return true;
		}
		return false;
	}

	public void addLight(int x, int y, int z, int val) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			chunk.setTorchLight(x, y, z, val);
			lightNodeAdds.add(new LightNodeAdd(x, y, z));
		}
	}

	public void removeLight(int x, int y, int z, int val) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			lightNodeRemovals.add(new LightNodeRemoval(x, y, z, (int) chunk.getTorchLight(x, y, z)));
			chunk.setTorchLight(x, y, z, 0);
		}
	}

	public float getLight(int x, int y, int z) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(cx, cy, cz);
		if (chunk != null) {
			return chunk.getTorchLight(x, y, z);
		}
		return 0;
	}

	public void clearDimension(GameResources gm) throws Exception {
		Logger.log("Saving Dimension " + chunkDim);
		save(gm);
		for (Chunk chunk : chunks.values()) {
			if (chunk != null) {
				chunkNodeRemovals.add(new ChunkNodeRemoval(chunk));
			}
		}
		while (!chunkNodeRemovals.isEmpty()) {
			ChunkNodeRemoval node = chunkNodeRemovals.poll();
			saveChunk(node.chunk, gm);
			removeChunk(node.chunk);
		}
		dimensionService.es.shutdown();
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

	public DimensionService getDimensionService() {
		return dimensionService;
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
