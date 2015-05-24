package net.guerra24.voxel.client.world;

import net.guerra24.voxel.client.kernel.DisplayManager;
import net.guerra24.voxel.client.kernel.Kernel;
import net.guerra24.voxel.client.resources.GuiResources;
import net.guerra24.voxel.client.world.chunks.Chunk;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class World {

	private static float pos = -0.85f;
	private static double pos2 = 0.0d;
	private Chunk[][] chunks;
	private int viewDistance = 2, x, z;
	public int time = 0;

	public void startWorld() {
		initialize();
		createWorld();
	}

	public void createWorld() {
		Kernel.gameResources.guis5.add(Kernel.guiResources.loadW);
		Kernel.gameResources.guis5.add(Kernel.guiResources.loadBar);
		Kernel.gameResources.guis5.remove(GuiResources.load);
		for (x = 0; x < viewDistance; x++) {
			for (z = 0; z < viewDistance; z++) {
				pos = (float) (pos + pos2);
				chunks[x][z] = new Chunk(new Vector3f(x * Chunk.CHUNK_SIZE, 0,
						z * Chunk.CHUNK_SIZE));
				Kernel.guiResources.loadBar.setPosition(new Vector2f(pos, 0));
				Kernel.gameResources.guiRenderer
						.render(Kernel.gameResources.guis5);
				DisplayManager.updateDisplay();
			}
		}
		Water.createWater();
		Kernel.gameResources.guis5.remove(Kernel.guiResources.loadW);
		Kernel.gameResources.guis5.remove(Kernel.guiResources.loadBar);
		Kernel.gameResources.guis5.add(GuiResources.load);
	}

	public void initialize() {
		chunks = new Chunk[viewDistance][viewDistance];
		if (viewDistance == 16) {
			pos2 = 16 / 6500.0;
		} else if (viewDistance == 8) {
			pos2 = 8 / 800.0;
		}
	}

	public void update() {
		time++;
		if (time == 20) {
			for (int x = 0; x < viewDistance; x++) {
				for (int z = 0; z < viewDistance; z++) {
					chunks[x][z].update();
				}
			}
			time = 0;
		}
	}
}
