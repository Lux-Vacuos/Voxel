package voxel.client.core.engine.util;

import org.lwjgl.opengl.GLContext;

public class GLCaps {
	public static void checkGLCaps() {
		if (GLContext.getCapabilities().OpenGL11) {
			Logger.log("OpenGL 1.1 is supported");
		} else {
			System.exit(0);
		}
		if (GLContext.getCapabilities().OpenGL12) {
			Logger.log("OpenGL 1.2 is supported");
		} else {
			System.exit(0);
		}
		if (GLContext.getCapabilities().OpenGL13) {
			Logger.log("OpenGL 1.3 is supported");
		} else {
			System.exit(0);
		}
		if (GLContext.getCapabilities().OpenGL14) {
			Logger.log("OpenGL 1.4 is supported");
		} else {
			System.exit(0);
		}
		if (GLContext.getCapabilities().OpenGL15) {
			Logger.log("OpenGL 1.5 is supported");
		} else {
			System.exit(0);
		}
		if (GLContext.getCapabilities().OpenGL20) {
			Logger.log("OpenGL 2.0 is supported");
		} else {
			System.exit(0);
		}
		if (GLContext.getCapabilities().OpenGL21) {
			Logger.log("OpenGL 2.1 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL30) {
			Logger.log("OpenGL 3.0 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL31) {
			Logger.log("OpenGL 3.1 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL32) {
			Logger.log("OpenGL 3.2 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL33) {
			Logger.log("OpenGL 3.3 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL40) {
			Logger.log("OpenGL 4.0 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL41) {
			Logger.log("OpenGL 4.1 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL42) {
			Logger.log("OpenGL 4.2 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL43) {
			Logger.log("OpenGL 4.3 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL44) {
			Logger.log("OpenGL 4.4 is supported");
		} else {
		}
		if (GLContext.getCapabilities().OpenGL45) {
			Logger.log("OpenGL 4.5 is supported");
		} else {
		}
	}
}