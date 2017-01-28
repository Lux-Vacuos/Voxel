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

package net.luxvacuos.voxel.client.ecs;

import com.badlogic.ashley.core.ComponentMapper;

import net.luxvacuos.voxel.client.ecs.components.ProjectionMatrix;
import net.luxvacuos.voxel.client.ecs.components.Renderable;
import net.luxvacuos.voxel.client.ecs.components.ViewMatrix;
import net.luxvacuos.voxel.universal.ecs.Components;

public class ClientComponents extends Components {

	private ClientComponents() { }
	
	public static final ComponentMapper<Renderable> RENDERABLE = ComponentMapper.getFor(Renderable.class);
	
	public static final ComponentMapper<ViewMatrix> VIEW_MATRIX = ComponentMapper.getFor(ViewMatrix.class);
	
	public static final ComponentMapper<ProjectionMatrix> PROJECTION_MATRIX = ComponentMapper.getFor(ProjectionMatrix.class);

}
