package net.voxel.core.world.blocks;

public class BlocksVoid extends Blocks{

	public BlocksVoid() {
		super((byte) 0);
	}
	@Override
	public byte getId() {
		return 0;
	}
}