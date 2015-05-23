package net.guerra24.voxel.client.world.block;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.world.block.types.BlockDimOre;
import net.guerra24.voxel.client.world.block.types.BlockDirt;
import net.guerra24.voxel.client.world.block.types.BlockGoldOre;
import net.guerra24.voxel.client.world.block.types.BlockGrass;
import net.guerra24.voxel.client.world.block.types.BlockIndes;
import net.guerra24.voxel.client.world.block.types.BlockNull;
import net.guerra24.voxel.client.world.block.types.BlockSand;
import net.guerra24.voxel.client.world.block.types.BlockStone;

public abstract class Block {

	public static Block Grass = new BlockGrass();
	public static Block Stone = new BlockStone();
	public static Block NULL = new BlockNull();
	public static Block Indes = new BlockIndes();
	public static Block Sand = new BlockSand();
	public static Block Dirt = new BlockDirt();
	public static Block DiamondOre = new BlockDimOre();
	public static Block GoldOre = new BlockGoldOre();

	private boolean isActive;

	public abstract byte getId();

	public abstract Entity getEntity(Vector3f pos);

	public static Block getBlock(byte id) {
		switch (id) {
		case -1:
			return Block.Indes;
		case 1:
			return Block.Stone;
		case 2:
			return Block.Grass;
		case 3:
			return Block.Sand;
		case 4:
			return Block.Dirt;
		case 5:
			return Block.DiamondOre;
		case 6:
			return Block.GoldOre;
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
