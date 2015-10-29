package net.guerra24.voxel.world.chunks;

public class LightNode {
	public int x, y, z;
	public Chunk chunk;

	public LightNode(int x, int y, int z, Chunk chunk) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunk = chunk;
	}
}
