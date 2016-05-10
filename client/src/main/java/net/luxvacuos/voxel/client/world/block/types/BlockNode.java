package net.luxvacuos.voxel.client.world.block.types;

import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.block.BlockEntity;
import net.luxvacuos.voxel.client.world.block.BlocksResources;

public class BlockNode extends BlockEntity {

	public BlockNode(Integer x, Integer y, Integer z) {
		super(x, y, z, BlocksResources.getNode());
		transparent = true;
		objModel = true;
	}

	public BlockNode() {
		super();
		transparent = true;
		objModel = true;
	}
	
	@Override
	public void update(Dimension dimension, float delta) {
		getModel().rotX += 16 * delta;
		getModel().rotY += 20 * delta;
		getModel().rotZ += 24 * delta;
	}

	@Override
	public void init() {
		setModel(BlocksResources.getNode());
	}

	@Override
	public byte getId() {
		return 18;
	}

}
