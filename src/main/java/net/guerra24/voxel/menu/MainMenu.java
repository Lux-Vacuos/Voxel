package net.guerra24.voxel.menu;

import net.guerra24.voxel.util.vector.Vector2f;

public class MainMenu {

	private Button playButton;
	private Button exitButton;

	public MainMenu() {
		playButton = new Button(new Vector2f(20, 436), new Vector2f(320, 528));
		exitButton = new Button(new Vector2f(20, 324), new Vector2f(320, 416));
	}

	public Button getPlayButton() {
		return playButton;
	}

	public Button getExitButton() {
		return exitButton;
	}

}
