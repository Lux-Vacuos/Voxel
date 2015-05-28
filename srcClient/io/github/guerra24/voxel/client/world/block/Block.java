package io.github.guerra24.voxel.client.world.block;

import io.github.guerra24.voxel.client.world.block.types.BlockDimOre;
import io.github.guerra24.voxel.client.world.block.types.BlockDirt;
import io.github.guerra24.voxel.client.world.block.types.BlockGoldOre;
import io.github.guerra24.voxel.client.world.block.types.BlockGrass;
import io.github.guerra24.voxel.client.world.block.types.BlockIndes;
import io.github.guerra24.voxel.client.world.block.types.BlockNull;
import io.github.guerra24.voxel.client.world.block.types.BlockSand;
import io.github.guerra24.voxel.client.world.block.types.BlockStone;
import io.github.guerra24.voxel.client.world.entities.Entity;

import org.lwjgl.util.vector.Vector3f;

public abstract class Block {

	public static Block Grass = new BlockGrass();
	public static Block Stone = new BlockStone();
	public static Block NULL = new BlockNull();
	public static Block Indes = new BlockIndes();
	public static Block Sand = new BlockSand();
	public static Block Dirt = new BlockDirt();
	public static Block DiamondOre = new BlockDimOre();
	public static Block GoldOre = new BlockGoldOre();

	public abstract byte getId();

	public abstract Entity getEntity(Vector3f pos);

	public static Block getBlock(byte id) {
		switch (id) {
		case -2:
			return Block.NULL;
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

}
