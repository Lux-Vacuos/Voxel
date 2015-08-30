package io.github.guerra24.voxel.client.kernel.api.mod;

/**
 * Chunk Handler
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category API
 */
public abstract class ChunkHandler {
	/**
	 * Chunk Generation
	 * 
	 * @param blocks
	 *            Blocks
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract void chunkGen(byte[][][] blocks, int x, int y, int z);
}
