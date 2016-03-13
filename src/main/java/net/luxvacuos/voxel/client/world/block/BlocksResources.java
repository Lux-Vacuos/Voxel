/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.world.block;

import net.luxvacuos.voxel.client.resources.Loader;
import net.luxvacuos.voxel.client.resources.models.ModelTexture;
import net.luxvacuos.voxel.client.resources.models.RawModel;
import net.luxvacuos.voxel.client.resources.models.TessellatorTextureAtlas;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;
import net.luxvacuos.voxel.universal.util.vector.Vector2f;

public class BlocksResources {

	public static TexturedModel cubeTorch;

	public static TexturedModel missingDrop;

	private static TessellatorTextureAtlas tessellatorTextureAtlas;

	private static int normalMap;
	private static int heightMap;
	private static int specularMap;

	public static void createBlocks(Loader loader) {

		RawModel torch = loader.getObjLoader().loadObjModel("Torch");
		RawModel missing = loader.getObjLoader().loadObjModel("cube");

		tessellatorTextureAtlas = new TessellatorTextureAtlas(256, 256, loader.loadTextureBlocks("blocks"));
		normalMap = loader.loadTextureBlocks("blocks_normal");
		heightMap = loader.loadTextureBlocks("blocks_height");
		specularMap = loader.loadTextureBlocks("blocks_specular");

		ModelTexture torchTex = new ModelTexture(loader.loadTextureBlocks("Torch"));
		ModelTexture missingTex = new ModelTexture(loader.loadTextureBlocks("Missing"));
		cubeTorch = new TexturedModel(torch, torchTex);
		missingDrop = new TexturedModel(missing, missingTex);
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
		tessellatorTextureAtlas.registerTextureCoords("Lava", new Vector2f(32, 16));
		tessellatorTextureAtlas.registerTextureCoords("LavaSide", new Vector2f(48, 16));
	}

	public static TessellatorTextureAtlas getTessellatorTextureAtlas() {
		return tessellatorTextureAtlas;
	}

	public static int getHeightMap() {
		return heightMap;
	}

	public static int getNormalMap() {
		return normalMap;
	}

	public static int getSpecularMap() {
		return specularMap;
	}

}