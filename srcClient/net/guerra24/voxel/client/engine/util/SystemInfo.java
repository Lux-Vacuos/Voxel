package net.guerra24.voxel.client.engine.util;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.opengl.Display;

public class SystemInfo {
	public static void printSystemInfo() {

		String vendor = glGetString(GL_VENDOR);
		Logger.log("Vendor: " + vendor);

		String renderGl = glGetString(GL_RENDERER);
		Logger.log("Renderer: " + renderGl);

		if (Display.getVersion() != null) {
			Logger.log("Driver Version: " + Display.getVersion());
		} else {
			Logger.warn("Could not detect driver version");
		}

	}

}
