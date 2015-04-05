package net.guerra24.voxel.client.engine.util;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetString;

public class SystemInfo {
	public static void printSystemInfo() {

		String vendor = glGetString(GL_VENDOR);
		Logger.log("Vendor: " + vendor);

		String renderGl = glGetString(GL_RENDERER);
		Logger.log("Renderer: " + renderGl);
	}
}
