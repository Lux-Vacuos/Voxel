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

package net.luxvacuos.voxel.client.rendering.api.opengl;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;

/**
 *
 *
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public abstract class ImagePass {

	private DeferredShadingShader shader;
	private ImagePassFBO fbo;

	private int width, height;
	private String name;

	public ImagePass(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void init() throws Exception {
		fbo = new ImagePassFBO(width, height);
		shader = new DeferredShadingShader(name);
		shader.start();
		shader.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader.connectTextureUnits();
		shader.loadResolution(new Vector2f(GameResources.instance().getDisplay().getDisplayWidth(),
				GameResources.instance().getDisplay().getDisplayHeight()));
		shader.loadSkyColor(VoxelVariables.skyColor);
		shader.stop();
	}

	public void begin(GameResources gm, Matrix4f previousViewMatrix, Vector3f previousCameraPosition) {
		fbo.begin();
		shader.start();
		shader.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
				Maths.createViewMatrix(gm.getCamera()), width, height));
		shader.loadExposure(4f);
		shader.loadTime(gm.getWorldSimulation().getTime());
	}

	public abstract void render(ImagePassFBO[] auxs, RenderingPipeline pipe);

	public void end() {
		shader.stop();
		fbo.end();
	}

	public void dispose() {
		shader.cleanUp();
		fbo.cleanUp();
	}

	public void setName(String name) {
		this.name = name;
	}

	public ImagePassFBO getFbo() {
		return fbo;
	}

}
