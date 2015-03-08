package net.voxel.core.world.blocks;

import net.voxel.core.world.blocks.type.BlockAir;
import net.voxel.core.world.blocks.type.BlockGrass;
import net.voxel.core.world.blocks.type.BlockVoid;

import com.nishu.utils.Color4f;

public abstract class Blocks {

	public static Blocks Air = new BlockAir();
	public static Blocks Void = new BlockVoid();
	public static Blocks Grass = new BlockGrass();

	public abstract byte getId();
	public abstract Color4f getColor();
	public abstract float[] getTexCoords();

	public static Blocks getBlocks(byte id) {
		switch (id) {
		case 0:
			return Blocks.Air;
		case 1:
			return Blocks.Void;
		case 2:
			return Blocks.Grass;
		}
		return Blocks.Void;
	}
}