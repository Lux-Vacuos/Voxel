package voxel.client.engine.util;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;

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

	public static void chechOpenGl32() {
		if (GLContext.getCapabilities().OpenGL33) {
			Logger.log("OpenGL 3.3 is supported");
		} else {
			Logger.error("OpenGL 3.3 is not supported");
			Logger.error("Check Log");
			System.exit(-1);
		}
	}
}
