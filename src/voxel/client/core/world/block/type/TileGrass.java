package voxel.client.core.world.block.type;

import voxel.client.core.util.Spritesheet;
import voxel.client.core.world.block.Tile;

import com.nishu.utils.Color4f;

public class TileGrass extends Tile {

	/* 
	 * Texture coords for multi-textured tiles
	 * bottom - first
	 * top - second
	 * front - third
	 * back - fourth
	 * left - fifth
	 * right - sixth
	 */
	
	@Override
	public byte getId() {
		return 1;
	}

	@Override
	public Color4f getColor() {
		return Color4f.GREEN;
	}

	@Override
	public float[] getTexCoords() {
		return new float[] { 4 * Spritesheet.tiles.uniformSize(), Spritesheet.tiles.uniformSize(), 
				Spritesheet.tiles.uniformSize(), Spritesheet.tiles.uniformSize(),
				3 * Spritesheet.tiles.uniformSize(), Spritesheet.tiles.uniformSize(),
				3 * Spritesheet.tiles.uniformSize(), 0,
				3 * Spritesheet.tiles.uniformSize(), Spritesheet.tiles.uniformSize(),
				3 * Spritesheet.tiles.uniformSize(), Spritesheet.tiles.uniformSize() };
	}
}
