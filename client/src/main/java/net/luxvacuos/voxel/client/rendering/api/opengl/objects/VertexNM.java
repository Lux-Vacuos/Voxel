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

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.vector.Vector3f;

public class VertexNM {

	private static final int NO_INDEX = -1;

	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private VertexNM duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);

	public VertexNM(int index, Vector3f position) {
		this.index = index;
		this.position = position;
		this.length = position.length();
	}

	public void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}

	public VertexNM duplicate(int newIndex) {
		VertexNM vertex = new VertexNM(newIndex, position);
		vertex.tangents = this.tangents;
		return vertex;
	}

	public void averageTangents() {
		if (tangents.isEmpty()) {
			return;
		}
		for (Vector3f tangent : tangents) {
			Vector3f.add(averagedTangent, tangent, averagedTangent);
		}
		averagedTangent.normalise();
	}

	public Vector3f getAverageTangent() {
		return averagedTangent;
	}

	public int getIndex() {
		return index;
	}

	public float getLength() {
		return length;
	}

	public boolean isSet() {
		return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
	}

	public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
		return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}

	public Vector3f getPosition() {
		return position;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public VertexNM getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(VertexNM duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
