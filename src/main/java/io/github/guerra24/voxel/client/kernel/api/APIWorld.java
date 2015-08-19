package io.github.guerra24.voxel.client.kernel.api;

import io.github.guerra24.voxel.client.kernel.world.block.Block;

/**
 * World API
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.2 Build-55
 * @since 0.0.2 Build-55
 *
 */
public class APIWorld {
	/**
	 * Register a block to the world generation
	 * 
	 * @param id
	 *            Block id
	 * @param block
	 *            Block
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void registerBlock(byte id, Block block) {
		Block.registerBlock(id, block);
	}
}
