package net.luxvacuos.voxel.server.world.block;

import net.luxvacuos.voxel.server.world.Dimension;

public abstract class BlockEntity extends BlockBase {

	protected int x, y, z;

	public BlockEntity() {
	}

	public BlockEntity(Integer x, Integer y, Integer z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}


	public void update(Dimension dimension, float delta) {
	}

}
