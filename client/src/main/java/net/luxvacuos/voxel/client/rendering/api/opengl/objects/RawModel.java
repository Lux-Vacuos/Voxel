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

/**
 * Raw Model
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class RawModel {
	/**
	 * VAOid
	 */
	private int vaoID;
	/**
	 * Vertex Count
	 */
	private int vertexCount;

	/**
	 * 
	 * @param vaoID
	 * @param vertexCount
	 */
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * Get VAOid
	 * 
	 * @return VAOid
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Get Vertex Count
	 * 
	 * @return Vertex Count
	 */
	public int getVertexCount() {
		return vertexCount;
	}

}
