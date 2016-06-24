package net.luxvacuos.voxel.client.world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.ashley.core.Engine;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.ParticlePoint;
import net.luxvacuos.voxel.client.resources.models.ParticleSystem;
import net.luxvacuos.voxel.client.world.chunks.Chunk;
import net.luxvacuos.voxel.client.world.chunks.ChunkGenerator;
import net.luxvacuos.voxel.client.world.chunks.ChunkNodeRemoval;

public class ClientDimension extends Dimension {

	public ClientDimension(String name, Random seed, int chunkDim, GameResources gm) {
		super(name, seed, chunkDim, gm);
	}

	@Override
	protected void init(GameResources gm) {
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

	@Override
	protected void load() {
	}

	@Override
	protected void save() {
	}

	@Override
	public void updateChunksGeneration(GameResources gm, float delta) {
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
			saveChunk(node.chunk);
			removeChunk(node.chunk);
		}
	}

}
