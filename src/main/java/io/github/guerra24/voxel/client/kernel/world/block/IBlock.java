package io.github.guerra24.voxel.client.kernel.world.block;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

public abstract class IBlock {
	/**
	 * Gets the Block ID
	 * 
	 * @return ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract byte getId();

	/**
	 * Get the Face Up of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceUp(Vector3f pos, float light);

	/**
	 * Get the Face Down of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return Entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceDown(Vector3f pos);

	/**
	 * Get the Face East of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceEast(Vector3f pos);

	/**
	 * Get the Face West of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 */
	public abstract BlockEntity getFaceWest(Vector3f pos);

	/**
	 * Get the Face North of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceNorth(Vector3f pos);

	/**
	 * Get the Face South of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceSouth(Vector3f pos);

	/**
	 * Get the WaterTile of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return WaterTile
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract WaterTile getWaterTitle(Vector3f pos);

	/**
	 * Get a single model
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getSingleModel(Vector3f pos);

	/**
	 * Check if uses single model
	 * 
	 * @return Uses single model
	 */
	public abstract boolean usesSingleModel();
}
