/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.rendering.api.opengl;

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
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
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

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.igl.vector.Vector4d;
import net.luxvacuos.igl.vector.Vector8f;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.utils.BlockFace;

public class Tessellator {

	private int vaoID, vboID0, vboID1, vboID2, vboID3, vboID4, vboID5, iboID, vboCapacity = 64, indicesCounter,
			iboCapacity = 64;

	private ByteBuffer buffer0, buffer1, buffer2, buffer3, buffer4, buffer5, ibo;

	private List<Vector3d> pos, normals;
	private List<Vector2d> texcoords;
	private List<Integer> indices;
	private List<Vector4d> data;
	private List<Vector3d> tangent;
	private List<Vector3d> bitangent;
	private int texture;
	private int normalMap;
	private int heightMap;
	private int pbrMap;
	private int occlusion;
	private boolean updated = false;

	private TessellatorShader shader;
	private TessellatorBasicShader basicShader;

	public Tessellator(Matrix4d projectionMatrix, Matrix4d shadowProjectionMatrix) {
		init(projectionMatrix, shadowProjectionMatrix);
	}

	private void init(Matrix4d projectionMatrix, Matrix4d shadowProjectionMatrix) {
		pos = new ArrayList<Vector3d>();
		texcoords = new ArrayList<Vector2d>();
		normals = new ArrayList<Vector3d>();
		data = new ArrayList<Vector4d>();
		indices = new ArrayList<Integer>();
		tangent = new ArrayList<Vector3d>();
		bitangent = new ArrayList<Vector3d>();
		shader = TessellatorShader.getInstance();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadBiasMatrix(shadowProjectionMatrix);
		shader.stop();
		basicShader = TessellatorBasicShader.getInstance();
		basicShader.start();
		basicShader.loadProjectionMatrix(shadowProjectionMatrix);
		basicShader.stop();

		occlusion = glGenQueries();

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, GL_DYNAMIC_DRAW);

		vboID0 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID0);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID1 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID1);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID2 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID2);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID3 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID3);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID4 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID4);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(4);
		glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboID5 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID5);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(5);
		glVertexAttribPointer(5, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void begin(int texture, int normalMap, int heightMap, int pbrMap) {
		this.texture = texture;
		this.normalMap = normalMap;
		this.heightMap = heightMap;
		this.pbrMap = pbrMap;
		pos.clear();
		texcoords.clear();
		normals.clear();
		data.clear();
		tangent.clear();
		bitangent.clear();
	}

	public void vertex3f(Vector3d pos) {
		this.pos.add(pos);
	}

	public void texture2f(Vector2d texcoords) {
		this.texcoords.add(texcoords);
	}

	public void normal3f(Vector3d normals) {
		this.normals.add(normals);
	}

	public void tangent3f(Vector3d tangent) {
		this.tangent.add(tangent);
	}

	public void bitangent3f(Vector3d bitangent) {
		this.bitangent.add(bitangent);
	}

	public void indice(int ind) {
		this.indices.add(ind);
	}

	public void data4f(Vector4d data) {
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

	public void draw(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation,
			Matrix4d projectionMatrix, int shadowMap, int shadowData, boolean transparent) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGlBuffers(vboID3, vboCapacity, buffer3);
			updateGlBuffers(vboID4, vboCapacity, buffer4);
			updateGlBuffers(vboID5, vboCapacity, buffer5);
			updateGLIBOBuffer();
			updated = false;
			clearBuffers();
		}
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadViewMatrix(Maths.createViewMatrix(camera), camera.getPosition());
		shader.loadLightMatrix(Maths.createViewMatrix(sunCamera));
		shader.loadTransparent(transparent);
		shader.loadSettings(ClientVariables.useShadows, ClientVariables.useParallax);
		shader.loadMoveFactor(clientWorldSimulation.getMoveFactor());
		shader.loadRainFactor(clientWorldSimulation.getRainFactor());
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
		glBindTexture(GL_TEXTURE_2D, shadowMap);
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, normalMap);
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, heightMap);
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, pbrMap);
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, shadowData);
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

	public void drawShadow(Camera camera, Matrix4d shadowProjectionMatrix) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGlBuffers(vboID3, vboCapacity, buffer3);
			updateGlBuffers(vboID4, vboCapacity, buffer4);
			updateGlBuffers(vboID5, vboCapacity, buffer5);
			updateGLIBOBuffer();
			updated = false;
			clearBuffers();
		}
		glCullFace(GL_FRONT);
		basicShader.start();
		basicShader.loadviewMatrix(camera);
		basicShader.loadProjectionMatrix(shadowProjectionMatrix);
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

	public void drawOcclusion(Camera camera, Matrix4d projectionMatrix) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGlBuffers(vboID3, vboCapacity, buffer3);
			updateGlBuffers(vboID4, vboCapacity, buffer4);
			updateGlBuffers(vboID5, vboCapacity, buffer5);
			updateGLIBOBuffer();
			updated = false;
			clearBuffers();
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

	public void loadData(List<Vector3d> pos, List<Vector2d> texcoords, List<Vector3d> normals, List<Vector4d> data,
			List<Vector3d> tangent, List<Vector3d> bitangent) {
		buffer0 = BufferUtils.createByteBuffer((pos.size() * 3) * 4);
		buffer1 = BufferUtils.createByteBuffer((texcoords.size() * 2) * 4);
		buffer2 = BufferUtils.createByteBuffer((normals.size() * 3) * 4);
		buffer3 = BufferUtils.createByteBuffer((data.size() * 4) * 4);
		buffer4 = BufferUtils.createByteBuffer((tangent.size() * 3) * 4);
		buffer5 = BufferUtils.createByteBuffer((bitangent.size() * 3) * 4);
		for (int i = 0; i < pos.size(); i++) {
			buffer0.putFloat((float) pos.get(i).x);
			buffer0.putFloat((float) pos.get(i).y);
			buffer0.putFloat((float) pos.get(i).z);
		}
		for (int i = 0; i < texcoords.size(); i++) {
			buffer1.putFloat((float) texcoords.get(i).x);
			buffer1.putFloat((float) texcoords.get(i).y);
		}
		for (int i = 0; i < normals.size(); i++) {
			buffer2.putFloat((float) normals.get(i).x);
			buffer2.putFloat((float) normals.get(i).y);
			buffer2.putFloat((float) normals.get(i).z);
		}
		for (int i = 0; i < data.size(); i++) {
			buffer3.putFloat((float) data.get(i).x);
			buffer3.putFloat((float) data.get(i).y);
			buffer3.putFloat((float) data.get(i).z);
			buffer3.putFloat((float) data.get(i).w);
		}
		for (int i = 0; i < tangent.size(); i++) {
			buffer4.putFloat((float) tangent.get(i).x);
			buffer4.putFloat((float) tangent.get(i).y);
			buffer4.putFloat((float) tangent.get(i).z);
		}
		for (int i = 0; i < bitangent.size(); i++) {
			buffer5.putFloat((float) bitangent.get(i).x);
			buffer5.putFloat((float) bitangent.get(i).y);
			buffer5.putFloat((float) bitangent.get(i).z);
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

	public void generateCube(double x, double y, double z, float size, boolean top, boolean bottom, boolean left,
			boolean right, boolean front, boolean back, RenderBlock block, float tbl_, float tbr_, float tfl_,
			float tfr_, float bbl_, float bbr_, float bfl_, float bfr_) {
		if (block.getID() != 0) {
			generateCube(x, y, z, size, size, size, top, bottom, left, right, front, back, block, tbl_, tbr_, tfl_,
					tfr_, bbl_, bbr_, bfl_, bfr_);
		}
	}

	public void generateCube(double x, double y, double z, float xsize, float ysize, float zsize, boolean top,
			boolean bottom, boolean left, boolean right, boolean front, boolean back, RenderBlock block, float tbl_,
			float tbr_, float tfl_, float tfr_, float bbl_, float bbr_, float bfl_, float bfr_) {
		int id = block.getID();
		float tbl = tbl_ / 15f;
		float tbr = tbr_ / 15f;
		float tfl = tfl_ / 15f;
		float tfr = tfr_ / 15f;
		float bbl = bbl_ / 15f;
		float bbr = bbr_ / 15f;
		float bfl = bfl_ / 15f;
		float bfr = bfr_ / 15f;
		if (id != 0) {
			Vector8f texcoords;
			Vector3d edge1, edge2, tangent, bitangent;
			Vector2d deltaUV1, deltaUV2;
			float f;

			if (top) {
				texcoords = block.getTexCoords(BlockFace.TOP);
				// top face
				vertex3f(new Vector3d(x, y + ysize, z + zsize));
				texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
				normal3f(new Vector3d(0, 1, 0));
				data4f(new Vector4d(id, tfl, 0, 0));

				vertex3f(new Vector3d(x + xsize, y + ysize, z + zsize));
				texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
				normal3f(new Vector3d(0, 1, 0));
				data4f(new Vector4d(id, tfr, 0, 0));

				vertex3f(new Vector3d(x + xsize, y + ysize, z));
				texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
				normal3f(new Vector3d(0, 1, 0));
				data4f(new Vector4d(id, tbr, 0, 0));

				vertex3f(new Vector3d(x, y + ysize, z));
				texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
				normal3f(new Vector3d(0, 1, 0));
				data4f(new Vector4d(id, tbl, 0, 0));

				edge1 = Vector3d.sub(new Vector3d(x + xsize, y + ysize, z + zsize),
						new Vector3d(x, y + ysize, z + zsize), null);
				edge2 = Vector3d.sub(new Vector3d(x + xsize, y + ysize, z), new Vector3d(x, y + ysize, z + zsize),
						null);
				deltaUV1 = Vector2d.sub(new Vector2d(texcoords.getI(), texcoords.getJ()),
						new Vector2d(texcoords.getZ(), texcoords.getW()), null);
				deltaUV2 = Vector2d.sub(new Vector2d(texcoords.getK(), texcoords.getL()),
						new Vector2d(texcoords.getZ(), texcoords.getW()), null);

				f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

				tangent = new Vector3d();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				bitangent = new Vector3d();

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
				texcoords = block.getTexCoords(BlockFace.BOTTOM);
				// bottom face
				vertex3f(new Vector3d(x, y, z));
				texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
				normal3f(new Vector3d(0, -1, 0));
				data4f(new Vector4d(id, bbl, 0, 0));

				vertex3f(new Vector3d(x + xsize, y, z));
				texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
				normal3f(new Vector3d(0, -1, 0));
				data4f(new Vector4d(id, bbr, 0, 0));

				vertex3f(new Vector3d(x + xsize, y, z + zsize));
				texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
				normal3f(new Vector3d(0, -1, 0));
				data4f(new Vector4d(id, bfr, 0, 0));

				vertex3f(new Vector3d(x, y, z + zsize));
				texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
				normal3f(new Vector3d(0, -1, 0));
				data4f(new Vector4d(id, bfl, 0, 0));

				edge1 = Vector3d.sub(new Vector3d(x + xsize, y, z), new Vector3d(x, y, z), null);

				edge2 = Vector3d.sub(new Vector3d(x + xsize, y, z + zsize), new Vector3d(x, y, z), null);

				deltaUV1 = Vector2d.sub(new Vector2d(texcoords.getI(), texcoords.getJ()),
						new Vector2d(texcoords.getZ(), texcoords.getW()), null);

				deltaUV2 = Vector2d.sub(new Vector2d(texcoords.getK(), texcoords.getL()),
						new Vector2d(texcoords.getZ(), texcoords.getW()), null);

				f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

				tangent = new Vector3d();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				bitangent = new Vector3d();

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
				texcoords = block.getTexCoords(BlockFace.SOUTH);
				// back face
				vertex3f(new Vector3d(x, y, z + zsize));
				texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
				normal3f(new Vector3d(0, 0, 1));
				data4f(new Vector4d(id, bfl, 0, 0));

				vertex3f(new Vector3d(x + xsize, y, z + zsize));
				texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
				normal3f(new Vector3d(0, 0, 1));
				data4f(new Vector4d(id, bfr, 0, 0));

				vertex3f(new Vector3d(x + xsize, y + ysize, z + zsize));
				texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
				normal3f(new Vector3d(0, 0, 1));
				data4f(new Vector4d(id, tfr, 0, 0));

				vertex3f(new Vector3d(x, y + ysize, z + zsize));
				texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
				normal3f(new Vector3d(0, 0, 1));
				data4f(new Vector4d(id, tfl, 0, 0));

				edge1 = Vector3d.sub(new Vector3d(x + xsize, y, z + zsize), new Vector3d(x, y, z + zsize), null);

				edge2 = Vector3d.sub(new Vector3d(x + xsize, y + ysize, z + zsize), new Vector3d(x, y, z + zsize),
						null);

				deltaUV1 = Vector2d.sub(new Vector2d(texcoords.getK(), texcoords.getL()),
						new Vector2d(texcoords.getX(), texcoords.getY()), null);

				deltaUV2 = Vector2d.sub(new Vector2d(texcoords.getI(), texcoords.getJ()),
						new Vector2d(texcoords.getX(), texcoords.getY()), null);

				f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

				tangent = new Vector3d();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				bitangent = new Vector3d();

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
				texcoords = block.getTexCoords(BlockFace.NORTH);
				vertex3f(new Vector3d(x, y + ysize, z));
				texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
				normal3f(new Vector3d(0, 0, -1));
				data4f(new Vector4d(id, tbl, 0, 0));

				vertex3f(new Vector3d(x + xsize, y + ysize, z));
				texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
				normal3f(new Vector3d(0, 0, -1));
				data4f(new Vector4d(id, tbr, 0, 0));

				vertex3f(new Vector3d(x + xsize, y, z));
				texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
				normal3f(new Vector3d(0, 0, -1));
				data4f(new Vector4d(id, bbr, 0, 0));

				vertex3f(new Vector3d(x, y, z));
				texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
				normal3f(new Vector3d(0, 0, -1));
				data4f(new Vector4d(id, bbl, 0, 0));

				edge1 = Vector3d.sub(new Vector3d(x + xsize, y + ysize, z), new Vector3d(x, y + ysize, z), null);

				edge2 = Vector3d.sub(new Vector3d(x + xsize, y, z), new Vector3d(x, y + ysize, z), null);

				deltaUV1 = Vector2d.sub(new Vector2d(texcoords.getI(), texcoords.getJ()),
						new Vector2d(texcoords.getZ(), texcoords.getW()), null);

				deltaUV2 = Vector2d.sub(new Vector2d(texcoords.getK(), texcoords.getL()),
						new Vector2d(texcoords.getZ(), texcoords.getW()), null);

				f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

				tangent = new Vector3d();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				bitangent = new Vector3d();

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
				texcoords = block.getTexCoords(BlockFace.EAST);
				// right face
				vertex3f(new Vector3d(x, y, z));
				texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
				normal3f(new Vector3d(-1, 0, 0));
				data4f(new Vector4d(id, bbl, 0, 0));

				vertex3f(new Vector3d(x, y, z + zsize));
				texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
				normal3f(new Vector3d(-1, 0, 0));
				data4f(new Vector4d(id, bfl, 0, 0));

				vertex3f(new Vector3d(x, y + ysize, z + zsize));
				texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
				normal3f(new Vector3d(-1, 0, 0));
				data4f(new Vector4d(id, tfl, 0, 0));

				vertex3f(new Vector3d(x, y + ysize, z));
				texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
				normal3f(new Vector3d(-1, 0, 0));
				data4f(new Vector4d(id, tbl, 0, 0));

				edge1 = Vector3d.sub(new Vector3d(x, y, z + zsize), new Vector3d(x, y, z), null);

				edge2 = Vector3d.sub(new Vector3d(x, y + ysize, z + zsize), new Vector3d(x, y, z), null);

				deltaUV1 = Vector2d.sub(new Vector2d(texcoords.getX(), texcoords.getY()),
						new Vector2d(texcoords.getK(), texcoords.getL()), null);

				deltaUV2 = Vector2d.sub(new Vector2d(texcoords.getZ(), texcoords.getW()),
						new Vector2d(texcoords.getK(), texcoords.getL()), null);

				f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

				tangent = new Vector3d();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				bitangent = new Vector3d();

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
				texcoords = block.getTexCoords(BlockFace.WEST);
				// left face
				vertex3f(new Vector3d(x + xsize, y, z + zsize));
				texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
				normal3f(new Vector3d(1, 0, 0));
				data4f(new Vector4d(id, bfr, 0, 0));

				vertex3f(new Vector3d(x + xsize, y, z));
				texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
				normal3f(new Vector3d(1, 0, 0));
				data4f(new Vector4d(id, bbr, 0, 0));

				vertex3f(new Vector3d(x + xsize, y + ysize, z));
				texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
				normal3f(new Vector3d(1, 0, 0));
				data4f(new Vector4d(id, tbr, 0, 0));

				vertex3f(new Vector3d(x + xsize, y + ysize, z + zsize));
				texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
				normal3f(new Vector3d(1, 0, 0));
				data4f(new Vector4d(id, tfr, 0, 0));

				edge1 = Vector3d.sub(new Vector3d(x + xsize, y, z), new Vector3d(x + xsize, y, z + zsize), null);

				edge2 = Vector3d.sub(new Vector3d(x + xsize, y + ysize, z), new Vector3d(x + xsize, y, z + zsize),
						null);

				deltaUV1 = Vector2d.sub(new Vector2d(texcoords.getX(), texcoords.getY()),
						new Vector2d(texcoords.getK(), texcoords.getL()), null);

				deltaUV2 = Vector2d.sub(new Vector2d(texcoords.getZ(), texcoords.getW()),
						new Vector2d(texcoords.getK(), texcoords.getL()), null);

				f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

				tangent = new Vector3d();

				tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
				tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
				tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
				tangent.normalise();

				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);
				tangent3f(tangent);

				bitangent = new Vector3d();

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

	private void clearBuffers() {
		if (buffer0 != null)
			buffer0.clear();
		if (buffer1 != null)
			buffer1.clear();
		if (buffer2 != null)
			buffer2.clear();
		if (buffer3 != null)
			buffer3.clear();
		if (buffer4 != null)
			buffer4.clear();
		if (buffer5 != null)
			buffer5.clear();
		if (ibo != null)
			ibo.clear();
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
		clearBuffers();
	}

	public int getOcclusion() {
		return occlusion;
	}

}
