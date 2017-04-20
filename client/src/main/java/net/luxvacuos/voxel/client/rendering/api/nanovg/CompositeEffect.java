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

package net.luxvacuos.voxel.client.rendering.api.nanovg;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluBindFramebuffer;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.nanovg.NVGLUFramebuffer;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.shaders.WindowManagerShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public abstract class CompositeEffect implements IDisposable {

	private WindowManagerShader shader;

	public CompositeEffect(int width, int height, String name) {
		shader = new WindowManagerShader(name);
		shader.start();
		shader.loadResolution(new Vector2f(width, height));
		shader.stop();
	}

	public void render(NVGLUFramebuffer[] fbos, RawModel quad, Window wnd, IWindow window) {
		float borderSize = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		float titleBarHeight = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
		nvgluBindFramebuffer(wnd.getNVGID(), fbos[0]);
		shader.start();
		if (window.getTitleBar().isEnabled() && window.hasDecorations())
			shader.loadFrame(new Vector4f(window.getX() - borderSize, window.getY() + titleBarHeight,
					window.getWidth() + borderSize * 2f, window.getHeight() + titleBarHeight + borderSize));
		else if (!window.hasDecorations())
			shader.loadFrame(new Vector4f(window.getX(), window.getY(), window.getWidth(), window.getHeight()));
		else
			shader.loadFrame(new Vector4f(window.getX() - borderSize, window.getY() + borderSize,
					window.getWidth() + borderSize * 2f, window.getHeight() + borderSize * 2f));
		shader.loadBlurBehind(window.hasBlurBehind());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, fbos[1].texture());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, window.getFBO().texture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
		nvgluBindFramebuffer(wnd.getNVGID(), null);
	}

	@Override
	public void dispose() {
		shader.dispose();
	}

}
