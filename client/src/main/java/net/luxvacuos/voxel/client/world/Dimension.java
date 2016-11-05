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
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.glfw.Sync;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.ParticlePoint;
import net.luxvacuos.voxel.client.resources.models.ParticleSystem;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.chunks.Chunk;
import net.luxvacuos.voxel.client.world.chunks.ChunkGenerator;
import net.luxvacuos.voxel.client.world.chunks.LightNodeAdd;
import net.luxvacuos.voxel.client.world.chunks.LightNodeRemoval;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

@Deprecated
public abstract class Dimension {

	protected int chunkDim;
	// protected ConcurrentMap<ChunkKey, Chunk> chunks;
	protected Map<ChunkNode, Chunk> chunks;
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
	protected Queue<ChunkNode> rebuildQueue;

	protected ParticleSystem particleSystem;
	protected int renderedChunks = 0;
	protected int loadedChunks = 0;
	protected Engine physicsEngine;
	protected PhysicsSystem physicsSystem;
	protected Thread processThread;
	protected boolean running = true;
	private List<ChunkNode> tmp;

	public Dimension(String name, Random seed, int chunkDim, GameResources gm) {
		this.name = name;
		this.chunkDim = chunkDim;
		data = new DimensionData();
		data.addObject("Seed", seed.nextInt());
		data.addObject("PlayerPos", gm.getCamera().getPosition());
		init(gm);
	}

	protected void init(GameResources gm) {
		File filec = new File(ClientVariables.WORLD_PATH + name + "/dimension_" + chunkDim);
		if (!filec.exists())
			filec.mkdirs();
		if (existDimFile())
			load();
		particleSystem = new ParticleSystem(gm.getTorchTexture(), 2, 1, -0.01f, 4, 0.5f);
		particleSystem.setDirection(new Vector3d(0, 1, 0), 0.1f);
		particleSystem.setLifeError(0.8f);
		particleSystem.setScaleError(0.2f);
		particleSystem.setSpeedError(0.2f);
		seedi = (int) data.getObject("Seed");
		if ((Vector3d) data.getObject("PlayerPos") == null)
			gm.getCamera().setPosition(new Vector3d(0, 140, 0));
		else
			gm.getCamera().setPosition((Vector3d) data.getObject("PlayerPos"));
		noise = new SimplexNoise(256, 0.3f, seedi);
		lightNodeAdds = new LinkedList<>();
		lightNodeRemovals = new LinkedList<>();
		chunks = new ConcurrentHashMap<>();
		chunkGenerator = new ChunkGenerator();
		physicsEngine = new Engine();
		physicsSystem = new PhysicsSystem(this);
		physicsEngine.addSystem(physicsSystem);
		addQueue = new ConcurrentLinkedQueue<>();
		removeQueue = new ConcurrentLinkedQueue<>();
		rebuildQueue = new ConcurrentLinkedQueue<>();
		tmp = new LinkedList<>();
		processThread = new Thread(() -> {
			Kryo kryo = new Kryo();
			Sync sync = new Sync();
			ChunkNode node;
			kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
			while (running) {
				try {
					for (Chunk chunk : chunks.values()) {
						if (Math.abs(chunk.node.getX() - playerCX) > ClientVariables.radius) {
							addTo(chunk.node, removeQueue);
						} else if (Math.abs(chunk.node.getZ() - playerCZ) > ClientVariables.radius) {
							addTo(chunk.node, removeQueue);
						} else if (Math.abs(chunk.node.getY() - playerCY) > ClientVariables.radius) {
							addTo(chunk.node, removeQueue);
						}
					}
					if (!addQueue.isEmpty()) {
						tmp = new ArrayList<ChunkNode>(addQueue);
						// Maths.sortLowToHighN(tmp); //XXX: Need to reimplement
						// this without the player dependency
						addQueue = new ConcurrentLinkedQueue<ChunkNode>(tmp);
					}
					if (!rebuildQueue.isEmpty()) {
						tmp = new ArrayList<ChunkNode>(rebuildQueue);
						// Maths.sortLowToHighN(tmp);
						rebuildQueue = new ConcurrentLinkedQueue<ChunkNode>(tmp);
					}

					while (!removeQueue.isEmpty()) {
						node = removeQueue.poll();
						Chunk chnk = getChunk(node);
						// saveChunk(kryo, chnk);
						if (chnk != null)
							chnk.remove = true;
					}

					while (!addQueue.isEmpty()) {
						node = addQueue.poll();
						// if (existChunkFile(node)) {
						// loadChunk(kryo, node);
						// } else {
						Chunk chnk = getChunk(node);
						if (chnk == null)
							continue;
						chnk.init(this);
						// }
					}
					while (!rebuildQueue.isEmpty()) {
						node = rebuildQueue.poll();
						Chunk chn = getChunk(node);
						if (chn == null)
							continue;
						chn.rebuildMesh(this);
					}
					if (!StateMachine.isRunning())
						break;
					sync.sync(30);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		processThread.setDaemon(true);
		processThread.start();
	}

	protected void load() {
		Input input;
		try {
			input = new Input(new FileInputStream(ClientVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat"));
			data = GameResources.getInstance().getKryo().readObject(input, DimensionData.class);
			input.close();
		} catch (FileNotFoundException e) {
		}
	}

	protected void save() {
		Output output;
		try {
			output = new Output(new FileOutputStream(ClientVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat"));
			GameResources.getInstance().getKryo().writeObject(output, data);
			output.close();
		} catch (FileNotFoundException e) {
		}
	}

	public void updateChunksGeneration(Camera camera, Frustum frustum, float delta) {
		if (camera.getPosition().x < 0)
			playerCX = (int) ((camera.getPosition().x - 16) / 16);
		if (camera.getPosition().y < 0)
			playerCY = (int) ((camera.getPosition().y - 16) / 16);
		if (camera.getPosition().z < 0)
			playerCZ = (int) ((camera.getPosition().z - 16) / 16);
		if (camera.getPosition().x > 0)
			playerCX = (int) ((camera.getPosition().x) / 16);
		if (camera.getPosition().y > 0)
			playerCY = (int) ((camera.getPosition().y) / 16);
		if (camera.getPosition().z > 0)
			playerCZ = (int) ((camera.getPosition().z) / 16);

		ChunkNode node;
		int xx, yy, zz;
		for (int zr = -ClientVariables.radius; zr <= ClientVariables.radius; zr++) {
			zz = playerCZ + zr;
			for (int xr = -ClientVariables.radius; xr <= ClientVariables.radius; xr++) {
				xx = playerCX + xr;
				for (int yr = -ClientVariables.radius; yr <= ClientVariables.radius; yr++) {
					yy = playerCY + yr;
					if (!hasChunk(xx, yy, zz)) {
						node = new ChunkNode(xx, yy, zz);
						addTo(node, addQueue);
						addChunk(new Chunk(node, this));
					} else if (hasChunk(xx, yy, zz)) {
						Chunk chunk = getChunk(xx, yy, zz);
						if (!chunk.loaded || chunk.remove)
							continue;
						chunk.update(this, camera, delta);
						if (chunk.checkForRebuild())
							if (frustum.cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16,
									chunk.posY + 16, chunk.posZ + 16))
								addTo(new ChunkNode(xx, yy, zz), rebuildQueue);
						for (ParticlePoint particlePoint : chunk.getParticlePoints()) {
							particleSystem.generateParticles(particlePoint, delta);
						}
					}

				}
			}
		}
	}

	public void updateChunksRender(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation,
			Matrix4d projectionMatrix, Matrix4d shadowProjectionMatrix, int shadowMap, int shadowData, Frustum frustum, boolean transparent) {
		if (transparent)
			return; // XXX: DISABLED
		if (transparent)
			glEnable(GL_BLEND);
		List<Chunk> chunks_ = new ArrayList<>();
		for (Chunk chunk : chunks.values()) {
			if (chunk != null) {
				if (chunk.remove) {
					removeChunk(chunk);
					continue;
				}
				chunk.updateGraphics(projectionMatrix, shadowProjectionMatrix);
				if (chunk.getTess() == null || !chunk.loaded || chunk.remove)
					continue;
				if (frustum.cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16, chunk.posY + 16,
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
		for (

		Chunk chunk : chunks_) {
			chunk.render(camera, sunCamera, clientWorldSimulation, projectionMatrix, shadowMap, shadowData,
					transparent);
			renderedChunks++;
		}
		if (transparent)

			glDisable(GL_BLEND);
	}

	public void updateChunksShadow(Camera sunCamera, Matrix4d shadowProjectionMatrix, Frustum frustum) {
		for (Chunk chunk : chunks.values()) {
			if (chunk != null)
				if (frustum.cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16, chunk.posY + 16,
						chunk.posZ + 16))
					chunk.renderShadow(sunCamera, shadowProjectionMatrix);
		}
	}

	public void updateChunksOcclusion(Window window, Camera camera, Matrix4d projectionMatrix, Frustum frustum) {

		int width = (int) (window.getWidth() * window.getPixelRatio() / 6f);
		int height = (int) (window.getHeight() * window.getPixelRatio() / 6f);
		window.setViewport(0, 0, width, height);

		List<Chunk> chunks_ = new ArrayList<Chunk>();
		for (Chunk chunk : chunks.values()) {
			if (chunk != null)
				if (frustum.cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16, chunk.posY + 16,
						chunk.posZ + 16))
					chunks_.add(chunk);
		}
		Maths.sortLowToHigh(chunks_);
		for (Chunk chunk : chunks_) {
			chunk.renderOcclusion(camera, projectionMatrix);
		}

		window.resetViewport();
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

	protected void setupLightAdd(int x, int y, int z, int lightLevel) {
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

	public void saveChunk(Kryo kryo, Chunk chunk) {
		if (chunk != null)
			try {
				Output output = new Output(
						new FileOutputStream(ClientVariables.WORLD_PATH + name + "/dimension_" + chunkDim + "/chunk_"
								+ chunk.node.getX() + "_" + chunk.node.getY() + "_" + chunk.node.getZ() + ".dat"));
				kryo.writeObject(output, chunk);
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void loadChunk(Kryo kryo, ChunkNode node) {
		try {
			Input input = new Input(new FileInputStream(ClientVariables.WORLD_PATH + name + "/dimension_" + chunkDim
					+ "/chunk_" + node.getX() + "_" + node.getY() + "_" + node.getZ() + ".dat"));
			Chunk chunk = kryo.readObject(input, Chunk.class);
			input.close();
			if (chunk != null) {
				chunk.onLoad();
				chunk.loaded = true;
				addChunk(chunk);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean existChunkFile(ChunkNode node) {
		return new File(ClientVariables.WORLD_PATH + name + "/dimension_" + chunkDim + "/chunk_" + node.getX() + "_"
				+ node.getY() + "_" + node.getZ() + ".dat").exists();
	}

	public boolean existDimFile() {
		return new File(ClientVariables.WORLD_PATH + name + "/dim_" + chunkDim + ".dat").exists();
	}

	private Chunk getChunk(int x, int y, int z) {
		return this.getChunk(new ChunkNode(x, y, z));
	}

	public Chunk getChunk(ChunkNode node) {
		return chunks.get(node);
	}

	protected boolean hasChunk(int x, int y, int z) {
		return this.hasChunk(new ChunkNode(x, y, z));
	}

	public boolean hasChunk(ChunkNode node) {
		return chunks.containsKey(node);
	}

	public void addChunk(Chunk chunk) {
		// ChunkKey key = new ChunkKey(chunk.cx, chunk.cy, chunk.cz);
		Chunk old = chunks.get(chunk.node);
		if (old != null) {
			removeChunk(old);
		}
		chunks.put(chunk.node, chunk);
		loadedChunks++;
		for (int xx = chunk.node.getX() - 1; xx < chunk.node.getX() + 1; xx++) {
			for (int zz = chunk.node.getZ() - 1; zz < chunk.node.getZ() + 1; zz++) {
				for (int yy = chunk.node.getY() - 1; yy < chunk.node.getY() + 1; yy++) {
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
			// ChunkKey key = new ChunkKey(chunk.cx, chunk.cy, chunk.cz);
			chunks.remove(chunk.node);
			chunk.dispose();
			loadedChunks--;
			for (int xx = chunk.node.getX() - 1; xx < chunk.node.getX() + 1; xx++) {
				for (int zz = chunk.node.getZ() - 1; zz < chunk.node.getZ() + 1; zz++) {
					for (int yy = chunk.node.getY() - 1; yy < chunk.node.getY() + 1; yy++) {
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
		Vector3d vec = new Vector3d(0, 0, 0);

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
		data.addObject("PlayerPos", GameResources.getInstance().getCamera().getPosition());
		save();
		for (Chunk chunk : chunks.values()) {
			if (chunk != null) {
				addTo(chunk.node, removeQueue);
			}
		}
		while (!removeQueue.isEmpty()) {
			ChunkNode node = removeQueue.poll();
			Chunk chnk = getChunk(node);
			saveChunk(GameResources.getInstance().getKryo(), chnk);
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