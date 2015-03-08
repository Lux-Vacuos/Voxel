package net.voxel.core.world.blocks.type;

import net.voxel.core.util.SpriteSheets;
import net.voxel.core.world.blocks.Blocks;

import com.nishu.utils.Color4f;

public class BlockGrass extends Blocks {

	@Override
	public byte getId() {
		return 2;
	}

	@Override
	public Color4f getColor() {
		return new Color4f(0f, 0.75f, 0.10f, 1f);
	}

	@Override
	public float[] getTexCoords() {
		return new float[] { 0 + SpriteSheets.blocks.uniformSize(), 0f };
	}

}
