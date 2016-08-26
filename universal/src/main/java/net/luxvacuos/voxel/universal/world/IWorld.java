package net.luxvacuos.voxel.universal.world;

import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public interface IWorld {
	
	//World Stuff
	public String getName();
	
	public void update(float delta);
	
	//Dimension stuff
	public void addDimension(IDimension dimension);
	
	public IDimension getDimension(int id);
	
	public void setActiveDimension(int id);
	
	public IDimension getActiveDimension();
	
	public ImmutableArray<IDimension> getDimensions();
	
	
}
