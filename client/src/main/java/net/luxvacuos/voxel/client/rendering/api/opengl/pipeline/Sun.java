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

package net.luxvacuos.voxel.client.rendering.api.opengl.pipeline;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import net.luxvacuos.voxel.client.rendering.api.opengl.ImagePass;
import net.luxvacuos.voxel.client.rendering.api.opengl.ImagePassFBO;
import net.luxvacuos.voxel.client.rendering.api.opengl.RenderingPipeline;

public class Sun extends ImagePass {


	public Sun(String name, int width, int height) {
		super(name, width, height);
	}

	@Override
	public void render(ImagePassFBO[] auxs, RenderingPipeline pipe) {
		auxs[1] = getFbo();
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, pipe.getMainFBO().getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, pipe.getMainFBO().getData1Tex());
	}

}
