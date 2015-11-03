package net.guerra24.voxel.menu;

import net.guerra24.voxel.core.VoxelVariables;
import net.guerra24.voxel.util.vector.Vector2f;

public class OptionsMenu {

	private Button exitButton;

	public OptionsMenu() {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		float yScale = height / 720f;
		float xScale = width / 1280f;
		exitButton = new Button(new Vector2f(520 * xScale, 70 * yScale), new Vector2f(400, 100));
	}

	public Button getExitButton() {
		return exitButton;
	}
}
