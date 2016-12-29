package net.luxvacuos.voxel.universal.world;

import java.util.Collection;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public interface IWorld {

	// World Stuff
	public String getName();

	public void update(float delta);

	public void dispose();

	// Dimension stuff
	public void addDimension(IDimension dimension);

	public IDimension getDimension(int id);

	public void setActiveDimension(int id);

	public IDimension getActiveDimension();

	public Collection<IDimension> getDimensions();

}
