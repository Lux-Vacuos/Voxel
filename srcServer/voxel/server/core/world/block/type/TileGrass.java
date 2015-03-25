package voxel.server.core.world.block.type;

import voxel.server.core.world.block.Tile;

import com.nishu.utils.Color4f;

public class TileGrass extends Tile {
	@Override
	public byte getId() {
		return 1;
	}

	@Override
	public Color4f getColor() {
		return Color4f.GREEN;
	}

	@Override
	public int getTextID() {
		return 2;
	}
}
