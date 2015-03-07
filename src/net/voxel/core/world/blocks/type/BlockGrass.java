package net.voxel.core.world.blocks.type;

import com.nishu.utils.Color4f;

import net.voxel.core.world.blocks.Blocks;

public class BlockGrass extends Blocks{

	@Override
	public byte getId() {
		return 1;
	}

	@Override
	public Color4f getColor() {
		return Color4f.GREEN;
	}
	
}
