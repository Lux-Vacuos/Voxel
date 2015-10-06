package io.github.guerra24.voxel.client.kernel.world.block.types;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.block.BlockEntity;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;

public class BlockGlass extends Block {

	@Override
	public byte getId() {
		return 8;
	}

	@Override
	public BlockEntity getFaceUp(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeGlassUP, pos, 0, 0, 0, 1);
	}

	@Override
	public BlockEntity getFaceDown(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeGlassDOWN, pos, 0, 0, 0, 1);
	}

	@Override
	public BlockEntity getFaceEast(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeGlassEAST, pos, 0, 0, 0, 1);
	}

	@Override
	public BlockEntity getFaceWest(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeGlassWEST, pos, 0, 0, 0, 1);
	}

	@Override
	public BlockEntity getFaceNorth(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeGlassNORTH, pos, 0, 0, 0, 1);
	}

	@Override
	public BlockEntity getFaceSouth(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeGlassSOUTH, pos, 0, 0, 0, 1);
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
