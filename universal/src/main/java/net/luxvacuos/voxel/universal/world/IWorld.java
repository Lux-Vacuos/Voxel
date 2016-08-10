package net.luxvacuos.voxel.universal.world;

import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.voxel.universal.world.block.BlockPos;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public interface IWorld {
	
	//World Stuff
	public String getName();
	
	//Dimension stuff
	public void addDimension(IDimension dimension);
	
	public IDimension getDimension(int id);
	
	public void setActiveDimension(int id);
	
	public IDimension getActiveDimension();
	
	public ImmutableArray<IDimension> getDimensions();
	
	//Helper functions for Dimensions
	public void addLight(int dimensionID, BlockPos block, int val);
	
	public float getLight(int dimensionID, BlockPos block);
	
	public void removeLight(int dimensionID, BlockPos block);
}
