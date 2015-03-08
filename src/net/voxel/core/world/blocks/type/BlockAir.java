package net.voxel.core.world.blocks.type;

import com.nishu.utils.Color4f;

import net.voxel.core.world.blocks.Blocks;

public class BlockAir extends Blocks {

	@Override
	public byte getId() {
		return 0;
	}

	@Override
	public Color4f getColor() {
		return Color4f.WHITE;
	}

	@Override
	public float[] getTexCoords() {
		return new float[] {-1, -1} ;
	}

}
