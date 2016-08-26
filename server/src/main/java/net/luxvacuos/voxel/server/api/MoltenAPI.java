package net.luxvacuos.voxel.server.api;

import com.badlogic.ashley.core.Entity;

import net.luxvacuos.voxel.server.resources.ServerGameResources;
import net.luxvacuos.voxel.server.world.chunks.Chunk;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class MoltenAPI {

	public void testPrint() {
		System.out.println("TEST");
	}

	public Chunk getChunk(ChunkNode node) {
		return ServerGameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().getChunk(node);
	}

	public void addChunk(Chunk chunk) {
		ServerGameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().addChunk(chunk);
	}

	public void addEntity(Entity entity) {
		ServerGameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
				.addEntity(entity);
	}

}
