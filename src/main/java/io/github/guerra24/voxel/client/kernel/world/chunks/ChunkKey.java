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

package io.github.guerra24.voxel.client.kernel.world.chunks;

import java.util.ArrayList;

public class ChunkKey implements Cloneable {
	public int dim, cx, cz;

	public ChunkKey(int dim, int cx, int cz) {
		this.dim = dim;
		this.cx = cx;
		this.cz = cz;
	}

	public int hashCode() {
		return (dim << 16) ^ (cx << 8) ^ cz;
	}

	public boolean equals(Object obj) {
		ChunkKey key = (ChunkKey) obj;
		if (key.dim != dim)
			return false;
		if (key.cx != cx)
			return false;
		if (key.cz != cz)
			return false;
		return true;
	}

	public ChunkKey clone() {
		return new ChunkKey(dim, cx, cz);
	}

	private static ArrayList<ChunkKey> pool = new ArrayList<ChunkKey>();
	public static int cnt;

	public static ChunkKey alloc(int dim, int cx, int cz) {
		ChunkKey c;
		synchronized (pool) {
			int size = pool.size();
			if (size == 0) {
				cnt++;
				return new ChunkKey(dim, cx, cz);
			}
			c = pool.remove(size - 1);
		}
		c.dim = dim;
		c.cx = cx;
		c.cz = cz;
		return c;
	}

	private static void free(ChunkKey c) {
		synchronized (pool) {
			pool.add(c);
		}
	}

	public void free() {
		free(this);
	}
}