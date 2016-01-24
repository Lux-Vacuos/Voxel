package net.guerra24.voxel.client.menu;

public class BlockGui {

	private byte id;
	private int tex;
	private int total;

	public BlockGui(byte id, int tex, int total) {
		this.id = id;
		this.tex = tex;
		this.total = total;
	}

	public byte getId() {
		return id;
	}

	public int getTex() {
		return tex;
	}

	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}

}
