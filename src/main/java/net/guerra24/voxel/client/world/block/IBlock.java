package net.guerra24.voxel.client.world.block;

import net.guerra24.voxel.client.resources.models.WaterTile;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public abstract class IBlock  {
	
	public abstract byte getId();
	
	/**
	 * Get the Face Up of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 */
	public abstract BlockEntity getFaceUp(Vector3f pos);

	/**
	 * Get the Face Down of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return Entity
	 */
	public abstract BlockEntity getFaceDown(Vector3f pos);

	/**
	 * Get the Face East of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
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
	 */
	public abstract BlockEntity getFaceNorth(Vector3f pos);

	/**
	 * Get the Face South of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 */
	public abstract BlockEntity getFaceSouth(Vector3f pos);

	/**
	 * Get the WaterTile of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return WaterTile
	 */
	public abstract WaterTile getWaterTitle(Vector3f pos);

	/**
	 * Get a single model
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 */
	public abstract BlockEntity getSingleModel(Vector3f pos);

	/**
	 * Check if uses single model
	 * 
	 * @return Uses single model
	 */
	public abstract boolean usesSingleModel();
}
