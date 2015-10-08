package io.github.guerra24.voxel.client.kernel.graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import io.github.guerra24.voxel.client.kernel.graphics.opengl.Display;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.PostProcessingShader;
import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector2f;

public class PostProcessingRenderer {

	private PostProcessingShader shader;
	private FrameBuffer post_fbo;
	private FrameBuffer post_fbo_depth;
	private final RawModel quad;

	public PostProcessingRenderer(Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new PostProcessingShader();
		shader.start();
		Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1));
		shader.loadTransformation(matrix);
		shader.connectTextureUnits();
		shader.stop();
		post_fbo = new FrameBuffer(false, Display.getWidth(), Display.getHeight());
		post_fbo_depth = new FrameBuffer(true, 512, 512);
	}

	public void render() {
		shader.start();
		shader.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, post_fbo.getDepthTexture());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, post_fbo_depth.getDepthTexture());
		VoxelGL33.glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public FrameBuffer getPost_fbo() {
		return post_fbo;
	}

	public FrameBuffer getPost_fbo_depth() {
		return post_fbo_depth;
	}
}
