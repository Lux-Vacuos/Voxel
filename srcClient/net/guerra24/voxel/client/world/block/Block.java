package net.guerra24.voxel.client.world.block;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.world.block.types.BlockGrass;
import net.guerra24.voxel.client.world.block.types.BlockNull;
import net.guerra24.voxel.client.world.block.types.BlockStone;

public abstract class Block {

	public static Block Grass = new BlockGrass();
	public static Block Stone = new BlockStone();
	public static Block NULL = new BlockNull();

	private boolean isActive;

	public abstract byte getId();
	public abstract Entity getEntity(Vector3f pos);

	public static Block getBlock(byte id) {
		switch (id) {
		case 1:
			return Block.Stone;
		case 2:
			return Block.Grass;
		}
		return Block.NULL;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
