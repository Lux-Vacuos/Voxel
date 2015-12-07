package net.guerra24.voxel.client.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.resources.models.FontType;
import net.guerra24.voxel.client.resources.models.GUIText;

public class TextHandler {

	private FontType font;

	private List<GUIText> activeText;

	public TextHandler(GameResources gm) {
		font = new FontType(gm.getLoader().loadTextureFont("tahoma"), new File("assets/fonts/tahoma.fnt"));
		activeText = new ArrayList<GUIText>();
	}

	public void loadActiveText() {
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public void addToActive(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove();
		}
		activeText.addAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public void removeFromActive(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove();
		}
		activeText.removeAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public void switchTo(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove();
		}
		activeText.clear();
		activeText.addAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public FontType getFont() {
		return font;
	}
}
