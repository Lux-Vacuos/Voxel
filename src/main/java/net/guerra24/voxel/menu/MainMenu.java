package net.guerra24.voxel.menu;

import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.models.ModelTexture;
import net.guerra24.voxel.resources.models.RawModel;
import net.guerra24.voxel.resources.models.TexturedModel;

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
	public static void loadModels(GameResources gm) {
		RawModel planetModel = gm.getLoader().getObjLoader().loadObjModel("Planet", gm.getLoader());
		ModelTexture planetTexture = new ModelTexture(gm.getLoader().loadTextureEntity("Planet"));
		planet = new TexturedModel(planetModel, planetTexture);
	}
}
