package net.voxel.core.world.blocks;

import java.util.HashMap;

import jdk.nashorn.internal.ir.Block;
import net.voxel.core.world.blocks.type.BlockAir;
import net.voxel.core.world.blocks.type.BlockGrass;
import net.voxel.core.world.blocks.type.BlockVoid;

import com.nishu.utils.Color4f;

public abstract class Blocks {
	
	public static HashMap<Byte, Blocks> blocksMap = new HashMap<Byte, Blocks>();
	
	public static Blocks Air = new BlockAir();
	public static Blocks Void = new BlockVoid();
	public static Blocks Grass = new BlockGrass();

	public abstract byte getId();
	public abstract Color4f getColor();
	public abstract float[] getTexCoords();
	
	public static Blocks getBlock(byte id) {
		return blocksMap.get(id);
	}
	public static void createBlockMap() {
		blocksMap.put((byte) 0, Air);
		blocksMap.put((byte) 1, Void);
		blocksMap.put((byte) 2, Grass);
	}
}