package net.guerra24.voxel.client.menu;

import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.FontType;
import net.guerra24.voxel.client.resources.models.GUIText;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class GameSP {
	private List<GUIText> texts;

	public GameSP(GameResources gm) {
		FontType font = gm.getTextHandler().getFont();
		texts = new ArrayList<GUIText>();
		GUIText textVersion = new GUIText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build, 1,
				font, new Vector2f(0.002f, 0.97f), 1, false);
		textVersion.setColour(0.79f, 0.79f, 0.79f);
		texts.add(textVersion);
	}

	public void load(GameResources gm) {
		gm.getTextHandler().switchTo(texts);
	}

}
