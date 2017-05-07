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

package net.luxvacuos.voxel.client.rendering.api.opengl.objects;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public class Mesh implements IDisposable {

	private VAO mesh;
	private AIMesh aiMesh;

	public Mesh(AIMesh aiMesh) {
		this.aiMesh = aiMesh;

		mesh = VAO.create();

		List<Vector3f> pos = new ArrayList<>();
		List<Vector2f> tex = new ArrayList<>();
		List<Vector3f> nor = new ArrayList<>();
		List<Vector3f> tan = new ArrayList<>();

		for (int i = 0; i < aiMesh.mNumVertices(); i++) {
			AIVector3D position = aiMesh.mVertices().get(i);
			AIVector3D texcoord = aiMesh.mTextureCoords(0).get(i);
			AIVector3D normal = aiMesh.mNormals().get(i);
			AIVector3D tangent = aiMesh.mTangents().get(i);
			pos.add(new Vector3f(position.x(), position.y(), position.z()));
			tex.add(new Vector2f(texcoord.x(), texcoord.y()));
			nor.add(new Vector3f(normal.x(), normal.y(), normal.z()));
			tan.add(new Vector3f(tangent.x(), tangent.y(), tangent.z()));
		}

		loadData(pos, tex, nor, tan);

		int faceCount = aiMesh.mNumFaces();
		int elementCount = faceCount * 3;
		IntBuffer elementArrayBufferData = BufferUtils.createIntBuffer(elementCount);
		AIFace.Buffer facesBuffer = aiMesh.mFaces();
		for (int i = 0; i < faceCount; ++i) {
			AIFace face = facesBuffer.get(i);
			if (face.mNumIndices() != 3) {
				throw new IllegalStateException("AIFace.mNumIndices() != 3");
			}
			elementArrayBufferData.put(face.mIndices());
		}
		elementArrayBufferData.flip();
		int[] ind = new int[elementCount];
		elementArrayBufferData.get(ind);
		mesh.createIndexBuffer(ind, GL_STATIC_DRAW);
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}

	public AIMesh getAiMesh() {
		return aiMesh;
	}

	public VAO getMesh() {
		return mesh;
	}

	private void loadData(List<Vector3f> positions, List<Vector2f> texcoords, List<Vector3f> normals,
			List<Vector3f> tangets) {
		FloatBuffer posB = BufferUtils.createFloatBuffer(positions.size() * 3);
		FloatBuffer texB = BufferUtils.createFloatBuffer(texcoords.size() * 2);
		FloatBuffer norB = BufferUtils.createFloatBuffer(normals.size() * 3);
		FloatBuffer tanB = BufferUtils.createFloatBuffer(tangets.size() * 3);
		for (int i = 0; i < positions.size(); i++) {
			posB.put(positions.get(i).x);
			posB.put(positions.get(i).y);
			posB.put(positions.get(i).z);
		}
		for (int i = 0; i < texcoords.size(); i++) {
			texB.put(texcoords.get(i).x);
			texB.put(texcoords.get(i).y);
		}
		for (int i = 0; i < normals.size(); i++) {
			norB.put(normals.get(i).x);
			norB.put(normals.get(i).y);
			norB.put(normals.get(i).z);
		}
		for (int i = 0; i < tangets.size(); i++) {
			tanB.put(tangets.get(i).x);
			tanB.put(tangets.get(i).y);
			tanB.put(tangets.get(i).z);
		}
		posB.flip();
		texB.flip();
		norB.flip();
		tanB.flip();
		float[] posA = new float[posB.capacity()], texA = new float[texB.capacity()], norA = new float[norB.capacity()],
				tanA = new float[tanB.capacity()];
		posB.get(posA);
		texB.get(texA);
		norB.get(norA);
		tanB.get(tanA);
		mesh.createAttribute(0, posA, 3, GL_STATIC_DRAW);
		mesh.createAttribute(1, texA, 2, GL_STATIC_DRAW);
		mesh.createAttribute(2, norA, 3, GL_STATIC_DRAW);
		mesh.createAttribute(3, tanA, 3, GL_STATIC_DRAW);

	}

}
