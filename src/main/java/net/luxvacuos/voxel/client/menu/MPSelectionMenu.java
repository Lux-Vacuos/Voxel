package net.luxvacuos.voxel.client.menu;

import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.universal.util.vector.Vector2f;

public class MPSelectionMenu {

	private float xScale, yScale;

	private Button exitButton;
	private Button playButton;
	private String ip = "";

	public MPSelectionMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		exitButton = new Button(new Vector2f(655, 30), new Vector2f(215, 80), xScale, yScale);
		playButton = new Button(new Vector2f(410, 30), new Vector2f(215, 80), xScale, yScale);
	}

	public void render() {
		VectorsRendering.renderWindow("Multiplayer", "Roboto-Bold", 20 * xScale, 20 * yScale, 1240 * xScale,
				540 * yScale);
		VectorsRendering.renderText("IP:  ", "Roboto-Regular", 280 * xScale, 279 * yScale, 60 * yScale,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderWindow(20 * xScale, 570 * yScale, 1240 * xScale, 130 * yScale);
		while (Keyboard.next())
			ip = Keyboard.keyWritten(ip);
		VectorsRendering.renderSearchBox(ip, "Roboto-Regular", "Entypo", 340 * xScale, 260 * yScale, 600 * xScale,
				40 * yScale);

		exitButton.render("Back");
		playButton.render("Play");
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getPlayButton() {
		return playButton;
	}

	public String getIP() {
		return ip;
	}

}
