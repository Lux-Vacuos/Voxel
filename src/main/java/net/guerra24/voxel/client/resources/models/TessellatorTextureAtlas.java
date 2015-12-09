package net.guerra24.voxel.client.resources.models;

import java.util.HashMap;
import java.util.Map;

import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector8f;

public class TessellatorTextureAtlas {

	private Map<String, Vector8f> texcoords;
	private int width, height;
	private int texture;
	private float ax, ay;

	public TessellatorTextureAtlas(int textureWidth, int textureHeight, int texture) {
		texcoords = new HashMap<String, Vector8f>();
		this.width = textureWidth;
		this.height = textureHeight;
		this.texture = texture;
		ax = 16f / (float) width;
		ay = 16f / (float) height;
	}

	public void registerTextureCoords(String name, Vector2f texcoords) {
		Vector8f tex = new Vector8f(texcoords.x / width, (texcoords.y / height) + ay, texcoords.x / width,
				texcoords.y / height, (texcoords.x / width) + ax, texcoords.y / height, (texcoords.x / width) + ax,
				(texcoords.y / height) + ay);
		this.texcoords.put(name.toLowerCase(), tex);
	}

	public Vector8f getTextureCoords(String name) {
		Vector8f coords = texcoords.get(name.toLowerCase());
		if (coords == null)
			return new Vector8f(0, 0, 0, 1, 1, 1, 1, 0);
		return coords;
	}

	public int getTexture() {
		return texture;
	}

}
