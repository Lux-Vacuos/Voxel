package net.guerra24.voxel.world.block.types;

import net.guerra24.voxel.resources.models.WaterTile;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.block.BlockEntity;
import net.guerra24.voxel.world.block.BlocksResources;
import net.guerra24.voxel.world.block.IBlock;

public class BlockWood extends IBlock {

	@Override
	public byte getId() {
		return 12;
	}

	@Override
	public BlockEntity getFaceUp(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeWoodUP, pos, 0, 0, 0, 1, "UP");
	}

	@Override
	public BlockEntity getFaceDown(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeWoodDOWN, pos, 0, 0, 0, 1, "DOWN");
	}

	@Override
	public BlockEntity getFaceEast(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeWoodEAST, pos, 0, 0, 0, 1, "EAST");
	}

	@Override
	public BlockEntity getFaceWest(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeWoodWEST, pos, 0, 0, 0, 1, "WEST");
	}

	@Override
	public BlockEntity getFaceNorth(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeWoodNORTH, pos, 0, 0, 0, 1, "NORTH");
	}

	@Override
	public BlockEntity getFaceSouth(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeWoodSOUTH, pos, 0, 0, 0, 1, "SOUTH");
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
