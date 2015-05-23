package net.guerra24.voxel.client.world;

import net.guerra24.voxel.client.kernel.Engine;
import net.guerra24.voxel.client.resources.models.WaterTile;

public class Water {

	public static WaterTile water;

	public static void createWater() {
		Engine.gameResources.waters
				.add(water = new WaterTile(0, 0, 60.4f + 16));
	}

}
