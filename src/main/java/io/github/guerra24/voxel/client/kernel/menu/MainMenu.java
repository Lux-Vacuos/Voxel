package io.github.guerra24.voxel.client.kernel.menu;

import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.models.ModelTexture;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;

/**
 * Main Menu
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class MainMenu {

	public static TexturedModel planet;

	/**
	 * Loads the Main Menu Models
	 * 
	 * @param gm
	 *            GameResources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void loadModels(GameControllers gm) {
		RawModel planetModel = gm.getLoader().getObjLoader().loadObjModel("Planet", gm.getLoader());
		ModelTexture planetTexture = new ModelTexture(gm.getLoader().loadTextureEntity("Planet"));
		planet = new TexturedModel(planetModel, planetTexture);
	}
}
