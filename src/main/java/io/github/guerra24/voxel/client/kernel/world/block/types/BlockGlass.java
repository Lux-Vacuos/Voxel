package io.github.guerra24.voxel.client.kernel.world.block.types;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;

import org.lwjglx.util.vector.Vector3f;

public class BlockGlass extends Block {

	@Override
	public byte getId() {
		return 8;
	}

	@Override
	public Entity getFaceUp(Vector3f pos) {
		return new Entity(BlocksResources.cubeGlassUP, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceDown(Vector3f pos) {
		return new Entity(BlocksResources.cubeGlassDOWN, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceEast(Vector3f pos) {
		return new Entity(BlocksResources.cubeGlassEAST, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceWest(Vector3f pos) {
		return new Entity(BlocksResources.cubeGlassWEST, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceNorth(Vector3f pos) {
		return new Entity(BlocksResources.cubeGlassNORTH, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceSouth(Vector3f pos) {
		return new Entity(BlocksResources.cubeGlassSOUTH, pos, 0, 0, 0, 1);
	}

	@Override
	public WaterTile getWaterTitle(Vector3f pos) {
		return null;
	}

	@Override
	public Entity getSingleModel(Vector3f pos) {
		return null;
	}

}
