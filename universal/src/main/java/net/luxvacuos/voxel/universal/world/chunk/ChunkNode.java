package net.luxvacuos.voxel.universal.world.chunk;

import net.luxvacuos.voxel.universal.world.utils.BlockCoords;

public final class ChunkNode {

	private final int x, y, z;
	
	public ChunkNode(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ChunkNode))
			return false;
		ChunkNode other = (ChunkNode) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	
	public static ChunkNode getFromBlockCoords(int x, int y, int z) {
		int cx = x >> 4;
		int cy = y >> 4;
		int cz = z >> 4;
		
		return new ChunkNode(cx, cy, cz);
	}
	
	public static ChunkNode getFromBlockCoords(BlockCoords block) {
		int cx = block.getX() >> 4;
		int cy = block.getY() >> 4;
		int cz = block.getZ() >> 4;
		
		return new ChunkNode(cx, cy, cz);
	}
}
