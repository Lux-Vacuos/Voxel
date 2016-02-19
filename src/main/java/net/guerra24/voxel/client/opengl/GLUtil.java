package net.guerra24.voxel.client.opengl;

import org.lwjgl.opengl.GL11;

public class GLUtil {

	private GLUtil() {
	}

	public static int getTextureMaxSize() {
		return GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
	}

}
