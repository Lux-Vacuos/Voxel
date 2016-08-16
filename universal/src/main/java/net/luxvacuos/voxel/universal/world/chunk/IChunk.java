package net.luxvacuos.voxel.universal.world.chunk;

public interface IChunk {

	public ChunkNode getNode();
	
	public ChunkData getChunkData();
	
	public int getX();
	
	public int getY();
	
	public int getZ();
	
	
}
