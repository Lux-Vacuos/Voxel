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

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.voxel.client.resources.Loader;
import net.luxvacuos.voxel.client.resources.models.ModelTexture;
import net.luxvacuos.voxel.client.resources.models.RawModel;
import net.luxvacuos.voxel.client.resources.models.TessellatorTextureAtlas;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;

public class BlocksResources {

	public static TexturedModel cubeTorch;

	private static TessellatorTextureAtlas tessellatorTextureAtlas;

	private static int normalMap;
	private static int heightMap;
	private static int specularMap;

	public static void createBlocks(Loader loader) {

		RawModel torch = loader.getObjLoader().loadObjModel("Torch");

		tessellatorTextureAtlas = new TessellatorTextureAtlas(256, 256, loader.loadTextureBlocks("blocks"));
		normalMap = loader.loadTextureBlocks("blocks_normal");
		heightMap = loader.loadTextureBlocks("blocks_height");
		specularMap = loader.loadTextureBlocks("blocks_specular");

		ModelTexture torchTex = new ModelTexture(loader.loadTextureBlocks("Torch"));
		cubeTorch = new TexturedModel(torch, torchTex);
		loadTexCoords();
	}

	private static void loadTexCoords() {
		tessellatorTextureAtlas.registerTextureCoords("Cobblestone", new Vector2f(0, 0));
		tessellatorTextureAtlas.registerTextureCoords("Grass", new Vector2f(16, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSide", new Vector2f(32, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSideSnow", new Vector2f(48, 0));
		tessellatorTextureAtlas.registerTextureCoords("GrassSnow", new Vector2f(64, 0));
		tessellatorTextureAtlas.registerTextureCoords("Dirt", new Vector2f(80, 0));
		tessellatorTextureAtlas.registerTextureCoords("Indes", new Vector2f(96, 0));
		tessellatorTextureAtlas.registerTextureCoords("Ice", new Vector2f(112, 0));
		tessellatorTextureAtlas.registerTextureCoords("Leaves", new Vector2f(128, 0));
		tessellatorTextureAtlas.registerTextureCoords("LeavesSnow", new Vector2f(144, 0));
		tessellatorTextureAtlas.registerTextureCoords("Sand", new Vector2f(160, 0));
		tessellatorTextureAtlas.registerTextureCoords("SandSnow", new Vector2f(176, 0));
		tessellatorTextureAtlas.registerTextureCoords("Water", new Vector2f(192, 0));
		tessellatorTextureAtlas.registerTextureCoords("Stone", new Vector2f(208, 0));
		tessellatorTextureAtlas.registerTextureCoords("Wood", new Vector2f(224, 0));
		tessellatorTextureAtlas.registerTextureCoords("DimOre", new Vector2f(240, 0));
		tessellatorTextureAtlas.registerTextureCoords("Glass", new Vector2f(0, 16));
		tessellatorTextureAtlas.registerTextureCoords("GoldOre", new Vector2f(16, 16));
		tessellatorTextureAtlas.registerTextureCoords("Lava", new Vector2f(32, 16));
		tessellatorTextureAtlas.registerTextureCoords("LavaSide", new Vector2f(48, 16));
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

	public static int getSpecularMap() {
		return specularMap;
	}

}