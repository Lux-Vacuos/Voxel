package net.voxel.core.world.blocks;

import com.nishu.utils.Color4f;

public class BlocksVoid extends Blocks{

	@Override
	public byte getId() {
		return 0;
	}

	@Override
	public Color4f getColor() {
		return Color4f.BLACK;
	}
}