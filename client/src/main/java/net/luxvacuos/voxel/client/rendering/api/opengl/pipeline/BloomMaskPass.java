package net.luxvacuos.voxel.client.rendering.api.opengl.pipeline;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import net.luxvacuos.voxel.client.rendering.api.opengl.ImagePass;
import net.luxvacuos.voxel.client.rendering.api.opengl.ImagePassFBO;
import net.luxvacuos.voxel.client.rendering.api.opengl.RenderingPipeline;

public class BloomMaskPass extends ImagePass {

	public BloomMaskPass(int width, int height) {
		super(width, height);
	}

	@Override
	public void render(ImagePassFBO[] auxs, RenderingPipeline pipe) {
		auxs[1] = auxs[0];
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, pipe.getMainFBO().getData0Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, auxs[0].getTexture());
	}

}
