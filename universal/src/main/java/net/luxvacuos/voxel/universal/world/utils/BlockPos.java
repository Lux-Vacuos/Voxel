package net.luxvacuos.voxel.universal.world.utils;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public final class BlockPos {
	
	/**
	 * The Coords for this block
	 */
	private final BlockCoords coords;
	
	/**
	 * The Dimension this block is in
	 */
	private final IDimension dimension;
	
	/**
	 * If this BlockPos was gotten from a ChunkSnapshot
	 */
	protected boolean fromSnapshot = false;
	
	protected BlockPos(IDimension dimension, BlockCoords coords) {
		this.coords = coords;
		this.dimension = dimension;
	}
	
	public final BlockCoords getCoords() {
		return this.coords;
	}
	
	public final IDimension getDimension() {
		return this.dimension;
	}
	
	public boolean isFromSnapshot() {
		return this.fromSnapshot;
	}

}
