package io.github.guerra24.voxel.client.kernel.util;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetString;
import io.github.guerra24.voxel.client.kernel.Kernel;

import org.lwjgl.opengl.Display;

public class SystemInfo {
	public static void printSystemInfo() {

		Logger.log(Kernel.currentThread(), "Vendor: " + glGetString(GL_VENDOR));

		Logger.log(Kernel.currentThread(), "Renderer: "
				+ glGetString(GL_RENDERER));

		if (Display.getVersion() != null) {
			Logger.log(Kernel.currentThread(),
					"Driver Version: " + Display.getVersion());
		} else {
			Logger.warn(Kernel.currentThread(),
					"Could not detect driver version");
		}

	}

}
