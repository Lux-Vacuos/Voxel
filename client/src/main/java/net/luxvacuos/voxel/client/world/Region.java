package net.luxvacuos.voxel.client.world;

import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.voxel.client.world.chunks.Chunk;
import net.luxvacuos.voxel.universal.world.chunk.ChunkNode;

public class Region {

	private Map<ChunkNode, Chunk> chunks;

	public Region() {
		chunks = new HashMap<>();
	}

	public Chunk getChunk(int cx, int cy, int cz) {
		return chunks.get(new ChunkNode(cx, cy, cz));
	}

	public boolean hasChunk(int cx, int cy, int cz) {
		return chunks.containsKey(new ChunkNode(cx, cy, cz));
	}

	public void addChunk(Chunk chunk) {
		chunks.put(chunk.node, chunk);
	}

}
