package net.guerra24.voxel.client.engine.world.blocks;

import net.guerra24.voxel.client.engine.render.textures.ModelTexture;
import net.guerra24.voxel.client.engine.resources.Loader;
import net.guerra24.voxel.client.engine.resources.OBJLoader;
import net.guerra24.voxel.client.engine.resources.models.RawModel;
import net.guerra24.voxel.client.engine.resources.models.TexturedModel;

public class Blocks {

	public static TexturedModel cubeGrass;
	public static TexturedModel cubeStone;
	public static TexturedModel cubeSand;
	public static TexturedModel cubeGlass;
	public static Loader loader;

	public static void createBlocks() {

		loader = new Loader();

		RawModel model = OBJLoader.loadObjModel("Block", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("Grass"));
		ModelTexture texture1 = new ModelTexture(
				loader.loadTexture("Cobblestone"));
		ModelTexture texture2 = new ModelTexture(loader.loadTexture("Sand"));
		ModelTexture texture3 = new ModelTexture(loader.loadTexture("Glass"));
		cubeGrass = new TexturedModel(model, texture);
		cubeStone = new TexturedModel(model, texture1);
		cubeSand = new TexturedModel(model, texture2);
		cubeGlass = new TexturedModel(model, texture3);
	}
}
