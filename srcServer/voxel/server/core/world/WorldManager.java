package voxel.server.core.world;

import java.util.ArrayList;

import voxel.client.core.engine.shaders.ShaderProgram;
import voxel.client.core.util.ConstantsClient;
import voxel.client.core.util.Logger;
import voxel.server.core.util.Frustum;
import voxel.server.core.world.chunk.Chunk;
import voxel.server.core.world.entities.type.MobManager;

public class WorldManager {

	private static MobManager mobManager;

	@SuppressWarnings("unused")
	private ArrayList<Chunk> loadedChunks;
	private ArrayList<Chunk> activeChunks;

	private ShaderProgram shader;

	public WorldManager() {
		initGL();
		init();
		createWorld();
	}

	private void initGL() {
		Logger.log("Initializing Shaders");
		Logger.log("Shaders initialization");
	}

	private void init() {
		mobManager = new MobManager();
		loadedChunks = new ArrayList<Chunk>();
		activeChunks = new ArrayList<Chunk>();
	}

	private void createWorld() {
		for (int x = 0; x < ConstantsClient.viewDistance; x++) {
			for (int z = 0; z < ConstantsClient.viewDistance; z++) {
				activeChunks.add(new Chunk(shader, 1, x * ConstantsClient.CHUNKSIZE,
						0, z * ConstantsClient.CHUNKSIZE));
				ConstantsClient.chunksLoaded++;
			}
		}
	}

	public void update() {
		mobManager.update();
	}

	public void render() {
		ConstantsClient.chunksFrustum = 0;
		getMobManager().getPlayer().getCamera().applyTranslations();
		for (int i = 0; i < activeChunks.size(); i++) {
			if (Frustum.getFrustum().cubeInFrustum(
					activeChunks.get(i).getPos().getX(),
					activeChunks.get(i).getPos().getY(),
					activeChunks.get(i).getPos().getZ(),
					activeChunks.get(i).getPos().getX() + ConstantsClient.CHUNKSIZE,
					activeChunks.get(i).getPos().getY() + ConstantsClient.CHUNKSIZE,
					activeChunks.get(i).getPos().getZ() + ConstantsClient.CHUNKSIZE)) {
				if (Math.abs(activeChunks.get(i).getCenter().getX() + 16
						- (int) mobManager.getPlayer().getX()) < 24
						&& Math.abs(activeChunks.get(i).getCenter().getZ() + 16
								- mobManager.getPlayer().getZ()) < 24
						&& activeChunks.get(i).getCenter().getY()
								- mobManager.getPlayer().getY() < 24) {
					ConstantsClient.chunksFrustum++;
					activeChunks.get(i).render();
				}
			}
		}
		mobManager.render();
	}

	public static MobManager getMobManager() {
		return mobManager;
	}
}
