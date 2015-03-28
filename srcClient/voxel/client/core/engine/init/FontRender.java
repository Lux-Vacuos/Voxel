package voxel.client.core.engine.init;

import voxel.client.core.engine.GameLoop;
import voxel.client.core.engine.color.Color4f;
import voxel.client.core.engine.resources.Font;
import voxel.client.core.engine.resources.Text;
import voxel.client.core.util.ConstantsClient;
import voxel.server.core.world.WorldManager;

public class FontRender {

	private static Font font;
	private static Color4f color4f = Color4f.WHITE;

	public static void renderFont() {
		font = new Font();
		font.loadFont("Default", "fonts/comic.png");
	}

	public static void renderText() {
		Text.renderString(font, "Debug Info", 0f, 1.35f,
				ConstantsClient.textSize, color4f);
		Text.renderString(font, "FPS: " + GameLoop.getFPS(), 0f, 1.30f,
				ConstantsClient.textSize, Color4f.WHITE);
		Text.renderString(font, "X:"
				+ (int) WorldManager.getMobManager().getPlayer().getX() + " Y:"
				+ (int) WorldManager.getMobManager().getPlayer().getY() + " Z:"
				+ (int) WorldManager.getMobManager().getPlayer().getZ(), 0f,
				1.25f, ConstantsClient.textSize, Color4f.WHITE);
		Text.renderString(font, "Rotx:"
				+ (int) WorldManager.getMobManager().getPlayer().getPitch()
				+ " RotY:"
				+ (int) WorldManager.getMobManager().getPlayer().getYaw()
				+ " RotZ:"
				+ (int) WorldManager.getMobManager().getPlayer().getRoll(), 0f,
				1.20f, ConstantsClient.textSize, Color4f.WHITE);
		Text.renderString(font, "Chunks: " + ConstantsClient.chunksLoaded
				+ " (" + ConstantsClient.chunksFrustum + ")", 0f, 1.20f,
				ConstantsClient.textSize, Color4f.WHITE);
	}
}
