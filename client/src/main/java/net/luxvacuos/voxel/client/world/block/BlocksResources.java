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

package net.luxvacuos.voxel.client.world.block;

import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Texture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.resources.models.TessellatorTextureAtlas;

public class BlocksResources {

	private static TessellatorTextureAtlas tessellatorTextureAtlas;

	private static int normalMap;
	private static int heightMap;
	private static int pbrMap;
	private static TexturedModel node;

	public static void createBlocks(ResourceLoader loader) {

		tessellatorTextureAtlas = new TessellatorTextureAtlas(256, 256, loader.loadTextureBlocks("blocks"));
		normalMap = loader.loadTextureMisc("blocks_normal");
		heightMap = loader.loadTextureMisc("blocks_height");
		pbrMap = loader.loadTextureMisc("blocks_pbr");

		RawModel rNode = loader.loadObjModel("Node");
		Texture tNode = new Texture(loader.loadTextureBlocks("Node"));
		//node = new TexturedModel(rNode, tNode);
		loadTexCoords();
	}

	private static void loadTexCoords() {
		tessellatorTextureAtlas.registerTextureCoords("Cobblestone", new Vector2d(0, 0));
		tessellatorTextureAtlas.registerTextureCoords("Grass", new Vector2d(16, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSide", new Vector2d(32, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSideSnow", new Vector2d(48, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSnow", new Vector2d(64, 0));
		tessellatorTextureAtlas.registerTextureCoords("Dirt", new Vector2d(80, 0));
		tessellatorTextureAtlas.registerTextureCoords("Indes", new Vector2d(96, 0));
		tessellatorTextureAtlas.registerTextureCoords("Ice", new Vector2d(112, 0));
		tessellatorTextureAtlas.registerTextureCoords("Leaves", new Vector2d(128, 0));
		tessellatorTextureAtlas.registerTextureCoords("LeavesSnow", new Vector2d(144, 0));
		tessellatorTextureAtlas.registerTextureCoords("Sand", new Vector2d(160, 0));
		tessellatorTextureAtlas.registerTextureCoords("SandSnow", new Vector2d(176, 0));
		tessellatorTextureAtlas.registerTextureCoords("Water", new Vector2d(192, 0));
		tessellatorTextureAtlas.registerTextureCoords("Stone", new Vector2d(208, 0));
		tessellatorTextureAtlas.registerTextureCoords("Wood", new Vector2d(224, 0));
		tessellatorTextureAtlas.registerTextureCoords("DimOre", new Vector2d(240, 0));
		tessellatorTextureAtlas.registerTextureCoords("Glass", new Vector2d(0, 16));
		tessellatorTextureAtlas.registerTextureCoords("GoldOre", new Vector2d(16, 16));
		tessellatorTextureAtlas.registerTextureCoords("Lava", new Vector2d(32, 16));
		tessellatorTextureAtlas.registerTextureCoords("LavaSide", new Vector2d(48, 16));
		tessellatorTextureAtlas.registerTextureCoords("Pedestal", new Vector2d(64, 16));
		tessellatorTextureAtlas.registerTextureCoords("PedestalBottom", new Vector2d(80, 16));
		tessellatorTextureAtlas.registerTextureCoords("PedestalTop", new Vector2d(96, 16));
		tessellatorTextureAtlas.registerTextureCoords("Missing", new Vector2d(240, 240));
	}

	public static TessellatorTextureAtlas getTessellatorTextureAtlas() {
		return tessellatorTextureAtlas;
	}

	public static int getHeightMap() {
		return heightMap;
	}

	public static int getNormalMap() {
		return normalMap;
	}

	public static int getPbrMap() {
		return pbrMap;
	}

	public static TexturedModel getNode() {
		return node;
	}

}