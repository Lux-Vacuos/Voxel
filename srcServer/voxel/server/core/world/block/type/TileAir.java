package voxel.server.core.world.block.type;

import voxel.server.core.glutil.Color4f;
import voxel.server.core.world.block.Tile;

public class TileAir extends Tile {

	@Override
	public byte getId() {
		return -1;
	}

	@Override
	public Color4f getColor() {
		return Color4f.WHITE;
	}

	@Override
	public int getTextID() {
		return 0;
	}
}
