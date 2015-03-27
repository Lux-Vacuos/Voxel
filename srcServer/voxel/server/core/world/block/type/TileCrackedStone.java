package voxel.server.core.world.block.type;

import voxel.client.core.engine.color.Color4f;
import voxel.server.core.world.block.Tile;

public class TileCrackedStone extends Tile {

	@Override
	public byte getId() {
		return 2;
	}

	@Override
	public Color4f getColor() {
		return Color4f.GRAY;
	}

	@Override
	public int getTextID() {
		return 1;
	}

}
