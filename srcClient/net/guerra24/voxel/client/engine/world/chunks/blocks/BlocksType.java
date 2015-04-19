package net.guerra24.voxel.client.engine.world.chunks.blocks;

import java.util.HashMap;

import net.guerra24.voxel.client.engine.world.chunks.blocks.types.BlockGrass;
import net.guerra24.voxel.client.engine.world.chunks.blocks.types.BlockStone;

public abstract class BlocksType {
	public static HashMap<Byte, BlocksType> tileMap = new HashMap<Byte, BlocksType>();

	public static BlocksType Stone = new BlockStone();
	public static BlocksType Grass = new BlockGrass();

	public abstract byte getId();

	public static BlocksType getTile(int tiles) {
		return tileMap.get(tiles);
	}

	public static void createTileMap() {
		tileMap.put((byte) 1, Stone);
		tileMap.put((byte) 2, Grass);
	}
}