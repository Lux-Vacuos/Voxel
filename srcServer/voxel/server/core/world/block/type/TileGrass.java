package voxel.server.core.world.block.type;

import voxel.client.core.engine.color.Color4f;
import voxel.server.core.world.block.Tile;

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
