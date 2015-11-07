package net.guerra24.voxel.client.menu;

import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.ButtonModel;
import net.guerra24.voxel.client.resources.models.ModelTexture;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class MainMenu {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;
	private List<ButtonModel> list;

	private RawModel model1;
	private int model1Text;

	private RawModel modelBackground;
	private int modelBackgroundText;

	private TexturedModel model1Final;
	private TexturedModel mainMenuBackFinal;

	public MainMenu(Loader loader) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		float yScale = height / 720f;
		float xScale = width / 1280f;
		playButton = new Button(new Vector2f(177 * xScale, 532 * yScale), new Vector2f(215, 100));
		exitButton = new Button(new Vector2f(177 * xScale, 224 * yScale), new Vector2f(215, 100));
		optionsButton = new Button(new Vector2f(177 * xScale, 376 * yScale), new Vector2f(215, 100));
		model1 = loader.getObjLoader().loadObjModel("button", loader);
		model1Text = loader.loadTextureGui("button_1");
		model1Final = new TexturedModel(model1, new ModelTexture(model1Text));

		modelBackground = loader.getObjLoader().loadObjModel("mainMenuBackground", loader);
		modelBackgroundText = loader.loadTextureGui("mainMenuBackground");
		mainMenuBackFinal = new TexturedModel(modelBackground, new ModelTexture(modelBackgroundText));

		list = new ArrayList<ButtonModel>();
		list.add(new ButtonModel(model1Final, new Vector3f(-0.7f, 0.4f, 0), new Vector3f(90, 0, 0), 0.07f));
		list.add(new ButtonModel(model1Final, new Vector3f(-0.7f, 0.1f, 0), new Vector3f(90, 0, 0), 0.07f));
		list.add(new ButtonModel(model1Final, new Vector3f(-0.7f, -0.2f, 0), new Vector3f(90, 0, 0), 0.07f));
		list.add(new ButtonModel(model1Final, new Vector3f(-1.4f, -3.95f, 0), new Vector3f(90, 0, 00), 0.07f));
		list.add(new ButtonModel(mainMenuBackFinal, new Vector3f(0.1f, -0.92f, -1f), new Vector3f(90, 0, 0), 4));
	}

	public Button getPlayButton() {
		return playButton;
	}

	public Button getExitButton() {
		return exitButton;
	}

	public List<ButtonModel> getList() {
		return list;
	}

	public Button getOptionsButton() {
		return optionsButton;
	}

}
