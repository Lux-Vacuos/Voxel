package net.luxvacuos.voxel.universal.world.chunk;

import net.luxvacuos.voxel.universal.resources.IDisposable;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public interface IChunk extends IDisposable {

	public ChunkNode getNode();
	
	public ChunkData getChunkData();
	
	public int getX();
	
	public int getZ();
	
	public IBlock getBlockAt(int x, int y, int z);
	
	public ChunkSnapshot takeSnapshot();
	
	public IDimension getDimension();
	
	public void markForRebuild();
	
	public boolean needsRebuild();
	
	public boolean needsMeshRebuild();
	
	public void update(float delta);
}
