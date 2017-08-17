package net.luxvacuos.voxel.universal.world.utils;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public final class BlockPos {
	
	/**
	 * The Coords for this block
	 */
	private final BlockNode coords;
	
	/**
	 * The Dimension this block is in
	 */
	private final IDimension dimension;
	
	/**
	 * If this BlockPos was gotten from a ChunkSnapshot
	 */
	protected boolean fromSnapshot = false;
	
	protected BlockPos(IDimension dimension, BlockNode coords) {
		this.coords = coords;
		this.dimension = dimension;
	}
	
	public final BlockNode getCoords() {
		return this.coords;
	}
	
	public final IDimension getDimension() {
		return this.dimension;
	}
	
	public boolean isFromSnapshot() {
		return this.fromSnapshot;
	}

}
