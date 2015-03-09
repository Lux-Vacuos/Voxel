package net.voxel.world.tiles.types;

import net.voxel.world.tiles.Tile;

import com.nishu.utils.Color4f;

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
	public float[] getTexCoords() {
		return new float[] { 0f, 0f };
	}
}
