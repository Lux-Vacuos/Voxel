package net.guerra24.voxel.universal.resources;

import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.ModelTexture;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.resources.models.TexturedModel;

public class UniversalResources {

	public static TexturedModel player;

	public static void loadUniversalResources(GameResources gm) {
		ModelTexture texture = new ModelTexture(gm.getLoader().loadTextureEntity("player"));
		RawModel model = gm.getLoader().getObjLoader().loadObjModel("player", gm.getLoader());
		player = new TexturedModel(model, texture);
	}

}
