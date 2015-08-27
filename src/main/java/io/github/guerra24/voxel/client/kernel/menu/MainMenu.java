package io.github.guerra24.voxel.client.kernel.menu;

import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.OBJLoader;
import io.github.guerra24.voxel.client.kernel.resources.models.ModelTexture;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;

/**
 * Main Menu
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.2 Build-58
 * @since 0.0.2 Build-58
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
	public static void loadModels(GameResources gm) {
		RawModel planetModel = OBJLoader.loadObjModel("Planet", gm.loader);
		ModelTexture planetTexture = new ModelTexture(
				gm.loader.loadTextureEntity("Planet"));
		planet = new TexturedModel(planetModel, planetTexture);
	}
}
