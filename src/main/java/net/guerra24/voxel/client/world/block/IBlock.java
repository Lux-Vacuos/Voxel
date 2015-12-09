package net.guerra24.voxel.client.world.block;

import net.guerra24.voxel.client.resources.models.WaterTile;
import net.guerra24.voxel.universal.util.vector.Vector3f;
import net.guerra24.voxel.universal.util.vector.Vector8f;

public abstract class IBlock {

	public abstract byte getId();

	public abstract Vector8f texCoordsUp();

	public abstract Vector8f texCoordsDown();

	public abstract Vector8f texCoordsFront();

	public abstract Vector8f texCoordsBack();

	public abstract Vector8f texCoordsRight();

	public abstract Vector8f texCoordsLeft();

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
