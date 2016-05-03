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

public class Forward extends RenderingPipeline {

	public Forward() throws Exception {
		super();
	}

	private ForwardImagePass pass;
	private SunPass sunPass;

	@Override
	public void init(GameResources gm) throws Exception {
		sunPass = new SunPass(width, height);
		sunPass.setName("Sun");
		sunPass.init();
		super.imagePasses.add(sunPass);
		pass = new ForwardImagePass(width, height);
		pass.setName("Forward");
		pass.init();
		super.imagePasses.add(pass);
	}

	@Override
	public void dispose() {
	}

}
