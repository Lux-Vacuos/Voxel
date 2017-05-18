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

package net.luxvacuos.voxel.client.core.states;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import net.luxvacuos.voxel.client.core.GraphicalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.menus.CrashWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;

public class CrashState extends AbstractState {

	public static Throwable t;

	public CrashState() {
		super(StateNames.CRASH);
	}

	@Override
	public void start() {
		GraphicalSubsystem.getWindowManager()
				.addWindow(new CrashWindow(0, (int) REGISTRY.getRegistryItem("/Voxel/Display/height"),
						(int) REGISTRY.getRegistryItem("/Voxel/Display/width"),
						(int) REGISTRY.getRegistryItem("/Voxel/Display/height"), t));
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		GraphicalSubsystem.getWindowManager().render();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		GraphicalSubsystem.getWindowManager().update(delta);
	}

}
