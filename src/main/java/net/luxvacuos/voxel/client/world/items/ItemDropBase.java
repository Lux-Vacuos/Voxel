package net.luxvacuos.voxel.client.world.items;

import net.luxvacuos.voxel.client.resources.models.TexturedModel;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.entities.GameEntity;
import net.luxvacuos.voxel.universal.util.vector.Vector3f;

public abstract class ItemDropBase extends GameEntity {

	private BlockBase block;

	public ItemDropBase(TexturedModel model, Vector3f position, BlockBase block, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.block = block;
	}

	public BlockBase getBlock() {
		return block;
	}

}
