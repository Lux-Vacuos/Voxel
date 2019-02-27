/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
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

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.lightengine.client.ecs.entities.CameraEntity;
import net.luxvacuos.lightengine.client.util.Maths;
import net.luxvacuos.lightengine.universal.resources.IDisposable;
import net.luxvacuos.voxel.client.rendering.shaders.BlockOutlineShader;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.utils.BlockNode;

public class BlockOutlineRenderer implements IDisposable {

	private int vaoID, vboID0, iboID, vboCapacity = 64, indicesCounter, iboCapacity = 64;

	private ByteBuffer buffer0, ibo;

	private List<Vector3f> pos;

	private BlockOutlineShader shader;
	private Vector3f color;
	private Vector3f position = new Vector3f();

	public BlockOutlineRenderer() {
		shader = new BlockOutlineShader();
		color = new Vector3f(0.8f, 0.8f, 0.8f);
		pos = new ArrayList<>();
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
		glBindVertexArray(0);
	}

	public void begin() {
		pos.clear();
	}

	public void vertex3f(Vector3f pos) {
		this.pos.add(pos);
	}

	public void end() {
		loadData(pos);
		pos.clear();
	}

	public void loadData(List<Vector3f> pos) {
		buffer0 = BufferUtils.createByteBuffer((pos.size() * 3) * 4);
		for (int i = 0; i < pos.size(); i++) {
			buffer0.putFloat((float) pos.get(i).x);
			buffer0.putFloat((float) pos.get(i).y);
			buffer0.putFloat((float) pos.get(i).z);
		}
		buffer0.flip();
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

	public void generateCube(IBlock block) {
		BoundingBox box = block.getBoundingBox(new BlockNode(0, 0, 0));
		// TODO: Casts
		float x = (float) (box.min.x - 0.5);
		float y = (float) (box.min.y - 0.5);
		float z = (float) (box.min.z - 0.5);
		float xsize = (float) box.max.x;
		float ysize = (float) box.max.y;
		float zsize = (float) box.max.z;
		vertex3f(new Vector3f(x, y + ysize, z + zsize));
		vertex3f(new Vector3f(x + xsize, y + ysize, z + zsize));
		vertex3f(new Vector3f(x + xsize, y + ysize, z));
		vertex3f(new Vector3f(x, y + ysize, z));

		vertex3f(new Vector3f(x, y, z + zsize));
		vertex3f(new Vector3f(x + xsize, y, z + zsize));
		vertex3f(new Vector3f(x + xsize, y + ysize, z + zsize));
		vertex3f(new Vector3f(x, y + ysize, z + zsize));

		vertex3f(new Vector3f(x, y, z + zsize));
		vertex3f(new Vector3f(x, y, z));
		vertex3f(new Vector3f(x + xsize, y, z));
		vertex3f(new Vector3f(x + xsize, y, z + zsize));

		vertex3f(new Vector3f(x, y, z));
		vertex3f(new Vector3f(x, y, z + zsize));
		vertex3f(new Vector3f(x, y + ysize, z + zsize));
		vertex3f(new Vector3f(x, y + ysize, z));

		vertex3f(new Vector3f(x, y + ysize, z));
		vertex3f(new Vector3f(x + xsize, y + ysize, z));
		vertex3f(new Vector3f(x + xsize, y, z));
		vertex3f(new Vector3f(x, y, z));

	}

	public void render(CameraEntity camera, IBlock block) {
		begin();
		this.generateCube(block);
		end();

		updateGlBuffers(vboID0, vboCapacity, buffer0);
		updateGLIBOBuffer();

		shader.start();
		shader.loadColor(color);
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera.getViewMatrix());
		position.set(block.getBlockX() + 0.5f, block.getBlockY() + 0.5f, block.getBlockZ() + 0.5f);
		shader.loadTransformationMatrix(Maths.createTransformationMatrix(position, 0, 0, 0, 1.01f));
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glDrawElements(GL_LINE_STRIP, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	@Override
	public void dispose() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID0);
		glDeleteBuffers(iboID);
		if (this.buffer0 != null)
			this.buffer0.clear();
		if (this.ibo != null)
			this.ibo.clear();
		pos.clear();
		shader.dispose();
	}

	public Vector3f getColor() {
		return color;
	}


}
