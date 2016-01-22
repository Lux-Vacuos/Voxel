package net.guerra24.voxel.client.menu;

public class BlockGui {

	private byte id;
	private int tex;

	public BlockGui(byte id, int tex) {
		this.id = id;
		this.tex = tex;
	}

	public byte getId() {
		return id;
	}

	public int getTex() {
		return tex;
	}

}
