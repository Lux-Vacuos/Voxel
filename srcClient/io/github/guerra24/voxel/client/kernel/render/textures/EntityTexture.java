package io.github.guerra24.voxel.client.kernel.render.textures;

import java.nio.ByteBuffer;

public class EntityTexture {

	private int width;
	private int height;
	private ByteBuffer buffer;

	public EntityTexture(ByteBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}
}
