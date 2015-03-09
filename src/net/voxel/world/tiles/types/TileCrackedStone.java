package net.voxel.world.tiles.types;

import net.voxel.utilites.Spritesheet;
import net.voxel.world.tiles.Tile;

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
