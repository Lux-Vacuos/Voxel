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

package io.github.guerra24.voxel.client.world.block;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.render.textures.ModelTexture;
import io.github.guerra24.voxel.client.resources.OBJLoader;
import io.github.guerra24.voxel.client.resources.models.RawModel;
import io.github.guerra24.voxel.client.resources.models.TexturedModel;

public class BlocksResources {

	public static TexturedModel cubeIndes;
	public static TexturedModel cubeGrass;
	public static TexturedModel cubeStone;
	public static TexturedModel cubeSand;
	public static TexturedModel cubeGlass;
	public static TexturedModel cubeDirt;
	public static TexturedModel cubeDiamondOre;
	public static TexturedModel cubeGoldOre;

	public static void createBlocks() {

		RawModel model = OBJLoader.loadObjModel("Block",
				Kernel.gameResources.loader);
		ModelTexture texture0 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Indes"));
		ModelTexture texture = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Grass"));
		ModelTexture texture1 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Stone"));
		ModelTexture texture2 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Sand"));
		ModelTexture texture3 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Glass"));
		ModelTexture texture4 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Dirt"));
		ModelTexture texture5 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Diamond-Ore"));
		ModelTexture texture6 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("Gold-Ore"));

		cubeIndes = new TexturedModel(model, texture0);
		cubeGrass = new TexturedModel(model, texture);
		cubeStone = new TexturedModel(model, texture1);
		cubeSand = new TexturedModel(model, texture2);
		cubeGlass = new TexturedModel(model, texture3);
		cubeDirt = new TexturedModel(model, texture4);
		cubeDiamondOre = new TexturedModel(model, texture5);
		cubeGoldOre = new TexturedModel(model, texture6);
	}
}
