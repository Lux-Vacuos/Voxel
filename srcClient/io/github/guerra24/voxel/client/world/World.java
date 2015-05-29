package io.github.guerra24.voxel.client.world;

import io.github.guerra24.voxel.client.kernel.DisplayManager;
import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.resources.GuiResources;
import io.github.guerra24.voxel.client.world.chunks.Chunk;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class World {

	private static float pos = -0.85f;
	private static double pos2 = 0.0d;
	public Chunk[][] chunks;
	public int viewDistance = 8;
	private int x;
	private int z;
	public float time = 0;

	public void startWorld() {
		initialize();
		createWorld();
	}

	private void createWorld() {
		Kernel.gameResources.guis5.add(Kernel.guiResources.loadW);
		Kernel.gameResources.guis5.add(Kernel.guiResources.loadBar);
		Kernel.gameResources.guis5.remove(GuiResources.load);
		pos = -0.85f;
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

	private void initialize() {
		chunks = new Chunk[viewDistance][viewDistance];
		if (viewDistance == 16) {
			pos2 = 16 / 6500.0;
		} else if (viewDistance == 8) {
			pos2 = 8 / 800.0;
		}
	}

	public void test() {
		if (Mouse.isButtonDown(0)) {
			chunks[0][0].blocks[(int) Kernel.gameResources.mouse
					.getCurrentRay().x * 10][(int) Kernel.gameResources.mouse
					.getCurrentRay().y * 10][(int) Kernel.gameResources.mouse
					.getCurrentRay().z * 10] = 0;
			System.out
					.println(Kernel.gameResources.mouse.getCurrentRay().x * 10);
			System.out
					.println(Kernel.gameResources.mouse.getCurrentRay().y * 10);
			System.out
					.println(Kernel.gameResources.mouse.getCurrentRay().z * 10);
			chunks[0][0].isToRebuild = true;
		}
	}

	public void update() {
		time++;
		if (time == 10) {
			for (int x = 0; x < viewDistance; x++) {
				for (int z = 0; z < viewDistance; z++) {
					chunks[x][z].update();
					Kernel.standaloneRender();
				}
			}
			time = 0;
		}
	}
}
