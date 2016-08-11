package net.luxvacuos.voxel.server.api;

import com.badlogic.ashley.core.Entity;

import net.luxvacuos.voxel.server.resources.GameResources;
import net.luxvacuos.voxel.server.world.chunks.Chunk;
import net.luxvacuos.voxel.universal.world.chunk.ChunkNode;

public class MoltenAPI {

	public void testPrint() {
		System.out.println("TEST");
	}

	public Chunk getChunk(ChunkNode node) {
		return GameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().getChunk(node);
	}

	public void addChunk(Chunk chunk) {
		GameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().addChunk(chunk);
	}

	public void addEntity(Entity entity) {
		GameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
				.addEntity(entity);
	}

}
