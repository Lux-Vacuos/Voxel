package net.luxvacuos.voxel.client.menu;

import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.universal.util.vector.Vector2f;

public class MPLoadingWorld {

	private float xScale, yScale;

	private Button exitButton;

	public MPLoadingWorld(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		exitButton = new Button(new Vector2f(533, 220), new Vector2f(215, 80), xScale, yScale);
	}

	public void render(String message) {
		VectorsRendering.renderWindow("Loading Multiplayer", "Roboto-Bold", 20 * xScale, 20 * yScale, 1240 * xScale,
				540 * yScale);
		VectorsRendering.renderText(message, "Roboto-Regular", 40 * xScale, 300 * yScale, 60 * yScale,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderWindow(20 * xScale, 570 * yScale, 1240 * xScale, 130 * yScale);
		exitButton.render("Cancel");
	}

	public Button getExitButton() {
		return exitButton;
	}

}
