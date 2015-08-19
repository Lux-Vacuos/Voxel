package io.github.guerra24.voxel.client.kernel.world.block.types;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;

import org.lwjgl.util.vector.Vector3f;

public class BlockTorch extends Block {

	@Override
	public byte getId() {
		return 9;
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

	@Override
	public Entity getSingleModel(Vector3f pos) {
		return new Entity(BlocksResources.cubeTorch, pos, 0, 0, 0, 1);
	}
}
