package net.voxel.core.world.blocks.type;

import com.nishu.utils.Color4f;

import net.voxel.core.world.blocks.Blocks;

public class BlockGrass extends Blocks {

	@Override
	public byte getId() {
		return 1;
	}

	@Override
	public Color4f getColor() {
		return new Color4f(0f, 0.75f, 0.10f, 1f);
	}

}
