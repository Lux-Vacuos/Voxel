package net.guerra24.voxel.client.graphics;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.glCullFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import net.guerra24.voxel.client.graphics.shaders.ShadowShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.block.BlockEntity;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

public class MasterShadowRenderer {

	private Map<TexturedModel, List<BlockEntity>> blockEntities = new HashMap<TexturedModel, List<BlockEntity>>();
	private ShadowShader shader;
	private ShadowRenderer renderer;
	private FrameBuffer fbo;
	private Matrix4f projectionMatrix;

	/**
	 * Constructor, Initializes the OpenGL code, creates the projection matrix,
	 * entity renderer and skybox renderer
	 * 
	 * @param loader
	 *            Game Loader
	 */
	public MasterShadowRenderer() {
		shader = new ShadowShader();
		projectionMatrix = Maths.orthographic(-30, 30, -30, 30, -100, 100);
		renderer = new ShadowRenderer(shader, projectionMatrix);
		fbo = new FrameBuffer(true, 4096, 4096);
	}

	public void being() {
		fbo.begin(4096, 4096);
	}

	public void end() {
		fbo.end();
	}

	/**
	 * Render the Chunk
	 * 
	 * @param cubes1temp
	 *            A list of BlockEntity
	 * @param lights1
	 *            A list of Lights
	 * @param camera
	 *            A Camera
	 */
	public void renderChunk(Queue<Object> cubes, GameResources gm) {
		for (Object entity : cubes) {
			if (entity instanceof BlockEntity)
				processBlockEntity((BlockEntity) entity);
		}
		renderBlocks(gm);
	}

	/**
	 * Chunk Rendering PipeLine
	 * 
	 * @param lights
	 *            A list of lights
	 * @param camera
	 *            A Camera
	 */
	private void renderBlocks(GameResources gm) {
		glCullFace(GL_FRONT);
		shader.start();
		shader.loadviewMatrix(gm.getSun_Camera());
		renderer.renderBlockEntity(blockEntities, gm);
		shader.stop();
		blockEntities.clear();
		glCullFace(GL_BACK);
	}

	/**
	 * Add the BlockEntity to the batcher map
	 * 
	 * @param BlockEntity
	 *            An Entity
	 */
	private void processBlockEntity(BlockEntity entity) {
		TexturedModel entityModel = entity.getModel();
		List<BlockEntity> batch = blockEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<BlockEntity> newBatch = new ArrayList<BlockEntity>();
			newBatch.add(entity);
			blockEntities.put(entityModel, newBatch);
		}
	}

	public FrameBuffer getFbo() {
		return fbo;
	}

	/**
	 * Clear the Shader
	 */
	public void cleanUp() {
		shader.cleanUp();
		fbo.cleanUp();
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

}
