package net.guerra24.voxel.client.kernel.render.types;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import net.guerra24.voxel.client.kernel.DisplayManager;
import net.guerra24.voxel.client.kernel.Engine;
import net.guerra24.voxel.client.kernel.entities.types.Camera;
import net.guerra24.voxel.client.kernel.render.shaders.types.SkyboxShader;
import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.RawModel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SkyboxRenderer {

	private static final float SIZE = 500f;

	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE,
			-SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE,
			SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE };

	private static String[] TEXTURE_FILES = { "day/right", "day/left",
			"day/top", "day/bottom", "day/front", "day/back" };
	private static String[] NIGHT_TEXTURE_FILES = { "night/right",
			"night/left", "night/top", "night/bottom", "night/front",
			"night/back" };

	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time = 0;

	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Camera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFog(r, g, b);
		glBindVertexArray(cube.getVaoID());
		glEnableVertexAttribArray(0);
		bindTextures();
		glDrawArrays(GL_TRIANGLES, 0, cube.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	private void bindTextures() {
		time += DisplayManager.getFrameTimeSeconds() * 100;
		time %= 24000;
		int texture1;
		int texture2;
		float blendFactor;
		if (time >= 0 && time < 5000) {
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - 0) / (5000 - 0);
			Engine.gameResources.sun.setPosition(new Vector3f(-7000, -10000f,
					-7000));
		} else if (time >= 5000 && time < 8000) {
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - 5000) / (8000 - 5000);
			Engine.gameResources.sun
					.setPosition(new Vector3f(-7000, 0f, -7000));
		} else if (time >= 8000 && time < 21000) {
			texture1 = texture;
			texture2 = texture;
			blendFactor = (time - 8000) / (21000 - 8000);
			Engine.gameResources.sun.setPosition(new Vector3f(-7000, 10000f,
					-7000));
		} else {
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - 21000) / (24000 - 21000);
			Engine.gameResources.sun
					.setPosition(new Vector3f(-7000, 0f, -7000));
		}

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}

}
