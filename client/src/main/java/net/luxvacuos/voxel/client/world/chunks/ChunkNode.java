package net.luxvacuos.voxel.client.world.chunks;

public class ChunkNode {

	public int cx, cy, cz;

	public ChunkNode(int cx, int cy, int cz) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
	}

	@Override
	public boolean equals(Object obj) {
		ChunkNode node = (ChunkNode) obj;
		return node.cx == this.cx && node.cy == this.cy && node.cz == this.cz;
	}

}
