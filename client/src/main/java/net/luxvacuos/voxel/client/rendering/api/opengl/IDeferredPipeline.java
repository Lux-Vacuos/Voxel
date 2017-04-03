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

package net.luxvacuos.voxel.client.rendering.api.opengl;

import java.util.List;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.CubeMapTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Texture;
import net.luxvacuos.voxel.universal.core.IWorldSimulation;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public interface IDeferredPipeline extends IDisposable {

	public void init();

	public void begin();

	public void end();

	public void preRender(CameraEntity camera, Vector3d lightPosition, Vector3d invertedLightPosition,
			IWorldSimulation clientWorldSimulation, List<Light> lights, CubeMapTexture irradianceCapture,
			CubeMapTexture environmentMap, Texture brdfLUT, float exposure);

	public void render(FBO postProcess);

	public RenderingPipelineFBO getMainFBO();

}
