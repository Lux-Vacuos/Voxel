package net.luxvacuos.voxel.universal.world.dimension;

import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public interface IDimension {
	
	public String getName();
	
	public int getID();

	public boolean exists();
	
	public void update(float delta);
	
	public ImmutableArray<IChunk> getLoadedChunks();
}
