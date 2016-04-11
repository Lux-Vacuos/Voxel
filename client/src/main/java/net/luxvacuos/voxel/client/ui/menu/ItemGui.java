package net.luxvacuos.voxel.client.ui.menu;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.world.block.BlockBase;

public class ItemGui {
	private BlockBase block;
	private Vector3f position;
	private int total;
	
	public ItemGui() {
	}

	public ItemGui(Vector3f pos, BlockBase block) {
		this.position = pos;
		this.block = block;
	}

	public void generateModel(Tessellator tess) {
		if (block.isCustomModel())
			block.generateCustomModel(tess, position.x, position.y, position.z, 1f, true, false, false, true, false,
					true, 0, 0, 0, 0, 0, 0);
		else
			tess.generateCube(position.x, position.y, position.z, 1f, true, false, false, true, false, true, block, 0,
					0, 0, 0, 0, 0);
	}

	public void setBlock(BlockBase block) {
		this.block = block;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public BlockBase getBlock() {
		return block;
	}

	public Vector3f getPosition() {
		return position;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
