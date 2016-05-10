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

	private LightingPass lightingPass;
	private SunPass sunPass;
	private BloomMaskPass bloomMaskPass;
	private BloomHorizonal bloomHorizontal;
	private BloomVertical bloomVertical;
	private SSRPass ssrPass;
	private AmbientOcclusionPass ambientOcclusionPass;

	@Override
	public void init(GameResources gm) throws Exception {
		sunPass = new SunPass(width, height);
		sunPass.setName("Sun");
		sunPass.init();
		super.imagePasses.add(sunPass);
		lightingPass = new LightingPass(width, height);
		lightingPass.setName("Lighting");
		lightingPass.init();
		super.imagePasses.add(lightingPass);
		ambientOcclusionPass = new AmbientOcclusionPass(width, height);
		ambientOcclusionPass.setName("AmbientOcclusion");
		ambientOcclusionPass.init();
		super.imagePasses.add(ambientOcclusionPass);
		bloomMaskPass = new BloomMaskPass(width, height);
		bloomMaskPass.setName("BloomMask");
		bloomMaskPass.init();
		super.imagePasses.add(bloomMaskPass);
		bloomHorizontal = new BloomHorizonal(width, height);
		bloomHorizontal.setName("BloomHorizontal");
		bloomHorizontal.init();
		super.imagePasses.add(bloomHorizontal);
		bloomVertical = new BloomVertical(width, height);
		bloomVertical.setName("BloomVertical");
		bloomVertical.init();
		super.imagePasses.add(bloomVertical);
		ssrPass = new SSRPass(width, height);
		ssrPass.setName("SSR");
		ssrPass.init();
		super.imagePasses.add(ssrPass);
	}

	@Override
	public void dispose() {
	}

}
