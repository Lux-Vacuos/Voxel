package net.luxvacuos.voxel.client.world.block;

import net.luxvacuos.voxel.client.world.Dimension;

@Deprecated
public abstract class BlockEntity extends BlockBase {

	protected int x, y, z;

	public BlockEntity() {
	}

	public BlockEntity(Integer x, Integer y, Integer z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void init() {
	}

	public void update(Dimension dimension, float delta) {
	}

	public void render() {
	}

}
