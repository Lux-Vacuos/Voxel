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

import net.luxvacuos.voxel.client.rendering.api.opengl.RenderingPipeline;
import net.luxvacuos.voxel.client.resources.GameResources;

public class MultiPass extends RenderingPipeline {

	public MultiPass() throws Exception {
		super("MultiPass");
	}

	private Lighting lighting;
	private VolumetricLight volumetricLight;
	private Sun sun;
	private BloomMask bloomMask;
	private GaussianHorizonal gaussianHorizontal;
	private GaussianVertical gaussianVertical;
	private ScreenSpaceReflections screenSpaceReflections;
	private AmbientOcclusion ambientOcclusion;
	private ColorCorrection colorCorrection;

	@Override
	public void init(GameResources gm) throws Exception {
		sun = new Sun(width, height);
		sun.setName("Sun");
		sun.init();
		super.imagePasses.add(sun);

		volumetricLight = new VolumetricLight(width, height);
		volumetricLight.setName("VolumetricLight");
		volumetricLight.init();
		super.imagePasses.add(volumetricLight);
		
		gaussianHorizontal = new GaussianHorizonal(width, height);
		gaussianHorizontal.setName("GaussianHorizontal");
		gaussianHorizontal.init();
		super.imagePasses.add(gaussianHorizontal);

		gaussianVertical = new GaussianVertical(width, height);
		gaussianVertical.setName("GaussianVertical");
		gaussianVertical.init();
		super.imagePasses.add(gaussianVertical);

		lighting = new Lighting(width, height);
		lighting.setName("Lighting");
		lighting.init();
		super.imagePasses.add(lighting);

		ambientOcclusion = new AmbientOcclusion(width, height);
		ambientOcclusion.setName("AmbientOcclusion");
		ambientOcclusion.init();
		super.imagePasses.add(ambientOcclusion);

		bloomMask = new BloomMask(width, height);
		bloomMask.setName("BloomMask");
		bloomMask.init();
		super.imagePasses.add(bloomMask);

		super.imagePasses.add(gaussianHorizontal);
		
		super.imagePasses.add(gaussianVertical);

		colorCorrection = new ColorCorrection(width, height);
		colorCorrection.setName("ColorCorrection");
		colorCorrection.init();
		super.imagePasses.add(colorCorrection);

		screenSpaceReflections = new ScreenSpaceReflections(width, height);
		screenSpaceReflections.setName("ScreenSpaceReflections");
		screenSpaceReflections.init();
		super.imagePasses.add(screenSpaceReflections);
	}

	@Override
	public void dispose() {
	}

}
