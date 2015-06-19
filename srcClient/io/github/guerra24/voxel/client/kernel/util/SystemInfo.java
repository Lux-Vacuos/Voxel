package io.github.guerra24.voxel.client.kernel.util;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.opengl.Display;

public class SystemInfo {
	public static void printSystemInfo() {

		Logger.log(Thread.currentThread(), "Vendor: " + glGetString(GL_VENDOR));

		Logger.log(Thread.currentThread(), "Renderer: "
				+ glGetString(GL_RENDERER));

		if (Display.getVersion() != null) {
			Logger.log(Thread.currentThread(),
					"Driver Version: " + Display.getVersion());
		} else {
			Logger.warn(Thread.currentThread(),
					"Could not detect driver version");
		}

	}

}
