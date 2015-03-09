package net.voxel.world;

import java.util.ArrayList;

import net.logger.Logger;
import net.voxel.utilites.Constants;
import net.voxel.utilites.Frustum;
import net.voxel.utilites.Spritesheet;
import net.voxel.world.chunks.Chunk;
import net.voxel.world.entities.mobs.MobManager;

import com.nishu.utils.Shader;
import com.nishu.utils.ShaderProgram;

public class WorldManager {

	private MobManager mobManager;

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
		Shader temp = new Shader("/shaders/chunk.vert", "/shaders/chunk.frag");
		shader = new ShaderProgram(temp.getvShader(), temp.getfShader());
		Logger.log("Shaders initialization");
	}

	private void init() {
		mobManager = new MobManager();
		loadedChunks = new ArrayList<Chunk>();
		activeChunks = new ArrayList<Chunk>();
	}

	private void createWorld() {
		for (int x = 0; x < Constants.viewDistance; x++) {
				for (int z = 0; z < Constants.viewDistance; z++) {
					activeChunks.add(new Chunk(shader, 1, x * Constants.CHUNKSIZE, 0, z * Constants.CHUNKSIZE));
					Constants.chunksLoaded++;
			}
		}
	}

	public void update() {
		mobManager.update();
	}

	public void render() {
		Constants.chunksFrustum = 0;
		Spritesheet.tiles.bind();
		getMobManager().getPlayer().getCamera().applyTranslations();
		for (int i = 0; i < activeChunks.size(); i++) {
			if (Frustum.getFrustum().cubeInFrustum(activeChunks.get(i).getPos().getX(), activeChunks.get(i).getPos().getY(), activeChunks.get(i).getPos().getZ(), activeChunks.get(i).getPos().getX() + Constants.CHUNKSIZE, activeChunks.get(i).getPos().getY() + Constants.CHUNKSIZE, activeChunks.get(i).getPos().getZ() + Constants.CHUNKSIZE)) {
				if (Math.abs(activeChunks.get(i).getCenter().getX() - (int) mobManager.getPlayer().getX()) < 64 && Math.abs(activeChunks.get(i).getCenter().getZ() - mobManager.getPlayer().getZ()) < 64 && activeChunks.get(i).getCenter().getY() - mobManager.getPlayer().getY() < 32) {
					Constants.chunksFrustum++;
					activeChunks.get(i).render();
				}
			}
		}
		mobManager.render();
	}

	public MobManager getMobManager() {
		return mobManager;
	}
}
