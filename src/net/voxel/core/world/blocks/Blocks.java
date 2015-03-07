package net.voxel.core.world.blocks;

import net.voxel.core.world.blocks.type.BlockGrass;
import net.voxel.core.world.blocks.type.BlockVoid;

import com.nishu.utils.Color4f;

public abstract class Blocks {

	public static Blocks Void = new BlockVoid();
	public static Blocks Grass = new BlockGrass();

	private boolean isActive;

	public abstract byte getId();

	public abstract Color4f getColor();

	public static Blocks getBlocks(byte id) {
		switch (id) {
		case 0:
			return Blocks.Void;
		case 1:
			return Blocks.Grass;
		}
		return Blocks.Void;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}