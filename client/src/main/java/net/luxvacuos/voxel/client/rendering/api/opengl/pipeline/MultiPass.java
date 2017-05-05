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

package net.luxvacuos.voxel.client.rendering.api.opengl.pipeline;

import net.luxvacuos.voxel.client.rendering.api.opengl.DeferredPipeline;

public class MultiPass extends DeferredPipeline {

	public MultiPass() {
		super("MultiPass");
	}

	private Lighting lighting;
	private VolumetricLight volumetricLight;
	private Sun sun;
	private BloomMask bloomMask;
	private GaussianHorizonal gH1, gH2;
	private GaussianVertical gV1, gV2;
	private Reflections reflections;
	private ColorCorrection colorCorrection;
	private LensFlares lensFlares;
	private LensFlareMod lensFlareMod;
	private Bloom bloom;
	private PointLightPass pointLightPass;

	@Override
	public void init() {
		sun = new Sun("Sun", width, height);
		super.imagePasses.add(sun);

		volumetricLight = new VolumetricLight("VolumetricLight", width, height);
		super.imagePasses.add(volumetricLight);

		gH1 = new GaussianHorizonal("GaussianHorizontal", width / 2, height / 2);
		super.imagePasses.add(gH1);

		gV1 = new GaussianVertical("GaussianVertical", width / 2, height / 2);
		super.imagePasses.add(gV1);

		lighting = new Lighting("Lighting", width, height);
		super.imagePasses.add(lighting);

		pointLightPass = new PointLightPass("PointLight", width, height);
		super.imagePasses.add(pointLightPass);

		//reflections = new Reflections("Reflections", width, height);
		//reflections.init();
		//super.imagePasses.add(reflections);

		bloomMask = new BloomMask("BloomMask", width, height);
		super.imagePasses.add(bloomMask);

		super.imagePasses.add(gH1);
		super.imagePasses.add(gV1);
		
		gH2 = new GaussianHorizonal("GaussianHorizontal", width / 4, height / 4);
		super.imagePasses.add(gH2);
		
		gV2 = new GaussianVertical("GaussianVertical", width / 4, height / 4);
		super.imagePasses.add(gV2);
		
		bloom = new Bloom("Bloom", width, height);
		super.imagePasses.add(bloom);
		
		lensFlares = new LensFlares("LensFlares", width, height);
		super.imagePasses.add(lensFlares);
		
		lensFlareMod = new LensFlareMod("LensFlaresMod", width, height);
		super.imagePasses.add(lensFlareMod);
		
		colorCorrection = new ColorCorrection("ColorCorrection", width, height);
		super.imagePasses.add(colorCorrection);
		
	}

}
