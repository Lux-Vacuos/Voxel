package net.guerra24.voxel.client.world.block.types;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.block.BlocksResources;

public class BlockDirt extends Block {

	@Override
	public byte getId() {
		return 4;
	}

	@Override
	public Entity getEntity(Vector3f pos) {
		return new Entity(BlocksResources.cubeDirt, pos, 0, 0, 0, 1);
	}

}
