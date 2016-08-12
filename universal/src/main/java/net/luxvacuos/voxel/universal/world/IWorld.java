package net.luxvacuos.voxel.universal.world;

import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.BlockCoords;

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
	public void addLight(BlockCoords block, int val);
	
	public float getLight(BlockCoords block);
	
	public void removeLight(BlockCoords block);
}
