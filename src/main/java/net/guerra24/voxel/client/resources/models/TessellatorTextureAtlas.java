/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
