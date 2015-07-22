package io.github.guerra24.voxel.client.kernel.world.block.types;

import org.lwjgl.util.vector.Vector3f;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;

public class BlockAir extends Block {

	@Override
	public byte getId() {
		return 0;
	}

	@Override
	public Entity getFaceUp(Vector3f pos) {
		return null;
	}

	@Override
	public Entity getFaceDown(Vector3f pos) {
		return null;
	}

	@Override
	public Entity getFaceEast(Vector3f pos) {
		return null;
	}

	@Override
	public Entity getFaceWest(Vector3f pos) {
		return null;
	}

	@Override
	public Entity getFaceNorth(Vector3f pos) {
		return null;
	}

	@Override
	public Entity getFaceSouth(Vector3f pos) {
		return null;
	}

	@Override
	public WaterTile getWaterTitle(Vector3f pos) {
		return null;
	}

}
