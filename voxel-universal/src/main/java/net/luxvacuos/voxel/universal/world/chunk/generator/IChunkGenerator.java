package net.luxvacuos.voxel.universal.world.chunk.generator;

import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public interface IChunkGenerator {
	
	public void generateChunk(IChunk chunk, int worldX, int worldY, int worldZ);

}
