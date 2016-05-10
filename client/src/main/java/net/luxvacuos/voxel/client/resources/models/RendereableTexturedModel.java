package net.luxvacuos.voxel.client.resources.models;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.EntityShader;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.util.Maths;

public class RendereableTexturedModel {

	private TexturedModel model;
	private Vector3f pos;
	public float rotX, rotY, rotZ, scale = 1;

	public RendereableTexturedModel(Vector3f pos, TexturedModel tex) {
		this.pos = pos;
		model = tex;
	}

	public void render(EntityShader shader) {
		shader.start();
		shader.loadProjectionMatrix(GameResources.instance().getRenderer().getProjectionMatrix());
		shader.loadviewMatrix(GameResources.instance().getCamera());
		shader.loadLightMatrix(GameResources.instance());
		shader.useShadows(VoxelVariables.useShadows);
		RawModel rawmodel = model.getRawModel();
		glBindVertexArray(rawmodel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, GameResources.instance().getMasterShadowRenderer().getFbo().getTexture());
		shader.loadEntityLight(1);
		shader.loadTransformationMatrix(
				Maths.createTransformationMatrix(pos, rotX, rotY, rotZ, scale));
		glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		shader.stop();
	}
}
