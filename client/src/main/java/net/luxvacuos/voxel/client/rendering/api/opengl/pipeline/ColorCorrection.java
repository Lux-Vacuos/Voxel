package net.luxvacuos.voxel.client.rendering.api.opengl.pipeline;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.GL_TEXTURE7;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import net.luxvacuos.voxel.client.rendering.api.opengl.IDeferredPipeline;
import net.luxvacuos.voxel.client.rendering.api.opengl.DeferredPass;
import net.luxvacuos.voxel.client.rendering.api.opengl.FBO;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.CubeMapTexture;

public class ColorCorrection extends DeferredPass {


	public ColorCorrection(String name, int width, int height) {
		super(name, width, height);
	}

	@Override
	public void render(FBO[] auxs, IDeferredPipeline pipe, CubeMapTexture environmentMap) {
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, auxs[0].getTexture());
		glActiveTexture(GL_TEXTURE7);
		glBindTexture(GL_TEXTURE_2D, auxs[1].getTexture());
	}

}
