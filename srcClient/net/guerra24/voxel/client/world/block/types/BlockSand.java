package net.guerra24.voxel.client.world.block.types;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.block.BlocksResources;
import net.guerra24.voxel.client.world.entities.Entity;

public class BlockSand extends Block {

	@Override
	public byte getId() {
		return 3;
	}

	@Override
	public Entity getEntity(Vector3f pos) {
		return new Entity(BlocksResources.cubeSand, pos, 0, 0, 0, 1);
	}

}
