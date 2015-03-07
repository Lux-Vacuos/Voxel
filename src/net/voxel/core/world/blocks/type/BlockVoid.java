package net.voxel.core.world.blocks.type;

import net.voxel.core.world.blocks.Blocks;

import com.nishu.utils.Color4f;

public class BlockVoid extends Blocks {

	@Override
	public byte getId() {
		return 0;
	}

	@Override
	public Color4f getColor() {
		return Color4f.BLACK;
	}
}