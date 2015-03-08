package net.voxel.core.world.blocks.type;

import net.voxel.core.world.blocks.Blocks;

import com.nishu.utils.Color4f;

public class BlockStone extends Blocks {

	@Override
	public byte getId() {
		return 3;
	}

	@Override
	public Color4f getColor() {
		return Color4f.GRAY;
	}

	@Override
	public float[] getTexCoords() {
		return new float[] { 2f, 0f };
	}
}