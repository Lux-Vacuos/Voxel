package net.guerra24.voxel.world.block.types;

import net.guerra24.voxel.resources.models.WaterTile;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.block.BlockEntity;
import net.guerra24.voxel.world.block.IBlock;

public class BlockAir extends IBlock {

	@Override
	public byte getId() {
		return 0;
	}

	@Override
	public BlockEntity getFaceUp(Vector3f pos) {
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
		return null;
	}

	@Override
	public boolean usesSingleModel() {
		return false;
	}

}
