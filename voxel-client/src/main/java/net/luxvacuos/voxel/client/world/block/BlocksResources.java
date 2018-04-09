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

package net.luxvacuos.voxel.client.world.block;

import static org.lwjgl.opengl.GL11.GL_NEAREST;

import org.joml.Vector2f;
import org.joml.Vector4f;

import net.luxvacuos.lightengine.client.rendering.IResourceLoader;
import net.luxvacuos.lightengine.client.rendering.opengl.objects.Material;
import net.luxvacuos.voxel.client.resources.models.TessellatorTextureAtlas;

public class BlocksResources {

	private static TessellatorTextureAtlas tessellatorTextureAtlas;
	private static Material material;

	public static void init(IResourceLoader loader) {
		var blocks = loader.loadTexture("textures/blocks.png", GL_NEAREST, false);
		var blocks_n = loader.loadTextureMisc("textures/blocks_n.png", GL_NEAREST, false);
		var blocks_r = loader.loadTextureMisc("textures/blocks_r.png", GL_NEAREST, false);
		var blocks_m = loader.loadTextureMisc("textures/blocks_m.png", GL_NEAREST, false);
		material = new Material(new Vector4f(1f), new Vector4f(0f), 1f, 1f);
		material.setDiffuseTexture(blocks);
		material.setNormalTexture(blocks_n);
		material.setRoughnessTexture(blocks_r);
		material.setMetallicTexture(blocks_m);
		tessellatorTextureAtlas = new TessellatorTextureAtlas(256, 256);
		loadTexCoords();
	}

	public static void dispose() {
		material.dispose();
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
		tessellatorTextureAtlas.registerTextureCoords("GlassPane", new Vector2f(0, 16));
		tessellatorTextureAtlas.registerTextureCoords("GoldOre", new Vector2f(16, 16));
		tessellatorTextureAtlas.registerTextureCoords("Lava", new Vector2f(32, 16));
		tessellatorTextureAtlas.registerTextureCoords("LavaSide", new Vector2f(48, 16));
		tessellatorTextureAtlas.registerTextureCoords("Pedestal", new Vector2f(64, 16));
		tessellatorTextureAtlas.registerTextureCoords("PedestalBottom", new Vector2f(80, 16));
		tessellatorTextureAtlas.registerTextureCoords("PedestalTop", new Vector2f(96, 16));
		tessellatorTextureAtlas.registerTextureCoords("Missing", new Vector2f(240, 240));
	}

	public static TessellatorTextureAtlas getTessellatorTextureAtlas() {
		return tessellatorTextureAtlas;
	}

	public static Material getMaterial() {
		return material;
	}

}