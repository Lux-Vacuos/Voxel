/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.resources.models;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glDeleteQueries;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glGenQueries;
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
import net.guerra24.voxel.client.graphics.shaders.TessellatorBasicShader;
import net.guerra24.voxel.client.graphics.shaders.TessellatorShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.block.BlockBase;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;
import net.guerra24.voxel.universal.util.vector.Vector4f;
import net.guerra24.voxel.universal.util.vector.Vector8f;

public class Tessellator {

	private int vaoID, vboID0, vboID1, vboID2, vboID3, vboID4, vboID5, iboID, vboCapacity = 64, indicesCounter,
			iboCapacity = 64;

	private ByteBuffer buffer0, buffer1, buffer2, buffer3, buffer4, buffer5, ibo;

	private List<Vector3f> pos, normals;
	private List<Vector2f> texcoords;
	private List<Integer> indices;
	private List<Vector4f> data;
	private List<Vector3f> tangent;
	private List<Vector3f> bitangent;
	private int texture;
	private int normalMap;
	private int heightMap;
	private int occlusion;
	private boolean updated = false;

	private TessellatorShader shader;
	private TessellatorBasicShader basicShader;

	private Matrix4f orthoProjectionMatrix;

	public Tessellator(GameResources gm) {
		init(gm);
	}

	private void init(GameResources gm) {
		pos = new ArrayList<Vector3f>();
		texcoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		data = new ArrayList<Vector4f>();
		indices = new ArrayList<Integer>();
		tangent = new ArrayList<Vector3f>();
		bitangent = new ArrayList<Vector3f>();
		this.orthoProjectionMatrix = gm.getMasterShadowRenderer().getProjectionMatrix();
		shader = TessellatorShader.getInstance();
		shader.start();
		shader.conectTextureUnits();
		shader.loadProjectionMatrix(gm.getRenderer().getProjectionMatrix());
		shader.loadBiasMatrix(gm);
		shader.stop();
		basicShader = TessellatorBasicShader.getInstance();
		basicShader.start();
		basicShader.loadProjectionMatrix(gm.getMasterShadowRenderer().getProjectionMatrix());
		basicShader.stop();

		occlusion = glGenQueries();

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, null, GL_DYNAMIC_DRAW);

		vboID0 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID0);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID1 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID1);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID2 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID2);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID3 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID3);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID4 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID4);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(4);
		glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID5 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID5);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(5);
		glVertexAttribPointer(5, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void begin(int texture, int normalMap, int heightMap) {
		this.texture = texture;
		this.normalMap = normalMap;
		this.heightMap = heightMap;
		pos.clear();
		texcoords.clear();
		normals.clear();
		data.clear();
		tangent.clear();
		bitangent.clear();
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

	public void tangent3f(Vector3f tangent) {
		this.tangent.add(tangent);
	}

	public void bitangent3f(Vector3f bitangent) {
		this.bitangent.add(bitangent);
	}

	public void indice(int ind) {
		this.indices.add(ind);
	}

	public void data4f(Vector4f data) {
		this.data.add(data);
	}

	public void end() {
		loadData(pos, texcoords, normals, data, tangent, bitangent);
		pos.clear();
		texcoords.clear();
		normals.clear();
		data.clear();
		tangent.clear();
		bitangent.clear();
		updated = true;
	}

	public void draw(GameResources gm) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGlBuffers(vboID3, vboCapacity, buffer3);
			updateGlBuffers(vboID4, vboCapacity, buffer4);
			updateGlBuffers(vboID5, vboCapacity, buffer5);
			updateGLIBOBuffer();
			updated = false;
		}
		shader.start();
		shader.loadviewMatrix(gm.getCamera());
		shader.loadLightMatrix(gm);
		shader.loadSettings(VoxelVariables.useShadows, VoxelVariables.useParallax);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, gm.getMasterShadowRenderer().getFbo().getTexture());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, normalMap);
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, heightMap);
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glBindVertexArray(0);
		shader.stop();
	}

	public void drawShadow(Camera camera) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGlBuffers(vboID3, vboCapacity, buffer3);
			updateGlBuffers(vboID4, vboCapacity, buffer4);
			updateGlBuffers(vboID5, vboCapacity, buffer5);
			updateGLIBOBuffer();
			updated = false;
		}
		glCullFace(GL_FRONT);
		basicShader.start();
		basicShader.loadviewMatrix(camera);
		basicShader.loadProjectionMatrix(orthoProjectionMatrix);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		basicShader.stop();
		glCullFace(GL_BACK);
	}

	public void drawOcclusion(Camera camera, Matrix4f projectionMatrix) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGlBuffers(vboID3, vboCapacity, buffer3);
			updateGlBuffers(vboID4, vboCapacity, buffer4);
			updateGlBuffers(vboID5, vboCapacity, buffer5);
			updateGLIBOBuffer();
			updated = false;
		}
		basicShader.start();
		basicShader.loadviewMatrix(camera);
		basicShader.loadProjectionMatrix(projectionMatrix);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		basicShader.stop();
	}

	public void loadData(List<Vector3f> pos, List<Vector2f> texcoords, List<Vector3f> normals, List<Vector4f> data,
			List<Vector3f> tangent, List<Vector3f> bitangent) {
		buffer0 = BufferUtils.createByteBuffer((pos.size() * 3) * 4);
		buffer1 = BufferUtils.createByteBuffer((texcoords.size() * 2) * 4);
		buffer2 = BufferUtils.createByteBuffer((normals.size() * 3) * 4);
		buffer3 = BufferUtils.createByteBuffer((data.size() * 4) * 4);
		buffer4 = BufferUtils.createByteBuffer((tangent.size() * 3) * 4);
		buffer5 = BufferUtils.createByteBuffer((bitangent.size() * 3) * 4);
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
		for (int i = 0; i < tangent.size(); i++) {
			buffer4.putFloat(tangent.get(i).x);
			buffer4.putFloat(tangent.get(i).y);
			buffer4.putFloat(tangent.get(i).z);
		}
		for (int i = 0; i < bitangent.size(); i++) {
			buffer5.putFloat(bitangent.get(i).x);
			buffer5.putFloat(bitangent.get(i).y);
			buffer5.putFloat(bitangent.get(i).z);
		}
		buffer0.flip();
		buffer1.flip();
		buffer2.flip();
		buffer3.flip();
		buffer4.flip();
		buffer5.flip();
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
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, GL_DYNAMIC_DRAW);
		glBufferSubData(GL_ARRAY_BUFFER, 0, data);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void updateGLIBOBuffer() {
		iboCapacity = ibo.capacity();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, GL_DYNAMIC_DRAW);
		glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, ibo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	public void generateCube(float x, float y, float z, float size, boolean top, boolean bottom, boolean left,
			boolean right, boolean front, boolean back, BlockBase block, float light) {
		int id = block.getId();
		float l = light / 15f;
		if (id != Block.Air.getId()) {
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

				Vector3f edge1 = Vector3f.sub(new Vector3f(x + size, y + size, z + size),
						new Vector3f(x, y + size, z + size), null);
				Vector3f edge2 = Vector3f.sub(new Vector3f(x + size, y + size, z), new Vector3f(x, y + size, z + size),
						null);
				Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
						new Vector2f(texcoords.getZ(), texcoords.getW()), null);
				Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
						new Vector2f(texcoords.getZ(), texcoords.getW()), null);

				float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

				Vector3f tangent = new Vector3f();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				Vector3f bitangent = new Vector3f();

				bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
				bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
				bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
				bitangent.normalise();

				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);

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

				Vector3f edge1 = Vector3f.sub(new Vector3f(x + size, y, z), new Vector3f(x, y, z), null);

				Vector3f edge2 = Vector3f.sub(new Vector3f(x + size, y, z + size), new Vector3f(x, y, z), null);

				Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
						new Vector2f(texcoords.getZ(), texcoords.getW()), null);

				Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
						new Vector2f(texcoords.getZ(), texcoords.getW()), null);

				float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

				Vector3f tangent = new Vector3f();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				Vector3f bitangent = new Vector3f();

				bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
				bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
				bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
				bitangent.normalise();

				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);
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

				Vector3f edge1 = Vector3f.sub(new Vector3f(x + size, y, z + size), new Vector3f(x, y, z + size), null);

				Vector3f edge2 = Vector3f.sub(new Vector3f(x + size, y + size, z + size), new Vector3f(x, y, z + size),
						null);

				Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
						new Vector2f(texcoords.getX(), texcoords.getY()), null);

				Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
						new Vector2f(texcoords.getX(), texcoords.getY()), null);

				float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

				Vector3f tangent = new Vector3f();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				Vector3f bitangent = new Vector3f();

				bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
				bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
				bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
				bitangent.normalise();

				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);

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

				Vector3f edge1 = Vector3f.sub(new Vector3f(x + size, y + size, z), new Vector3f(x, y + size, z), null);

				Vector3f edge2 = Vector3f.sub(new Vector3f(x + size, y, z), new Vector3f(x, y + size, z), null);

				Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
						new Vector2f(texcoords.getZ(), texcoords.getW()), null);

				Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
						new Vector2f(texcoords.getZ(), texcoords.getW()), null);

				float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

				Vector3f tangent = new Vector3f();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				Vector3f bitangent = new Vector3f();

				bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
				bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
				bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
				bitangent.normalise();

				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);

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

				Vector3f edge1 = Vector3f.sub(new Vector3f(x, y, z + size), new Vector3f(x, y, z), null);

				Vector3f edge2 = Vector3f.sub(new Vector3f(x, y + size, z + size), new Vector3f(x, y, z), null);

				Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getX(), texcoords.getY()),
						new Vector2f(texcoords.getK(), texcoords.getL()), null);

				Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getZ(), texcoords.getW()),
						new Vector2f(texcoords.getK(), texcoords.getL()), null);

				float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

				Vector3f tangent = new Vector3f();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				Vector3f bitangent = new Vector3f();

				bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
				bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
				bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
				bitangent.normalise();

				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);

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

				Vector3f edge1 = Vector3f.sub(new Vector3f(x + size, y, z), new Vector3f(x + size, y, z + size), null);

				Vector3f edge2 = Vector3f.sub(new Vector3f(x + size, y + size, z), new Vector3f(x + size, y, z + size),
						null);

				Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getX(), texcoords.getY()),
						new Vector2f(texcoords.getK(), texcoords.getL()), null);

				Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getZ(), texcoords.getW()),
						new Vector2f(texcoords.getK(), texcoords.getL()), null);

				float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

				Vector3f tangent = new Vector3f();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				Vector3f bitangent = new Vector3f();

				bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
				bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
				bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
				bitangent.normalise();

				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);
				bitangent3f(bitangent);

			}
		}
	}

	public void cleanUp() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID0);
		glDeleteBuffers(vboID1);
		glDeleteBuffers(vboID2);
		glDeleteBuffers(vboID3);
		glDeleteBuffers(vboID4);
		glDeleteBuffers(vboID5);
		glDeleteBuffers(iboID);
		glDeleteQueries(occlusion);
		pos.clear();
		texcoords.clear();
		normals.clear();
		data.clear();
		tangent.clear();
		bitangent.clear();
	}

	public int getOcclusion() {
		return occlusion;
	}

}
