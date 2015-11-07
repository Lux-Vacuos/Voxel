package net.guerra24.voxel.client.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.TextMasterRenderer;
import net.guerra24.voxel.client.resources.models.FontType;
import net.guerra24.voxel.client.resources.models.GUIText;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class TextHandler {

	private FontType font;

	private List<GUIText> activeText;

	public TextHandler(GameResources gm) {
		font = new FontType(gm.getLoader().loadTextureFont("tahoma"), new File("assets/fonts/tahoma.fnt"));
		activeText = new ArrayList<GUIText>();
		init();
		loadActiveText(gm.getTextMasterRenderer());
	}

	private void init() {
		GUIText textVersion = new GUIText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build, 1,
				font, new Vector2f(0.002f, 0.97f), 1, false);
		textVersion.setColour(0.79f, 0.79f, 0.79f);
		GUIText textVersionApi = new GUIText("Voxel API " + VoxelVariables.apiVersion, 1, font,
				new Vector2f(0.002f, 0.94f), 1, false);
		textVersionApi.setColour(0.79f, 0.79f, 0.79f);
		activeText.add(textVersion);
		activeText.add(textVersionApi);
	}

	private void loadActiveText(TextMasterRenderer textMasterRenderer) {
		for (GUIText guiText : activeText) {
			guiText.add(textMasterRenderer);
		}
	}

	public void addToActive(List<GUIText> dest, TextMasterRenderer textMasterRenderer) {
		for (GUIText guiText : activeText) {
			guiText.remove(textMasterRenderer);
		}
		activeText.addAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add(textMasterRenderer);
		}
	}

	public void removeFromActive(List<GUIText> dest, TextMasterRenderer textMasterRenderer) {
		for (GUIText guiText : activeText) {
			guiText.remove(textMasterRenderer);
		}
		activeText.removeAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add(textMasterRenderer);
		}
	}

	public void switchTo(List<GUIText> dest, TextMasterRenderer textMasterRenderer) {
		for (GUIText guiText : activeText) {
			guiText.remove(textMasterRenderer);
		}
		activeText.clear();
		activeText.addAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add(textMasterRenderer);
		}
	}

	public FontType getFont() {
		return font;
	}
}
