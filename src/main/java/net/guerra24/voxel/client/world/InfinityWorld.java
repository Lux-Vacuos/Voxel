/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.guerra24.voxel.client.api.ModInitialization;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.particle.ParticlePoint;
import net.guerra24.voxel.client.particle.ParticleSystem;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.util.Logger;
import net.guerra24.voxel.client.world.chunks.Chunk;
import net.guerra24.voxel.client.world.chunks.ChunkGenerator;
import net.guerra24.voxel.client.world.chunks.ChunkKey;
import net.guerra24.voxel.client.world.chunks.LightNode;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Dimensional World
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public class InfinityWorld implements IWorld {

	/**
	 * Dimensional World Data
	 */
	private int chunkDim;
	private int worldID;
	private HashMap<ChunkKey, Chunk> chunks;
	private Random seed;
	private SimplexNoise noise;
	private String name;
	private int xPlayChunk;
	private int zPlayChunk;
	private int yPlayChunk;
	private int tempRadius = 0;
	private int seedi;
	private ChunkGenerator chunkGenerator;
	private String codeName = "Infinity";
	private Queue<LightNode> lightNodes;
	private ParticleSystem particleSystem;
	private WorldService worldService;

	@Override
	public void startWorld(String name, Random seed, int chunkDim, ModInitialization api, GameResources gm) {
		this.name = name;
		this.seed = seed;
		this.chunkDim = chunkDim;
		gm.getCamera().setPosition(new Vector3f(0, 80, 0));
		if (existWorld()) {
			loadWorld(gm);
		}
		saveWorld(gm);
		init(gm);
		createDimension(gm, api);
	}

	@Override
	public void init(GameResources gm) {
		particleSystem = new ParticleSystem(gm.getTorchTexture(), 5, 1, -0.01f, 4, 0.5f);
		particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		particleSystem.setLifeError(0.8f);
		particleSystem.setScaleError(0.2f);
		particleSystem.setSpeedError(0.2f);
		seedi = seed.nextInt();
		noise = new SimplexNoise(128, 0.3f, seedi);
		lightNodes = new LinkedList<>();
		chunks = new HashMap<ChunkKey, Chunk>();
		chunkGenerator = new ChunkGenerator();
		worldService = new WorldService();
		gm.getPhysics().getMobManager().getPlayer().setPosition(gm.getCamera().getPosition());
	}

	@Override
	public void createDimension(GameResources gm, ModInitialization api) {
		Logger.log("Generating World");
		xPlayChunk = (int) (gm.getCamera().getPosition().x / 16);
		zPlayChunk = (int) (gm.getCamera().getPosition().z / 16);
		yPlayChunk = (int) (gm.getCamera().getPosition().y / 16);
		for (int zr = -4; zr <= 4; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -4; xr <= 4; xr++) {
				int xx = xPlayChunk + xr;
				for (int yr = -4; yr <= 4; yr++) {
					int yy = yPlayChunk + yr;
					if (zr * zr + xr * xr + yr * yr < 4 * 4 * 4) {
						if (!hasChunk(chunkDim, xx, yy, zz)) {
							if (existChunkFile(chunkDim, xx, yy, zz)) {
								loadChunk(chunkDim, xx, yy, zz, gm);
							} else {
								addChunk(new Chunk(chunkDim, xx, yy, zz, this, gm));
								saveChunk(chunkDim, xx, yy, zz, gm);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void updateChunksGeneration(GameResources gm, ModInitialization api, float delta) {
		if (gm.getCamera().getPosition().x < 0)
			xPlayChunk = (int) ((gm.getCamera().getPosition().x - 16) / 16);
		if (gm.getCamera().getPosition().y < 0)
			yPlayChunk = (int) ((gm.getCamera().getPosition().y - 16) / 16);
		if (gm.getCamera().getPosition().z < 0)
			zPlayChunk = (int) ((gm.getCamera().getPosition().z - 16) / 16);
		if (gm.getCamera().getPosition().x > 0)
			xPlayChunk = (int) ((gm.getCamera().getPosition().x) / 16);
		if (gm.getCamera().getPosition().y > 0)
			yPlayChunk = (int) ((gm.getCamera().getPosition().y) / 16);
		if (gm.getCamera().getPosition().z > 0)
			zPlayChunk = (int) ((gm.getCamera().getPosition().z) / 16);
		VoxelVariables.update();
		for (int zr = -tempRadius; zr <= tempRadius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -tempRadius; xr <= tempRadius; xr++) {
				int xx = xPlayChunk + xr;
				for (int yr = -tempRadius; yr <= tempRadius; yr++) {
					int yy = yPlayChunk + yr;
					if (zr * zr + xr * xr + yr * yr <= (VoxelVariables.genRadius - VoxelVariables.radiusLimit)
							* (VoxelVariables.genRadius - VoxelVariables.radiusLimit)
							* (VoxelVariables.genRadius - VoxelVariables.radiusLimit)) {
						if (!hasChunk(chunkDim, xx, yy, zz)) {
							if (!hasChunk(chunkDim, xx, yy, zz)) {
								if (existChunkFile(chunkDim, xx, yy, zz)) {
									loadChunk(chunkDim, xx, yy, zz, gm);
								} else {
									if (VoxelVariables.generateChunks) {
										addChunk(new Chunk(chunkDim, xx, yy, zz, this, gm));
										saveChunk(chunkDim, xx, yy, zz, gm);
									}
								}
							}
						} else {
							Chunk chunk = getChunk(chunkDim, xx, yy, zz);
							for (ParticlePoint particlePoint : chunk.getParticlePoints()) {
								particleSystem.generateParticles(particlePoint, delta);
							}
							chunk.update(this, chunkGenerator, worldService);
							if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16,
									chunk.posY + 16, chunk.posZ + 16)) {
								chunk.rebuild(worldService, this);
							}
						}
					}
					if (zr * zr + xr * xr + yr * yr <= VoxelVariables.genRadius * VoxelVariables.genRadius
							* VoxelVariables.genRadius
							&& zr * zr + xr * xr
									+ yr * yr >= (VoxelVariables.genRadius - VoxelVariables.radiusLimit + 1)
											* (VoxelVariables.genRadius - VoxelVariables.radiusLimit + 1)
											* (VoxelVariables.genRadius - VoxelVariables.radiusLimit + 1)) {
						if (hasChunk(getChunkDimension(), xx, yy, zz)) {
							saveChunk(getChunkDimension(), xx, yy, zz, gm);
							removeChunk(getChunk(getChunkDimension(), xx, yy, zz));
						}
					}
				}
			}
		}
		if (tempRadius <= VoxelVariables.genRadius)
			tempRadius++;
	}

	@Override
	public void updateChunksRender(GameResources gm) {
		for (int zr = -VoxelVariables.radius; zr <= VoxelVariables.radius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -VoxelVariables.radius; xr <= VoxelVariables.radius; xr++) {
				int xx = xPlayChunk + xr;
				for (int yr = -VoxelVariables.radius; yr <= VoxelVariables.radius; yr++) {
					int yy = yPlayChunk + yr;
					if (hasChunk(chunkDim, xx, yy, zz)) {
						Chunk chunk = getChunk(chunkDim, xx, yy, zz);
						if (chunk != null) {
							if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16,
									chunk.posY + 16, chunk.posZ + 16))
								chunk.render(gm);
						}
					}
				}
			}
		}
	}

	@Override
	public void updateChunksShadow(GameResources gm) {
		for (int zr = -VoxelVariables.radius; zr <= VoxelVariables.radius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -VoxelVariables.radius; xr <= VoxelVariables.radius; xr++) {
				int xx = xPlayChunk + xr;
				for (int yr = -VoxelVariables.radius; yr <= VoxelVariables.radius; yr++) {
					int yy = yPlayChunk + yr;
					if (hasChunk(chunkDim, xx, yy, zz)) {
						Chunk chunk = getChunk(chunkDim, xx, yy, zz);
						if (chunk != null)
							if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16,
									chunk.posY + 16, chunk.posZ + 16))
								chunk.renderShadow(gm);
					}
				}
			}
		}

	}

	@Override
	public void updateChunksOcclusion(GameResources gm) {
	}

	@Override
	public void lighting() {
		while (!lightNodes.isEmpty()) {
			LightNode node = lightNodes.poll();
			int x = node.x;
			int y = node.y;
			int z = node.z;
			int cx = x >> 4;
			int cz = z >> 4;
			int cy = y >> 4;
			Chunk chunk = getChunk(chunkDim, cx, cy, cz);
			int lightLevel = (int) chunk.getTorchLight(x, y, z);
			if (chunk.getTorchLight(x - 1, y, z) + 2 <= lightLevel) {
				setupLight(x - 1, y, z, lightLevel);
			}
			if (chunk.getTorchLight(x + 1, y, z) + 2 <= lightLevel) {
				setupLight(x + 1, y, z, lightLevel);
			}
			if (chunk.getTorchLight(x, y, z - 1) + 2 <= lightLevel) {
				setupLight(x, y, z - 1, lightLevel);
			}
			if (chunk.getTorchLight(x, y, z + 1) + 2 <= lightLevel) {
				setupLight(x, y, z + 1, lightLevel);
			}
			if (chunk.getTorchLight(x, y - 1, z) + 2 <= lightLevel) {
				setupLight(x, y - 1, z, lightLevel);
			}
			if (chunk.getTorchLight(x, y + 1, z) + 2 <= lightLevel) {
				setupLight(x, y + 1, z, lightLevel);
			}
		}
	}

	@Override
	public void setupLight(int x, int y, int z, int lightLevel) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(chunkDim, cx, cy, cz);
		if (chunk != null)
			if (chunk.getTorchLight(x, y, z) + 2 <= lightLevel) {
				chunk.setTorchLight(x, y, z, lightLevel - 1);
				chunk.needsRebuild = true;
				lightNodes.add(new LightNode(x, y, z));
			}
	}

	@Override
	public void switchDimension(int id, GameResources gm, ModInitialization api) {
		if (id != chunkDim) {
			clearDimension(gm);
			chunkDim = id;
			init(gm);
			createDimension(gm, api);
		}
	}

	@Override
	public void saveWorld(GameResources gm) {
		if (!existWorld()) {
			File file = new File(VoxelVariables.worldPath + name + "/");
			file.mkdirs();
		}
		if (!existChunkFolder(chunkDim)) {
			File filec = new File(VoxelVariables.worldPath + name + "/chunks_" + chunkDim + "/");
			filec.mkdirs();
		}
		try {
			Output output = new Output(new FileOutputStream(VoxelVariables.worldPath + name + "/world.dat"));
			gm.getKryo().writeObject(output, seed);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadWorld(GameResources gm) {
		try {
			Input input = new Input(new FileInputStream(VoxelVariables.worldPath + name + "/world.dat"));
			seed = gm.getKryo().readObject(input, Random.class);
			input.close();
		} catch (FileNotFoundException e) {
		}
	}

	@Override
	public void saveChunk(int chunkDim, int cx, int cy, int cz, GameResources gm) {
		try {
			Output output = new Output(new FileOutputStream(VoxelVariables.worldPath + name + "/chunks_" + chunkDim
					+ "/chunk_" + chunkDim + "_" + cx + "_" + cy + "_" + cz + ".dat"));
			gm.getKryo().writeObject(output, getChunk(chunkDim, cx, cy, cz));
			output.close();
		} catch (IOException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	public void loadChunk(int chunkDim, int cx, int cy, int cz, GameResources gm) {
		try {
			Input input = new Input(new FileInputStream(VoxelVariables.worldPath + name + "/chunks_" + chunkDim
					+ "/chunk_" + chunkDim + "_" + cx + "_" + cy + "_" + cz + ".dat"));
			Chunk chunk = gm.getKryo().readObject(input, Chunk.class);
			input.close();
			if (chunk != null) {
				chunk.load(gm);
				chunk.checkForMissingBlocks();
			}
			addChunk(chunk);
		} catch (FileNotFoundException e) {
		} catch (KryoException e) {
		}
	}

	@Override
	public boolean existChunkFile(int chunkDim, int cx, int cy, int cz) {
		File file = new File(VoxelVariables.worldPath + name + "/chunks_" + chunkDim + "/chunk_" + chunkDim + "_" + cx
				+ "_" + cy + "_" + cz + ".dat");
		return file.exists();
	}

	/**
	 * Check if exist a world file
	 * 
	 * @return true if exist
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	@Override
	public boolean existWorld() {
		File file = new File(VoxelVariables.worldPath + name + "/world.dat");
		return file.exists();
	}

	@Override
	public boolean existChunkFolder(int chunkDim) {
		File file = new File(VoxelVariables.worldPath + name + "/chunks_" + chunkDim + "/");
		return file.exists();
	}

	@Override
	public Chunk getChunk(int chunkDim, int cx, int cy, int cz) {
		ChunkKey key = ChunkKey.alloc(chunkDim, cx, cy, cz);
		Chunk chunk;
		chunk = chunks.get(key);
		key.free();
		return chunk;
	}

	@Override
	public boolean hasChunk(int chunkDim, int cx, int cy, int cz) {
		ChunkKey key = ChunkKey.alloc(chunkDim, cx, cy, cz);
		boolean contains;
		contains = chunks.containsKey(key);
		key.free();
		return contains;
	}

	@Override
	public void addChunk(Chunk chunk) {
		ChunkKey key = ChunkKey.alloc(chunk.dim, chunk.cx, chunk.cy, chunk.cz);
		Chunk old = chunks.get(key);
		if (old != null) {
			removeChunk(old);
		}
		chunks.put(key.clone(), chunk);
		key.free();
		for (int xx = chunk.cx - 1; xx < chunk.cx + 1; xx++) {
			for (int zz = chunk.cz - 1; zz < chunk.cz + 1; zz++) {
				for (int yy = chunk.cy - 1; yy < chunk.cy + 1; yy++) {
					Chunk chunka = getChunk(chunkDim, xx, yy, zz);
					if (chunka != null) {
						chunka.needsRebuild = true;
					}
				}
			}
		}
	}

	@Override
	public void removeChunk(Chunk chunk) {
		if (chunk != null) {
			ChunkKey key = ChunkKey.alloc(chunk.dim, chunk.cx, chunk.cy, chunk.cz);
			chunk.dispose();
			chunks.remove(key);
			key.free();
			for (int xx = chunk.cx - 1; xx < chunk.cx + 1; xx++) {
				for (int zz = chunk.cz - 1; zz < chunk.cz + 1; zz++) {
					for (int yy = chunk.cy - 1; yy < chunk.cy + 1; yy++) {
						Chunk chunka = getChunk(chunkDim, xx, yy, zz);
						if (chunka != null) {
							chunka.needsRebuild = true;
						}
					}
				}
			}
			chunk = null;
		}
	}

	@Override
	public int getChunksCount() {
		int cnt;
		cnt = chunks.size();
		return cnt;
	}

	@Override
	public byte getGlobalBlock(int x, int y, int z) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(chunkDim, cx, cy, cz);
		if (chunk != null)
			return chunk.getLocalBlock(x, y, z);
		else
			return 0;
	}

	@Override
	public void setGlobalBlock(int x, int y, int z, byte id) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(chunkDim, cx, cy, cz);
		if (chunk != null) {
			chunk.setLocalBlock(x, y, z, id);
			chunk.updated = false;
			chunk.needsRebuild = true;
		}
	}

	@Override
	public void lighting(int x, int y, int z, int val) {
		int cx = x >> 4;
		int cz = z >> 4;
		int cy = y >> 4;
		Chunk chunk = getChunk(chunkDim, cx, cy, cz);
		if (chunk != null) {
			chunk.setTorchLight(x, y, z, val);
			lightNodes.add(new LightNode(x, y, z));
		}
	}

	@Override
	public void clearDimension(GameResources gm) {
		Logger.log("Saving World");
		for (int zr = -VoxelVariables.genRadius; zr <= VoxelVariables.genRadius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -VoxelVariables.genRadius; xr <= VoxelVariables.genRadius; xr++) {
				int xx = xPlayChunk + xr;
				for (int yr = -VoxelVariables.genRadius; yr <= VoxelVariables.genRadius; yr++) {
					int yy = yPlayChunk + yr;
					if (zr * zr + xr * xr + yr * yr <= VoxelVariables.genRadius * VoxelVariables.genRadius
							* VoxelVariables.genRadius) {
						if (hasChunk(chunkDim, xx, yy, zz)) {
							saveChunk(chunkDim, xx, yy, zz, gm);
						}
					}
				}
			}
		}
		worldService.es.shutdown();
		chunks.clear();
	}

	@Override
	public int getzPlayChunk() {
		return zPlayChunk;
	}

	@Override
	public int getxPlayChunk() {
		return xPlayChunk;
	}

	@Override
	public int getWorldID() {
		return worldID;
	}

	@Override
	public int getChunkDimension() {
		return chunkDim;
	}

	@Override
	public int getyPlayChunk() {
		return yPlayChunk;
	}

	@Override
	public SimplexNoise getNoise() {
		return noise;
	}

	@Override
	public Random getSeed() {
		return seed;
	}

	@Override
	public ChunkGenerator getChunkGenerator() {
		return chunkGenerator;
	}

	public String getCodeName() {
		return codeName;
	}

	@Override
	public void setTempRadius(int tempRadius) {
		this.tempRadius = tempRadius;
	}

	public int getTempRadius() {
		return tempRadius;
	}

}
