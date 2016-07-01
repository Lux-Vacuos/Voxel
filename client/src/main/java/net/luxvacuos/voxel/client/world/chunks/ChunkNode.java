package net.luxvacuos.voxel.client.world.chunks;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.resources.GameResources;

public class ChunkNode {

	public int cx, cy, cz;
	public float distance;

	public ChunkNode(int cx, int cy, int cz) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		distance = (float) Vector3f.sub(GameResources.getInstance().getCamera().getPosition(),
				new Vector3f(cx * 16 + 8f, cy * 16 + 8f, cz * 16 + 8f), null).lengthSquared();
	}

	@Override
	public boolean equals(Object obj) {
		ChunkNode node = (ChunkNode) obj;
		return node.cx == this.cx && node.cy == this.cy && node.cz == this.cz;
	}

}
