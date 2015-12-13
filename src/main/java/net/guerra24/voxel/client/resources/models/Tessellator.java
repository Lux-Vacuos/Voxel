package net.guerra24.voxel.client.resources.models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.shaders.TessellatorShader;
import net.guerra24.voxel.client.graphics.shaders.TessellatorShadowShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.block.IBlock;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;
import net.guerra24.voxel.universal.util.vector.Vector4f;
import net.guerra24.voxel.universal.util.vector.Vector8f;

public class Tessellator {

	private int vaoID, vboID0, vboID1, vboID2, vboID3, iboID, vboCap0 = 64, vboCap1 = 64, vboCap2 = 64, vboCap3 = 64,
			indicesCounter, iboCapacity = 64;

	private ByteBuffer buffer0, buffer1, buffer2, buffer3, ibo;

	private List<Vector3f> pos, normals;
	private List<Vector2f> texcoords;
	private List<Integer> indices;
	private List<Vector4f> data;
	private int texture;
	private boolean updated = false;

	private TessellatorShader shader;
	private TessellatorShadowShader shadowShader;

	public Tessellator(GameResources gm) {
		init(gm);
	}

	private void init(GameResources gm) {
		pos = new ArrayList<Vector3f>();
		texcoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		data = new ArrayList<Vector4f>();
		indices = new ArrayList<Integer>();
		shader = TessellatorShader.getInstance();
		shader.start();
		shader.conectTextureUnits();
		shader.loadProjectionMatrix(gm.getRenderer().getProjectionMatrix());
		shader.loadBiasMatrix(gm);
		shader.stop();
		shadowShader = TessellatorShadowShader.getInstance();
		shadowShader.start();
		shadowShader.loadProjectionMatrix(gm.getMasterShadowRenderer().getProjectionMatrix());
		shadowShader.stop();

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, null, GL_DYNAMIC_DRAW);
		vboID0 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID0);
		glBufferData(GL_ARRAY_BUFFER, vboCap0, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		vboID1 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID1);
		glBufferData(GL_ARRAY_BUFFER, vboCap1, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		vboID2 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID2);
		glBufferData(GL_ARRAY_BUFFER, vboCap2, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		vboID3 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID3);
		glBufferData(GL_ARRAY_BUFFER, vboCap3, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void begin(int texture) {
		this.texture = texture;
		pos.clear();
		texcoords.clear();
		normals.clear();
		data.clear();
	}

	public void vertex3f(Vector3f pos) {
		this.pos.add(pos);
	}

	public void texture2f(Vector2f texcoords) {
		this.texcoords.add(texcoords);
	}

	public void normal3f(Vector3f normals) {
		this.normals.add(normals);
	}

	public void indice(int ind) {
		this.indices.add(ind);
	}

	public void data4f(Vector4f data) {
		this.data.add(data);
	}

	public void end() {
		loadData(pos, texcoords, normals, data);
		updated = true;
	}

	public void draw(GameResources gm) {
		if (updated) {
			updateGlBuffers(vboID0, vboCap0, buffer0);
			updateGlBuffers(vboID1, vboCap1, buffer1);
			updateGlBuffers(vboID2, vboCap2, buffer2);
			updateGlBuffers(vboID3, vboCap3, buffer3);
			updateGLIBOBuffer();
			updated = false;
		}
		shader.start();
		shader.loadviewMatrix(gm.getCamera());
		shader.loadLightMatrix(gm);
		shader.loadSettings(VoxelVariables.useShadows);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, gm.getMasterShadowRenderer().getFbo().getTexture());
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
		shader.stop();
	}

	public void drawShadow(Camera camera) {
		glCullFace(GL_FRONT);
		shadowShader.start();
		shadowShader.loadviewMatrix(camera);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shadowShader.stop();
		glCullFace(GL_BACK);
	}

	public void loadData(List<Vector3f> pos, List<Vector2f> texcoords, List<Vector3f> normals, List<Vector4f> data) {
		buffer0 = BufferUtils.createByteBuffer((pos.size() * 3) * 4);
		buffer1 = BufferUtils.createByteBuffer((texcoords.size() * 2) * 4);
		buffer2 = BufferUtils.createByteBuffer((normals.size() * 3) * 4);
		buffer3 = BufferUtils.createByteBuffer((data.size() * 4) * 4);
		for (int i = 0; i < pos.size(); i++) {
			buffer0.putFloat(pos.get(i).x);
			buffer0.putFloat(pos.get(i).y);
			buffer0.putFloat(pos.get(i).z);
		}
		for (int i = 0; i < texcoords.size(); i++) {
			buffer1.putFloat(texcoords.get(i).x);
			buffer1.putFloat(texcoords.get(i).y);
		}
		for (int i = 0; i < normals.size(); i++) {
			buffer2.putFloat(normals.get(i).x);
			buffer2.putFloat(normals.get(i).y);
			buffer2.putFloat(normals.get(i).z);
		}
		for (int i = 0; i < data.size(); i++) {
			buffer3.putFloat(data.get(i).x);
			buffer3.putFloat(data.get(i).y);
			buffer3.putFloat(data.get(i).z);
			buffer3.putFloat(data.get(i).w);
		}
		buffer0.flip();
		buffer1.flip();
		buffer2.flip();
		buffer3.flip();
		updateIBO((pos.size() * 3) / 2);
	}

	public void updateIBO(int count) {
		indicesCounter = count;
		ibo = BufferUtils.createByteBuffer(count * 4);
		for (int i = 0, bytes = 0; bytes < count; i += 4, bytes += 6) {
			ibo.putInt(i);
			ibo.putInt(i + 1);
			ibo.putInt(i + 2);
			ibo.putInt(i + 2);
			ibo.putInt(i + 3);
			ibo.putInt(i + 0);
		}
		ibo.flip();
	}

	public void updateGlBuffers(int vbo, int vboCapacity, ByteBuffer data) {
		vboCapacity = data.capacity();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, data, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void updateGLIBOBuffer() {
		iboCapacity = ibo.capacity();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, ibo, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	public void generateCube(int x, int y, int z, int size, boolean top, boolean bottom, boolean left, boolean right,
			boolean front, boolean back, IBlock block, float light) {
		int id = block.getId();
		float l = light / 15f;
		if (top) {
			Vector8f texcoords = block.texCoordsUp();
			// top face
			vertex3f(new Vector3f(x, y + size, z + size));
			texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3f(0, 1, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z + size));
			texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3f(0, 1, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z));
			texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3f(0, 1, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x, y + size, z));
			texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3f(0, 1, 0));
			data4f(new Vector4f(id, l, 0, 0));

		}
		if (bottom) {
			Vector8f texcoords = block.texCoordsDown();
			// bottom face
			vertex3f(new Vector3f(x, y, z));
			texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3f(0, -1, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y, z));
			texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3f(0, -1, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y, z + size));
			texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3f(0, -1, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x, y, z + size));
			texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3f(0, -1, 0));
			data4f(new Vector4f(id, l, 0, 0));
		}

		if (back) {
			Vector8f texcoords = block.texCoordsBack();
			// back face
			vertex3f(new Vector3f(x, y, z + size));
			texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3f(0, 0, 1));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y, z + size));
			texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3f(0, 0, 1));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z + size));
			texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3f(0, 0, 1));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x, y + size, z + size));
			texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3f(0, 0, 1));
			data4f(new Vector4f(id, l, 0, 0));
		}
		if (front) {
			// front face
			Vector8f texcoords = block.texCoordsFront();
			vertex3f(new Vector3f(x, y + size, z));
			texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3f(0, 0, -1));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z));
			texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3f(0, 0, -1));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y, z));
			texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3f(0, 0, -1));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x, y, z));
			texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3f(0, 0, -1));
			data4f(new Vector4f(id, l, 0, 0));
		}
		if (right) {
			Vector8f texcoords = block.texCoordsRight();
			// right face
			vertex3f(new Vector3f(x, y, z));
			texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3f(-1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x, y, z + size));
			texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3f(-1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x, y + size, z + size));
			texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3f(-1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x, y + size, z));
			texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3f(-1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));
		}
		if (left) {
			Vector8f texcoords = block.texCoordsLeft();
			// left face
			vertex3f(new Vector3f(x + size, y, z + size));
			texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3f(1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y, z));
			texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3f(1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z));
			texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3f(1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z + size));
			texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3f(1, 0, 0));
			data4f(new Vector4f(id, l, 0, 0));
		}
	}

	public void cleanUp() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID0);
		glDeleteBuffers(vboID1);
		glDeleteBuffers(vboID2);
		glDeleteBuffers(vboID3);
		glDeleteBuffers(iboID);
	}

}
