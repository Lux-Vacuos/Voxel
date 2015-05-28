package io.github.guerra24.voxel.client.world.block.types;

import io.github.guerra24.voxel.client.world.block.Block;
import io.github.guerra24.voxel.client.world.block.BlocksResources;
import io.github.guerra24.voxel.client.world.entities.Entity;

import org.lwjgl.util.vector.Vector3f;

public class BlockGrass extends Block {

	@Override
	public byte getId() {
		return 2;
	}

	@Override
	public Entity getEntity(Vector3f pos) {
		return new Entity(BlocksResources.cubeGrass, pos, 0, 0, 0, 1);
	}

}
