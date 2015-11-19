package net.guerra24.voxel.client.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.graphics.TextMasterRenderer;
import net.guerra24.voxel.client.resources.models.FontType;
import net.guerra24.voxel.client.resources.models.GUIText;

public class TextHandler {

	private FontType font;

	private List<GUIText> activeText;
	private TextMasterRenderer textMasterRenderer;

	public TextHandler(GameResources gm) {
		font = new FontType(gm.getLoader().loadTextureFont("tahoma"), new File("assets/fonts/tahoma.fnt"));
		activeText = new ArrayList<GUIText>();
		textMasterRenderer = gm.getTextMasterRenderer();
	}

	public void loadActiveText() {
		for (GUIText guiText : activeText) {
			guiText.add(textMasterRenderer);
		}
	}

	public void addToActive(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove(textMasterRenderer);
		}
		activeText.addAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add(textMasterRenderer);
		}
	}

	public void removeFromActive(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove(textMasterRenderer);
		}
		activeText.removeAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add(textMasterRenderer);
		}
	}

	public void switchTo(List<GUIText> dest) {
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
