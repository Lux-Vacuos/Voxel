package net.guerra24.voxel.client.world;

import net.guerra24.voxel.client.kernel.DisplayManager;
import net.guerra24.voxel.client.kernel.Engine;
import net.guerra24.voxel.client.resources.GuiResources;
import net.guerra24.voxel.client.world.chunks.Chunk;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class World {

	private static float pos = -0.85f;
	private static double pos2 = 0.0d;
	public static Chunk c;
	public static Chunk d;
	public int WORLD_SIZE;
	public int time = 0;

	public void init(int WORLD_SIZE) {
		this.WORLD_SIZE = WORLD_SIZE;
		initialize();
		Engine.gameResources.guis5.add(Engine.guiResources.loadW);
		Engine.gameResources.guis5.add(Engine.guiResources.loadBar);
		Engine.gameResources.guis5.remove(GuiResources.load);
		for (int x = 0; x < this.WORLD_SIZE; x++) {
			for (int z = 0; z < this.WORLD_SIZE; z++) {
				pos = (float) (pos + pos2);
				Water.createWater();
				c = new Chunk(new Vector3f(0, 0, 0));
				d = new Chunk(new Vector3f(16, 0, 0));
				Engine.guiResources.loadBar.setPosition(new Vector2f(pos, 0));
				Engine.gameResources.guiRenderer
						.render(Engine.gameResources.guis5);
				DisplayManager.updateDisplay();
			}
		}
		Engine.gameResources.guis5.remove(Engine.guiResources.loadW);
		Engine.gameResources.guis5.remove(Engine.guiResources.loadBar);
		Engine.gameResources.guis5.add(GuiResources.load);
		Engine.gameResources.allEntities.addAll(c.cubes);
		Engine.gameResources.allEntities.addAll(d.cubes);
	}

	public void initialize() {
		if (WORLD_SIZE == 16) {
			pos2 = 16 / 6500.0;
		} else if (WORLD_SIZE == 8) {
			pos2 = 8 / 800.0;
		}
	}

	public void update() {
		time++;
		if (time == 30) {
			c.update();
			d.update();
			time = 0;
		}
	}
}
