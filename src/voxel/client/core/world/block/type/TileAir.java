package voxel.client.core.world.block.type;

import voxel.client.core.world.block.Tile;

import com.nishu.utils.Color4f;

public class TileAir extends Tile{

	@Override
	public byte getId() {
		return -1;
	}

	@Override
	public Color4f getColor() {
		return Color4f.WHITE;
	}

	@Override
	public float[] getTexCoords() {
		return new float[] {-1, -1};
	}
}
