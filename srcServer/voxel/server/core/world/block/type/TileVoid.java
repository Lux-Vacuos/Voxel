package voxel.server.core.world.block.type;

import voxel.server.core.glutil.Color4f;
import voxel.server.core.world.block.Tile;

public class TileVoid extends Tile {

	@Override
	public byte getId() {
		return 0;
	}

	@Override
	public Color4f getColor() {
		return Color4f.BLACK;
	}

	@Override
	public int getTextID() {
		return 3;
	}
}
