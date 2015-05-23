package net.guerra24.voxel.client.world.block.types;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.world.block.Block;

public class BlockNull extends Block {

	@Override
	public byte getId() {
		return -2;
	}

	@Override
	public Entity getEntity(Vector3f pos) {
		return null;
	}

}
