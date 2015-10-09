package io.github.guerra24.voxel.client.kernel.world.block.types;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.block.BlockEntity;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.block.IBlock;

public class BlockPortal extends IBlock {

	@Override
	public byte getId() {
		return 10;
	}

	@Override
	public BlockEntity getFaceUp(Vector3f pos, float light) {
		return null;
	}

	@Override
	public BlockEntity getFaceDown(Vector3f pos) {
		return null;
	}

	@Override
	public BlockEntity getFaceEast(Vector3f pos) {
		return null;
	}

	@Override
	public BlockEntity getFaceWest(Vector3f pos) {
		return null;
	}

	@Override
	public BlockEntity getFaceNorth(Vector3f pos) {
		return null;
	}

	@Override
	public BlockEntity getFaceSouth(Vector3f pos) {
		return null;
	}

	@Override
	public WaterTile getWaterTitle(Vector3f pos) {
		return null;
	}

	@Override
	public BlockEntity getSingleModel(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubePortal, pos, 0, 0, 0, 1);
	}

	@Override
	public boolean usesSingleModel() {
		return true;
	}

}
