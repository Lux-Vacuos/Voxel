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

package net.luxvacuos.voxel.client.world;

import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.lightengine.client.ecs.entities.CameraEntity;
import net.luxvacuos.lightengine.client.rendering.opengl.Frustum;
import net.luxvacuos.voxel.client.rendering.world.IRenderWorld;
import net.luxvacuos.voxel.client.world.dimension.RenderDimension;
import net.luxvacuos.voxel.universal.world.World;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class RenderWorld extends World implements IRenderWorld {

	public RenderWorld(String name) {
		super(name);
	}

	@Override
	protected IDimension createDimension(int id, TagCompound data) {
		return new RenderDimension(this, data, id);
	}

	@Override
	public void render(CameraEntity camera, Frustum frustum) {
		((RenderDimension) this.getActiveDimension()).render(camera, frustum);

	}

	@Override
	public void renderOcclusion(CameraEntity camera, Frustum frustum) {
		((RenderDimension) this.getActiveDimension()).renderOcclusion(camera, frustum);

	}

	@Override
	public void renderShadow(CameraEntity sunCamera, Frustum frustum) {
		((RenderDimension) this.getActiveDimension()).renderShadow(sunCamera, frustum);

	}

}
