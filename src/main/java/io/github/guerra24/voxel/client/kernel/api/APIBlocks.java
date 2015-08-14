package io.github.guerra24.voxel.client.kernel.api;

import io.github.guerra24.voxel.client.kernel.world.block.Block;

public class APIBlocks {
	public static void registerBlock(byte id, Block block) {
		Block.registerBlock(id, block);
	}
}
