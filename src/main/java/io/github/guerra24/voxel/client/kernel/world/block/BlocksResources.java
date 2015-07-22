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

package io.github.guerra24.voxel.client.kernel.world.block;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.resources.OBJLoader;
import io.github.guerra24.voxel.client.kernel.resources.models.ModelTexture;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;

public class BlocksResources {
	public static TexturedModel cubeIndesUP;
	public static TexturedModel cubeGrassUP;
	public static TexturedModel cubeStoneUP;
	public static TexturedModel cubeSandUP;
	public static TexturedModel cubeGlassUP;
	public static TexturedModel cubeDirtUP;
	public static TexturedModel cubeDiamondOreUP;
	public static TexturedModel cubeGoldOreUP;

	public static TexturedModel cubeIndesDOWN;
	public static TexturedModel cubeGrassDOWN;
	public static TexturedModel cubeStoneDOWN;
	public static TexturedModel cubeSandDOWN;
	public static TexturedModel cubeGlassDOWN;
	public static TexturedModel cubeDirtDOWN;
	public static TexturedModel cubeDiamondOreDOWN;
	public static TexturedModel cubeGoldOreDOWN;

	public static TexturedModel cubeIndesEAST;
	public static TexturedModel cubeGrassEAST;
	public static TexturedModel cubeStoneEAST;
	public static TexturedModel cubeSandEAST;
	public static TexturedModel cubeGlassEAST;
	public static TexturedModel cubeDirtEAST;
	public static TexturedModel cubeDiamondOreEAST;
	public static TexturedModel cubeGoldOreEAST;

	public static TexturedModel cubeIndesWEST;
	public static TexturedModel cubeGrassWEST;
	public static TexturedModel cubeStoneWEST;
	public static TexturedModel cubeSandWEST;
	public static TexturedModel cubeGlassWEST;
	public static TexturedModel cubeDirtWEST;
	public static TexturedModel cubeDiamondOreWEST;
	public static TexturedModel cubeGoldOreWEST;

	public static TexturedModel cubeIndesNORTH;
	public static TexturedModel cubeGrassNORTH;
	public static TexturedModel cubeStoneNORTH;
	public static TexturedModel cubeSandNORTH;
	public static TexturedModel cubeGlassNORTH;
	public static TexturedModel cubeDirtNORTH;
	public static TexturedModel cubeDiamondOreNORTH;
	public static TexturedModel cubeGoldOreNORTH;

	public static TexturedModel cubeIndesSOUTH;
	public static TexturedModel cubeGrassSOUTH;
	public static TexturedModel cubeStoneSOUTH;
	public static TexturedModel cubeSandSOUTH;
	public static TexturedModel cubeGlassSOUTH;
	public static TexturedModel cubeDirtSOUTH;
	public static TexturedModel cubeDiamondOreSOUTH;
	public static TexturedModel cubeGoldOreSOUTH;

	public static void createBlocks() {

		RawModel up = OBJLoader.loadObjModel("FACE_UP",
				Kernel.gameResources.loader);
		RawModel down = OBJLoader.loadObjModel("FACE_DOWN",
				Kernel.gameResources.loader);
		RawModel east = OBJLoader.loadObjModel("FACE_EAST",
				Kernel.gameResources.loader);
		RawModel west = OBJLoader.loadObjModel("FACE_WEST",
				Kernel.gameResources.loader);
		RawModel nort = OBJLoader.loadObjModel("FACE_NORTH",
				Kernel.gameResources.loader);
		RawModel south = OBJLoader.loadObjModel("FACE_SOUTH",
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
		ModelTexture texture7 = new ModelTexture(
				Kernel.gameResources.loader.loadTextureBlocks("GrassSide"));

		cubeIndesUP = new TexturedModel(up, texture0);
		cubeGrassUP = new TexturedModel(up, texture);
		cubeStoneUP = new TexturedModel(up, texture1);
		cubeSandUP = new TexturedModel(up, texture2);
		cubeGlassUP = new TexturedModel(up, texture3);
		cubeDirtUP = new TexturedModel(up, texture4);
		cubeDiamondOreUP = new TexturedModel(up, texture5);
		cubeGoldOreUP = new TexturedModel(up, texture6);

		cubeIndesDOWN = new TexturedModel(down, texture0);
		cubeGrassDOWN = new TexturedModel(down, texture4);
		cubeStoneDOWN = new TexturedModel(down, texture1);
		cubeSandDOWN = new TexturedModel(down, texture2);
		cubeGlassDOWN = new TexturedModel(down, texture3);
		cubeDirtDOWN = new TexturedModel(down, texture4);
		cubeDiamondOreDOWN = new TexturedModel(down, texture5);
		cubeGoldOreDOWN = new TexturedModel(down, texture6);

		cubeIndesEAST = new TexturedModel(east, texture0);
		cubeGrassEAST = new TexturedModel(east, texture7);
		cubeStoneEAST = new TexturedModel(east, texture1);
		cubeSandEAST = new TexturedModel(east, texture2);
		cubeGlassEAST = new TexturedModel(east, texture3);
		cubeDirtEAST = new TexturedModel(east, texture4);
		cubeDiamondOreEAST = new TexturedModel(east, texture5);
		cubeGoldOreEAST = new TexturedModel(east, texture6);

		cubeIndesWEST = new TexturedModel(west, texture0);
		cubeGrassWEST = new TexturedModel(west, texture7);
		cubeStoneWEST = new TexturedModel(west, texture1);
		cubeSandWEST = new TexturedModel(west, texture2);
		cubeGlassWEST = new TexturedModel(west, texture3);
		cubeDirtWEST = new TexturedModel(west, texture4);
		cubeDiamondOreWEST = new TexturedModel(west, texture5);
		cubeGoldOreWEST = new TexturedModel(west, texture6);

		cubeIndesNORTH = new TexturedModel(nort, texture0);
		cubeGrassNORTH = new TexturedModel(nort, texture7);
		cubeStoneNORTH = new TexturedModel(nort, texture1);
		cubeSandNORTH = new TexturedModel(nort, texture2);
		cubeGlassNORTH = new TexturedModel(nort, texture3);
		cubeDirtNORTH = new TexturedModel(nort, texture4);
		cubeDiamondOreNORTH = new TexturedModel(nort, texture5);
		cubeGoldOreNORTH = new TexturedModel(nort, texture6);

		cubeIndesSOUTH = new TexturedModel(south, texture0);
		cubeGrassSOUTH = new TexturedModel(south, texture7);
		cubeStoneSOUTH = new TexturedModel(south, texture1);
		cubeSandSOUTH = new TexturedModel(south, texture2);
		cubeGlassSOUTH = new TexturedModel(south, texture3);
		cubeDirtSOUTH = new TexturedModel(south, texture4);
		cubeDiamondOreSOUTH = new TexturedModel(south, texture5);
		cubeGoldOreSOUTH = new TexturedModel(south, texture6);
	}
}
