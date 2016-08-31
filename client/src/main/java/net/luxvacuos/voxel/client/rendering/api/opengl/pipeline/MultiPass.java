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

	public MultiPass() {
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
	public void init(GameResources gm) {
		sun = new Sun("Sun", width, height);
		sun.init();
		super.imagePasses.add(sun);

		volumetricLight = new VolumetricLight("VolumetricLight", width, height);
		volumetricLight.init();
		super.imagePasses.add(volumetricLight);

		gaussianHorizontal = new GaussianHorizonal("GaussianHorizontal", width, height);
		gaussianHorizontal.init();
		super.imagePasses.add(gaussianHorizontal);

		gaussianVertical = new GaussianVertical("GaussianVertical", width, height);
		gaussianVertical.init();
		super.imagePasses.add(gaussianVertical);

		lighting = new Lighting("Lighting", width, height);
		lighting.init();
		super.imagePasses.add(lighting);

		ambientOcclusion = new AmbientOcclusion("AmbientOcclusion", width, height);
		ambientOcclusion.init();
		super.imagePasses.add(ambientOcclusion);

		bloomMask = new BloomMask("BloomMask", width, height);
		bloomMask.init();
		super.imagePasses.add(bloomMask);

		super.imagePasses.add(gaussianHorizontal);

		super.imagePasses.add(gaussianVertical);

		colorCorrection = new ColorCorrection("ColorCorrection", width, height);
		colorCorrection.init();
		super.imagePasses.add(colorCorrection);

		screenSpaceReflections = new ScreenSpaceReflections("ScreenSpaceReflections", width, height);
		screenSpaceReflections.init();
		super.imagePasses.add(screenSpaceReflections);
	}

}
