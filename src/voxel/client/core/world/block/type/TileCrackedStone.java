package voxel.client.core.world.block.type;

import voxel.client.core.util.Spritesheet;
import voxel.client.core.world.block.Tile;

import com.nishu.utils.Color4f;

public class TileCrackedStone extends Tile{

	@Override
	public byte getId() {
		return 2;
	}

	@Override
	public Color4f getColor() {
		return Color4f.GRAY;
	}

	@Override
	public float[] getTexCoords() {
		return new float[] { 2 * Spritesheet.tiles.uniformSize(), 0f };
	}

}
