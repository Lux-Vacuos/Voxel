package net.guerra24.voxel.client.util;

public class Pair<FIRST, SECOND> {
	public FIRST first;
	public SECOND second;

	public Pair() {
		this(null, null);
	}

	public Pair(FIRST first, SECOND second) {
		this.first = first;
		this.second = second;
	}
}
