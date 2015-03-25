package voxel.server.core.world.block.type;

import voxel.server.core.world.block.Tile;

import com.nishu.utils.Color4f;

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
