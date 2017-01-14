/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.igl.vector.Vector8f;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.rendering.world.block.IRenderBlock;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.utils.BlockFace;

public class Tessellator {

	private int vaoID, vboID0, vboID1, vboID2, iboID, vboCapacity = 64, indicesCounter, iboCapacity = 64;

	private ByteBuffer buffer0, buffer1, buffer2, ibo;

	private List<Vector3d> pos, normals;
	private List<Vector2d> texcoords;
	private List<Integer> indices;

	private int occlusion;
	private Material material;
	private boolean updated = false;

	private TessellatorShader shader;
	private TessellatorBasicShader basicShader;

	public Tessellator(Material material) {
		this.material = material;
		pos = new ArrayList<Vector3d>();
		texcoords = new ArrayList<Vector2d>();
		normals = new ArrayList<Vector3d>();
		indices = new ArrayList<Integer>();
		shader = TessellatorShader.getShader();
		basicShader = TessellatorBasicShader.getShader();

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

		glBindVertexArray(0);
	}

	public void begin() {
		pos.clear();
		texcoords.clear();
		normals.clear();
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

	public void indice(int ind) {
		this.indices.add(ind);
	}

	public void end() {
		loadData(pos, texcoords, normals);
		pos.clear();
		texcoords.clear();
		normals.clear();
		updated = true;
	}

	public void draw(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation, ShadowFBO shadow) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGLIBOBuffer();
			updated = false;
			clearBuffers();
		}
		shader.start();
		shader.loadViewMatrix(camera.getViewMatrix(), camera.getPosition());
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadLightMatrix(sunCamera.getViewMatrix());
		shader.loadSettings(ClientVariables.useShadows);
		shader.loadMoveFactor(clientWorldSimulation.getMoveFactor());
		shader.loadBiasMatrix(sunCamera.getProjectionMatrix());
		shader.loadMaterial(material);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, material.getDiffuseTexture().getID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, material.getNormalTexture().getID());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, material.getRoughnessTexture().getID());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, material.getMetallicTexture().getID());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, material.getSpecularTexture().getID());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, shadow.getShadowMap());
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
		shader.stop();
	}

	public void drawShadow(Camera sunCamera) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGLIBOBuffer();
			updated = false;
			clearBuffers();
		}
		glCullFace(GL_FRONT);
		basicShader.start();
		basicShader.loadViewMatrix(sunCamera.getViewMatrix(), sunCamera.getPosition());
		basicShader.loadProjectionMatrix(sunCamera.getProjectionMatrix());
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, material.getDiffuseTexture().getID());
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		basicShader.stop();
		glCullFace(GL_BACK);
	}

	public void drawOcclusion(Camera camera) {
		if (updated) {
			updateGlBuffers(vboID0, vboCapacity, buffer0);
			updateGlBuffers(vboID1, vboCapacity, buffer1);
			updateGlBuffers(vboID2, vboCapacity, buffer2);
			updateGLIBOBuffer();
			updated = false;
			clearBuffers();
		}
		basicShader.start();
		basicShader.loadViewMatrix(camera.getViewMatrix(), camera.getPosition());
		basicShader.loadProjectionMatrix(camera.getProjectionMatrix());
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, material.getDiffuseTexture().getID());
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		basicShader.stop();
	}

	public void loadData(List<Vector3d> pos, List<Vector2d> texcoords, List<Vector3d> normals) {
		buffer0 = BufferUtils.createByteBuffer((pos.size() * 3) * 4);
		buffer1 = BufferUtils.createByteBuffer((texcoords.size() * 2) * 4);
		buffer2 = BufferUtils.createByteBuffer((normals.size() * 3) * 4);
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
		buffer0.flip();
		buffer1.flip();
		buffer2.flip();
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
			boolean right, boolean front, boolean back, IRenderBlock block) {
		generateCube(x, y, z, size, size, size, top, bottom, left, right, front, back, block);
	}

	public void generateCube(double x, double y, double z, float xsize, float ysize, float zsize, boolean top,
			boolean bottom, boolean left, boolean right, boolean front, boolean back, IRenderBlock block) {
		Vector8f texcoords;
		if (top) {
			texcoords = block.getTexCoords(BlockFace.UP);
			// top face
			vertex3f(new Vector3d(x, y + ysize, z + zsize));
			texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3d(0, 1, 0));

			vertex3f(new Vector3d(x + xsize, y + ysize, z + zsize));
			texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3d(0, 1, 0));

			vertex3f(new Vector3d(x + xsize, y + ysize, z));
			texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3d(0, 1, 0));

			vertex3f(new Vector3d(x, y + ysize, z));
			texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3d(0, 1, 0));

		}
		if (bottom) {
			texcoords = block.getTexCoords(BlockFace.DOWN);
			// bottom face
			vertex3f(new Vector3d(x, y, z));
			texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3d(0, -1, 0));

			vertex3f(new Vector3d(x + xsize, y, z));
			texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3d(0, -1, 0));

			vertex3f(new Vector3d(x + xsize, y, z + zsize));
			texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3d(0, -1, 0));

			vertex3f(new Vector3d(x, y, z + zsize));
			texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3d(0, -1, 0));

		}

		if (back) {
			texcoords = block.getTexCoords(BlockFace.SOUTH);
			// back face
			vertex3f(new Vector3d(x, y, z + zsize));
			texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3d(0, 0, 1));

			vertex3f(new Vector3d(x + xsize, y, z + zsize));
			texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3d(0, 0, 1));

			vertex3f(new Vector3d(x + xsize, y + ysize, z + zsize));
			texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3d(0, 0, 1));

			vertex3f(new Vector3d(x, y + ysize, z + zsize));
			texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3d(0, 0, 1));

		}
		if (front) {
			// front face
			texcoords = block.getTexCoords(BlockFace.NORTH);
			vertex3f(new Vector3d(x, y + ysize, z));
			texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3d(0, 0, -1));

			vertex3f(new Vector3d(x + xsize, y + ysize, z));
			texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3d(0, 0, -1));

			vertex3f(new Vector3d(x + xsize, y, z));
			texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3d(0, 0, -1));

			vertex3f(new Vector3d(x, y, z));
			texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3d(0, 0, -1));

		}
		if (right) {
			texcoords = block.getTexCoords(BlockFace.EAST);
			// right face
			vertex3f(new Vector3d(x, y, z));
			texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3d(-1, 0, 0));

			vertex3f(new Vector3d(x, y, z + zsize));
			texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3d(-1, 0, 0));

			vertex3f(new Vector3d(x, y + ysize, z + zsize));
			texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3d(-1, 0, 0));

			vertex3f(new Vector3d(x, y + ysize, z));
			texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3d(-1, 0, 0));


		}
		if (left) {
			texcoords = block.getTexCoords(BlockFace.WEST);
			// left face
			vertex3f(new Vector3d(x + xsize, y, z + zsize));
			texture2f(new Vector2d(texcoords.getK(), texcoords.getL()));
			normal3f(new Vector3d(1, 0, 0));

			vertex3f(new Vector3d(x + xsize, y, z));
			texture2f(new Vector2d(texcoords.getX(), texcoords.getY()));
			normal3f(new Vector3d(1, 0, 0));

			vertex3f(new Vector3d(x + xsize, y + ysize, z));
			texture2f(new Vector2d(texcoords.getZ(), texcoords.getW()));
			normal3f(new Vector3d(1, 0, 0));

			vertex3f(new Vector3d(x + xsize, y + ysize, z + zsize));
			texture2f(new Vector2d(texcoords.getI(), texcoords.getJ()));
			normal3f(new Vector3d(1, 0, 0));

		}
	}

	private void clearBuffers() {
		if (this.buffer0 != null)
			this.buffer0.clear();
		if (this.buffer1 != null)
			this.buffer1.clear();
		if (this.buffer2 != null)
			this.buffer2.clear();
		if (this.ibo != null)
			this.ibo.clear();
	}

	public void cleanUp() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID0);
		glDeleteBuffers(vboID1);
		glDeleteBuffers(vboID2);
		glDeleteBuffers(iboID);
		glDeleteQueries(occlusion);
		pos.clear();
		texcoords.clear();
		normals.clear();
		clearBuffers();
	}

	public int getOcclusion() {
		return occlusion;
	}

}
