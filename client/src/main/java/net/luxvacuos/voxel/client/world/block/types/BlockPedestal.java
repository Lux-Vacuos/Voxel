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

package net.luxvacuos.voxel.client.world.block.types;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.igl.vector.Vector8f;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.world.block.BlockEntity;
import net.luxvacuos.voxel.client.world.block.BlocksResources;

public class BlockPedestal extends BlockEntity {

	public BlockPedestal(Integer x, Integer y, Integer z) {
		super(x, y, z);
		customModel = false;
		transparent = true;
	}

	public BlockPedestal() {
		super();
		customModel = false;
		transparent = true;
	}

	@Override
	public Vector8f texCoordsBack() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Pedestal");
	}

	@Override
	public Vector8f texCoordsDown() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("PedestalBottom");
	}

	@Override
	public Vector8f texCoordsFront() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Pedestal");
	}

	@Override
	public Vector8f texCoordsLeft() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Pedestal");
	}

	@Override
	public Vector8f texCoordsRight() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Pedestal");
	}

	@Override
	public Vector8f texCoordsUp() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("PedestalTop");
	}

	@Override
	public byte getId() {
		return 17;
	}

	@Override
	public void generateCustomModel(Tessellator tess, double x, double y, double z, float globalScale, boolean top,
			boolean bottom, boolean left, boolean right, boolean front, boolean back, float tbl_, float tbr_,
			float tfl_, float tfr_, float bbl_, float bbr_, float bfl_, float bfr_) {

		float tbl = tbl_ / 15f;
		float tbr = tbr_ / 15f;
		float tfl = tfl_ / 15f;
		float tfr = tfr_ / 15f;
		float bbl = bbl_ / 15f;
		float bbr = bbr_ / 15f;
		float bfl = bfl_ / 15f;
		float bfr = bfr_ / 15f;
		if (top) {
			Vector8f texcoords = texCoordsUp();
			// top face
			tess.vertex3f(new Vector3f(x + 0.3f, y + 1f, z + 0.7f));
			tess.texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			tess.normal3f(new Vector3f(0, 1, 0));
			tess.data4f(new Vector4f(getId(), tfl, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.7f, y + 1f, z + 0.7f));
			tess.texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			tess.normal3f(new Vector3f(0, 1, 0));
			tess.data4f(new Vector4f(getId(), tfr, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.7f, y + 1f, z + 0.3f));
			tess.texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			tess.normal3f(new Vector3f(0, 1, 0));
			tess.data4f(new Vector4f(getId(), tbr, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.3f, y + 1f, z + 0.3f));
			tess.texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			tess.normal3f(new Vector3f(0, 1, 0));
			tess.data4f(new Vector4f(getId(), tbl, 0, 0));

			Vector3f edge1 = Vector3f.sub(new Vector3f(x + 0.7f, y + 1f, z + 0.7f),
					new Vector3f(x + 0.3f, y + 1f, z + 0.7f), null);
			Vector3f edge2 = Vector3f.sub(new Vector3f(x + 0.7f, y + 1f, z + 0.3f),
					new Vector3f(x + 0.3f, y + 1f, z + 0.7f), null);
			Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
					new Vector2f(texcoords.getZ(), texcoords.getW()), null);
			Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
					new Vector2f(texcoords.getZ(), texcoords.getW()), null);

			float f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

			Vector3f tangent = new Vector3f();

			tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
			tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
			tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
			tangent.normalise();

			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);

			Vector3f bitangent = new Vector3f();

			bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
			bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
			bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
			bitangent.normalise();

			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);

		}

		if (bottom) {
			Vector8f texcoords = texCoordsDown();
			// bottom face
			tess.vertex3f(new Vector3f(x, y, z));
			tess.texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			tess.normal3f(new Vector3f(0, -1, 0));
			tess.data4f(new Vector4f(getId(), bbl, 0, 0));

			tess.vertex3f(new Vector3f(x + 1f, y, z));
			tess.texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			tess.normal3f(new Vector3f(0, -1, 0));
			tess.data4f(new Vector4f(getId(), bbr, 0, 0));

			tess.vertex3f(new Vector3f(x + 1f, y, z + 1f));
			tess.texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			tess.normal3f(new Vector3f(0, -1, 0));
			tess.data4f(new Vector4f(getId(), bfr, 0, 0));

			tess.vertex3f(new Vector3f(x, y, z + 1f));
			tess.texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			tess.normal3f(new Vector3f(0, -1, 0));
			tess.data4f(new Vector4f(getId(), bfl, 0, 0));

			Vector3f edge1 = Vector3f.sub(new Vector3f(x + 1f, y, z), new Vector3f(x, y, z), null);

			Vector3f edge2 = Vector3f.sub(new Vector3f(x + 1f, y, z + 1f), new Vector3f(x, y, z), null);

			Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
					new Vector2f(texcoords.getZ(), texcoords.getW()), null);

			Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
					new Vector2f(texcoords.getZ(), texcoords.getW()), null);

			float f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

			Vector3f tangent = new Vector3f();

			tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
			tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
			tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
			tangent.normalise();

			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);

			Vector3f bitangent = new Vector3f();

			bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
			bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
			bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
			bitangent.normalise();

			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
		}

		if (back) {
			Vector8f texcoords = texCoordsBack();
			// back face
			tess.vertex3f(new Vector3f(x, y, z + 1f));
			tess.texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			tess.normal3f(new Vector3f(0, 0.2f, 1));
			tess.data4f(new Vector4f(getId(), bfl, 0, 0));

			tess.vertex3f(new Vector3f(x + 1f, y, z + 1f));
			tess.texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			tess.normal3f(new Vector3f(0, 0.2f, 1));
			tess.data4f(new Vector4f(getId(), bfr, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.7f, y + 1f, z + 0.7f));
			tess.texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			tess.normal3f(new Vector3f(0, 0.2f, 1));
			tess.data4f(new Vector4f(getId(), tfr, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.3f, y + 1f, z + 0.7f));
			tess.texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			tess.normal3f(new Vector3f(0, 0.2f, 1));
			tess.data4f(new Vector4f(getId(), tfl, 0, 0));

			Vector3f edge1 = Vector3f.sub(new Vector3f(x + 0.3f, y + 1f, z + 0.7f), new Vector3f(x, y, z + 1f), null);

			Vector3f edge2 = Vector3f.sub(new Vector3f(x + 0.7f, y + 1f, z + 0.7f), new Vector3f(x, y, z + 1f), null);

			Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
					new Vector2f(texcoords.getX(), texcoords.getY()), null);

			Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
					new Vector2f(texcoords.getX(), texcoords.getY()), null);

			float f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

			Vector3f tangent = new Vector3f();

			tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
			tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
			tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
			tangent.normalise();

			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);

			Vector3f bitangent = new Vector3f();

			bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
			bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
			bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
			bitangent.normalise();

			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);

		}

		if (front) {
			// front face
			Vector8f texcoords = texCoordsFront();
			tess.vertex3f(new Vector3f(x + 0.3f, y + 1f, z + 0.3f));
			tess.texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			tess.normal3f(new Vector3f(0, 0.2f, -1));
			tess.data4f(new Vector4f(getId(), tbl, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.7f, y + 1f, z + 0.3f));
			tess.texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			tess.normal3f(new Vector3f(0, 0.2f, -1));
			tess.data4f(new Vector4f(getId(), tbr, 0, 0));

			tess.vertex3f(new Vector3f(x + 1f, y, z));
			tess.texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			tess.normal3f(new Vector3f(0, 0.2f, -1));
			tess.data4f(new Vector4f(getId(), bbr, 0, 0));

			tess.vertex3f(new Vector3f(x, y, z));
			tess.texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			tess.normal3f(new Vector3f(0, 0.2f, -1));
			tess.data4f(new Vector4f(getId(), bbl, 0, 0));

			Vector3f edge1 = Vector3f.sub(new Vector3f(x + 0.7f, y + 1f, z + 0.3f),
					new Vector3f(x + 0.3f, y + 1f, z + 0.3f), null);

			Vector3f edge2 = Vector3f.sub(new Vector3f(x + 1f, y, z), new Vector3f(x + 0.3f, y + 1f, z + 0.3f), null);

			Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getI(), texcoords.getJ()),
					new Vector2f(texcoords.getZ(), texcoords.getW()), null);

			Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getK(), texcoords.getL()),
					new Vector2f(texcoords.getZ(), texcoords.getW()), null);

			float f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

			Vector3f tangent = new Vector3f();

			tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
			tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
			tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
			tangent.normalise();

			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);

			Vector3f bitangent = new Vector3f();

			bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
			bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
			bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
			bitangent.normalise();

			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);

		}

		if (right) {
			Vector8f texcoords = texCoordsRight();
			// right face
			tess.vertex3f(new Vector3f(x, y, z));
			tess.texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			tess.normal3f(new Vector3f(-1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), bbl, 0, 0));

			tess.vertex3f(new Vector3f(x, y, z + 1f));
			tess.texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			tess.normal3f(new Vector3f(-1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), bfl, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.3f, y + 1f, z + 0.7f));
			tess.texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			tess.normal3f(new Vector3f(-1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), tfl, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.3f, y + 1f, z + 0.3f));
			tess.texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			tess.normal3f(new Vector3f(-1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), tbl, 0, 0));

			Vector3f edge1 = Vector3f.sub(new Vector3f(x, y, z + 1f), new Vector3f(x, y, z), null);

			Vector3f edge2 = Vector3f.sub(new Vector3f(x + 0.3f, y + 1f, z + 0.7f), new Vector3f(x, y, z), null);

			Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getX(), texcoords.getY()),
					new Vector2f(texcoords.getK(), texcoords.getL()), null);

			Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getZ(), texcoords.getW()),
					new Vector2f(texcoords.getK(), texcoords.getL()), null);

			float f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

			Vector3f tangent = new Vector3f();

			tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
			tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
			tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
			tangent.normalise();

			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);

			Vector3f bitangent = new Vector3f();

			bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
			bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
			bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
			bitangent.normalise();

			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);

		}

		if (left) {
			Vector8f texcoords = texCoordsLeft();
			// left face
			tess.vertex3f(new Vector3f(x + 1f, y, z + 1f));
			tess.texture2f(new Vector2f(texcoords.getK(), texcoords.getL()));
			tess.normal3f(new Vector3f(1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), bfr, 0, 0));

			tess.vertex3f(new Vector3f(x + 1f, y, z));
			tess.texture2f(new Vector2f(texcoords.getX(), texcoords.getY()));
			tess.normal3f(new Vector3f(1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), bbr, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.7f, y + 1f, z + 0.3f));
			tess.texture2f(new Vector2f(texcoords.getZ(), texcoords.getW()));
			tess.normal3f(new Vector3f(1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), tbr, 0, 0));

			tess.vertex3f(new Vector3f(x + 0.7f, y + 1f, z + 0.7f));
			tess.texture2f(new Vector2f(texcoords.getI(), texcoords.getJ()));
			tess.normal3f(new Vector3f(1, 0.2f, 0));
			tess.data4f(new Vector4f(getId(), tfr, 0, 0));

			Vector3f edge1 = Vector3f.sub(new Vector3f(x + 1f, y, z), new Vector3f(x + 1f, y, z + 1f), null);

			Vector3f edge2 = Vector3f.sub(new Vector3f(x + 0.7f, y + 1f, z + 0.3f), new Vector3f(x + 1f, y, z + 1f),
					null);

			Vector2f deltaUV1 = Vector2f.sub(new Vector2f(texcoords.getX(), texcoords.getY()),
					new Vector2f(texcoords.getK(), texcoords.getL()), null);

			Vector2f deltaUV2 = Vector2f.sub(new Vector2f(texcoords.getZ(), texcoords.getW()),
					new Vector2f(texcoords.getK(), texcoords.getL()), null);

			float f = (float) (1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y));

			Vector3f tangent = new Vector3f();

			tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
			tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
			tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
			tangent.normalise();

			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);
			tess.tangent3f(tangent);

			Vector3f bitangent = new Vector3f();

			bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
			bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
			bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
			bitangent.normalise();

			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);
			tess.bitangent3f(bitangent);

		}

	}

	@Override
	public BoundingBox getBoundingBox(Vector3f pos) {
		return new BoundingBox(new Vector3(pos.x + 0.15, pos.y, pos.z + 0.15),
				new Vector3(pos.x + 0.85, pos.y + 1, pos.z + 0.85));
	}

}
