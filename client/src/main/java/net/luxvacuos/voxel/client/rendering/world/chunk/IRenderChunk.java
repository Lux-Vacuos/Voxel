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

package net.luxvacuos.voxel.client.rendering.world.chunk;

import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.rendering.api.opengl.ShadowFBO;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public interface IRenderChunk extends IChunk {
	
	public void render(CameraEntity camera, CameraEntity sunCamera, ClientWorldSimulation clientWorldSimulation, ShadowFBO shadow);
	
	public void renderShadow(CameraEntity sunCamera);
	
	public void renderOcclusion(CameraEntity camera);
	
	public boolean needsMeshRebuild();
	
	public void markMeshRebuild();
	
	public Tessellator getTessellator();

}
