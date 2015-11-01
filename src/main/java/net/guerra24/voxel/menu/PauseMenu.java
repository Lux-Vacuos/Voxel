package net.guerra24.voxel.menu;

import net.guerra24.voxel.util.vector.Vector2f;

public class PauseMenu {
	private Button backToMain;

	public PauseMenu() {
		backToMain = new Button(new Vector2f(520, 70), new Vector2f(760, 145));
	}

	public Button getBackToMain() {
		return backToMain;
	}
}
