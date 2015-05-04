package net.guerra24.voxel.client.engine.world;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.render.textures.ModelTexture;
import net.guerra24.voxel.client.engine.resources.OBJLoader;
import net.guerra24.voxel.client.engine.resources.models.RawModel;
import net.guerra24.voxel.client.engine.resources.models.TexturedModel;

public class Blocks {

	public static TexturedModel cubeIndes;
	public static TexturedModel cubeGrass;
	public static TexturedModel cubeStone;
	public static TexturedModel cubeSand;
	public static TexturedModel cubeGlass;
	public static TexturedModel cubeDirt;
	public static TexturedModel cubeDiamondOre;
	public static TexturedModel cubeGoldOre;

	public static void createBlocks() {

		RawModel model = OBJLoader.loadObjModel("Block", Engine.loader);
		ModelTexture texture0 = new ModelTexture(
				Engine.loader.loadTextureBlocks("Indes"));
		ModelTexture texture = new ModelTexture(
				Engine.loader.loadTextureBlocks("Grass"));
		ModelTexture texture1 = new ModelTexture(
				Engine.loader.loadTextureBlocks("Stone"));
		ModelTexture texture2 = new ModelTexture(
				Engine.loader.loadTextureBlocks("Sand"));
		ModelTexture texture3 = new ModelTexture(
				Engine.loader.loadTextureBlocks("Glass"));
		ModelTexture texture4 = new ModelTexture(
				Engine.loader.loadTextureBlocks("Dirt"));
		ModelTexture texture5 = new ModelTexture(
				Engine.loader.loadTextureBlocks("Diamond-Ore"));
		ModelTexture texture6 = new ModelTexture(
				Engine.loader.loadTextureBlocks("Gold-Ore"));
		// Block Mix texture and model
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
