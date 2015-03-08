package net.voxel.core.world;

import java.util.ArrayList;

import com.nishu.utils.Shader;
import com.nishu.utils.ShaderProgram;

import net.voxel.core.world.chunks.Chunk;

public class WorldManager {
	@SuppressWarnings("unused")
	private ArrayList<Chunk> loadedChunks;
	private Chunk[][] activeChunks;
	
	private ShaderProgram shader;
	
	public WorldManager() {
		initGL();
		init();
		createWorld();
	}
	private void initGL() {
		Shader temp = new Shader("/shaders/chunk.vert", "/shaders/chunk.frag");
		shader = new ShaderProgram(temp.getvShader(), temp.getfShader());
	}
	private void init() {
		//not in use
		loadedChunks = new ArrayList<Chunk>();
		activeChunks = new Chunk[World.viewDistance][World.viewDistance];
	}
	private void createWorld() {
		for (int x = 0; x < World.viewDistance; x++) {
				for (int z = 0; z < World.viewDistance; z++) {
				activeChunks[x][z] = new Chunk(shader, 1, x * Chunk.CHUNKSIZE, 0, z * Chunk.CHUNKSIZE);
			}
		}
	}
	public void update() {
		
	}
	public void render() {
		for (int x = 0; x < World.viewDistance; x++) {
			for (int y = 0; y < World.viewDistance; y++)
				for (int z = 0; z < World.viewDistance; z++) {
					activeChunks[x][z].render();
			}
		}
	}
}
