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

package net.guerra24.voxel.client.world.block;

import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.ModelTexture;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.resources.models.TessellatorTextureAtlas;
import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class BlocksResources {

	public static TexturedModel cubeTorch;
	public static TexturedModel cubePortal;
	public static TexturedModel cubeLeaves;

	public static TessellatorTextureAtlas tessellatorTextureAtlas;

	public static void createBlocks(Loader loader) {

		RawModel torch = loader.getObjLoader().loadObjModel("Torch", loader);
		RawModel portal = loader.getObjLoader().loadObjModel("Portal", loader);
		RawModel leaves = loader.getObjLoader().loadObjModel("Leaves", loader);

		ModelTexture texture10 = new ModelTexture(loader.loadTextureBlocks("Leaves"));

		tessellatorTextureAtlas = new TessellatorTextureAtlas(256, 256, loader.loadTextureBlocks("blocks"));

		ModelTexture texture8 = new ModelTexture(loader.loadTextureBlocks("Torch"));
		ModelTexture texture9 = new ModelTexture(loader.loadTextureBlocks("Portal"));
		cubeTorch = new TexturedModel(torch, texture8);
		cubePortal = new TexturedModel(portal, texture9);
		cubeLeaves = new TexturedModel(leaves, texture10);
		loadTexCoords();
	}

	private static void loadTexCoords() {
		tessellatorTextureAtlas.registerTextureCoords("Cobblestone", new Vector2f(0, 0));
		tessellatorTextureAtlas.registerTextureCoords("Grass", new Vector2f(16, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSide", new Vector2f(32, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSideSnow", new Vector2f(48, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSnow", new Vector2f(64, 0));
		tessellatorTextureAtlas.registerTextureCoords("Dirt", new Vector2f(80, 0));
		tessellatorTextureAtlas.registerTextureCoords("Indes", new Vector2f(96, 0));
		tessellatorTextureAtlas.registerTextureCoords("Ice", new Vector2f(112, 0));
		tessellatorTextureAtlas.registerTextureCoords("Leaves", new Vector2f(128, 0));
		tessellatorTextureAtlas.registerTextureCoords("LeavesSnow", new Vector2f(144, 0));
		tessellatorTextureAtlas.registerTextureCoords("Sand", new Vector2f(160, 0));
		tessellatorTextureAtlas.registerTextureCoords("SandSnow", new Vector2f(176, 0));
		tessellatorTextureAtlas.registerTextureCoords("Stone", new Vector2f(208, 0));
		tessellatorTextureAtlas.registerTextureCoords("Wood", new Vector2f(224, 0));
		tessellatorTextureAtlas.registerTextureCoords("DimOre", new Vector2f(240, 0));
		tessellatorTextureAtlas.registerTextureCoords("Glass", new Vector2f(0, 16));
		tessellatorTextureAtlas.registerTextureCoords("GoldOre", new Vector2f(16, 16));
	}

}