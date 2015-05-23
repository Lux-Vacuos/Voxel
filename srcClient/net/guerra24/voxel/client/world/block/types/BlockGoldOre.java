package net.guerra24.voxel.client.world.block.types;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.block.BlocksResources;

public class BlockGoldOre extends Block {

	@Override
	public byte getId() {
		return 6;
	}

	@Override
	public Entity getEntity(Vector3f pos) {
		return new Entity(BlocksResources.cubeGoldOre, pos, 0, 0, 0, 1);
	}

}
