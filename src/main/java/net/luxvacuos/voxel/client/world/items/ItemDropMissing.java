package net.luxvacuos.voxel.client.world.items;

import net.luxvacuos.voxel.client.resources.models.TexturedModel;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.universal.util.vector.Vector3f;

public class ItemDropMissing extends ItemDropBase {
	public ItemDropMissing(TexturedModel model, Vector3f position, BlockBase block, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, block, rotX, rotY, rotZ, scale);
	}
}
