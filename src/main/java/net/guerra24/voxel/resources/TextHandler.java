package net.guerra24.voxel.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.core.VoxelVariables;
import net.guerra24.voxel.graphics.TextMasterRenderer;
import net.guerra24.voxel.resources.models.FontType;
import net.guerra24.voxel.resources.models.GUIText;
import net.guerra24.voxel.util.vector.Vector2f;

public class TextHandler {

	private FontType font;

	private List<GUIText> mainMenuText;
	private List<GUIText> activeText;

	public TextHandler(Loader loader) {
		font = new FontType(loader.loadTextureFont("tahoma"), new File("assets/fonts/tahoma.fnt"));
		mainMenuText = new ArrayList<GUIText>();
		activeText = new ArrayList<GUIText>();
		initManMenuText();
	}

	private void initManMenuText() {
		GUIText textVersion = new GUIText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build, 1,
				font, new Vector2f(0.002f, 0.97f), 1, false);
		textVersion.setColour(0.79f, 0.79f, 0.79f);
		GUIText textVersionApi = new GUIText("Voxel API " + VoxelVariables.apiVersion, 1, font,
				new Vector2f(0.002f, 0.94f), 1, false);
		textVersionApi.setColour(0.79f, 0.79f, 0.79f);
		GUIText macWarning = new GUIText("Voxel is running in OS X, some things do not work well", 1, font,
				new Vector2f(0, 0.2f), 1, true);
		macWarning.setColour(1, 0, 0);
		GUIText playButton = new GUIText("Play", 3.8f, font, new Vector2f(0, 0.285f), 1, true);
		playButton.setColour(0.95f, 0.95f, 0.95f);
		GUIText exitButton = new GUIText("Exit", 3.8f, font, new Vector2f(0, 0.585f), 1, true);
		exitButton.setColour(0.95f, 0.95f, 0.95f);
		mainMenuText.add(textVersion);
		mainMenuText.add(textVersionApi);
		mainMenuText.add(playButton);
		mainMenuText.add(exitButton);
		if (VoxelVariables.runningOnMac)
			mainMenuText.add(macWarning);
	}

	public void switchTo(List<GUIText> source, TextMasterRenderer textMasterRenderer) {
		for (GUIText text : activeText) {
			text.remove(textMasterRenderer);
		}
		activeText.clear();
		activeText.addAll(source);
		for (GUIText text : activeText) {
			text.add(textMasterRenderer);
		}
	}

	public List<GUIText> getMainMenuText() {
		return mainMenuText;
	}
}
