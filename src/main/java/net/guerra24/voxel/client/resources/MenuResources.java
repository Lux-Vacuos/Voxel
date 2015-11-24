package net.guerra24.voxel.client.resources;

import net.guerra24.voxel.client.resources.models.ModelTexture;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.resources.models.TexturedModel;

public class MenuResources {

	private static RawModel model1;
	private static int model1Text;

	private static RawModel modelBackground;
	private static int modelBackgroundText;

	private static TexturedModel model1Final;
	private static TexturedModel mainMenuBackFinal;

	static void load(GameResources gm) {
		Loader loader = gm.getLoader();
		model1 = loader.getObjLoader().loadObjModel("button", loader);
		model1Text = loader.loadTextureGui("button_1");
		model1Final = new TexturedModel(model1, new ModelTexture(model1Text));

		modelBackground = loader.getObjLoader().loadObjModel("mainMenuBackground", loader);
		modelBackgroundText = loader.loadTextureGui("mainMenuBackground");
		mainMenuBackFinal = new TexturedModel(modelBackground, new ModelTexture(modelBackgroundText));
	}

	public static TexturedModel getModel1Final() {
		return model1Final;
	}

	public static TexturedModel getMainMenuBackFinal() {
		return mainMenuBackFinal;
	}

}
