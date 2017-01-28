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

package net.luxvacuos.voxel.client.rendering.api.opengl;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.core.TaskManager;

public abstract class PostProcessPipeline implements IPostProcessPipeline {

	protected FBO fbo;
	protected int width, height;
	protected List<IPostProcessPass> imagePasses;
	private Matrix4d previousViewMatrix;
	private Vector3d previousCameraPosition;
	private RawModel quad;
	private FBO[] auxs;
	private DeferredShadingShader finalShader;
	private String name;

	public PostProcessPipeline(String name) {
		this.name = name;
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		width = (int) (window.getWidth() * window.getPixelRatio());
		height = (int) (window.getHeight() * window.getPixelRatio());

		if (width > GLUtil.getTextureMaxSize())
			width = GLUtil.getTextureMaxSize();
		if (height > GLUtil.getTextureMaxSize())
			height = GLUtil.getTextureMaxSize();
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = window.getResourceLoader().loadToVAO(positions, 2);
		fbo = new FBO(width, height);
		imagePasses = new ArrayList<>();
		auxs = new FBO[3];

		previousCameraPosition = new Vector3d();
		previousViewMatrix = new Matrix4d();
		finalShader = new DeferredShadingShader("Final");
		finalShader.start();
		finalShader.loadTransformation(Maths.createTransformationMatrix(new Vector2d(0, 0), new Vector2d(1, 1)));
		finalShader.loadResolution(new Vector2d(window.getWidth(), window.getHeight()));
		finalShader.loadSkyColor(ClientVariables.skyColor);
		finalShader.stop();
		init();
		for (IPostProcessPass deferredPass : imagePasses) {
			TaskManager.addTask(() -> deferredPass.init());
		}
	}

	@Override
	public void begin() {
		fbo.begin();
	}

	@Override
	public void end() {
		fbo.end();
	}

	@Override
	public void preRender(CameraEntity camera) {
		auxs[0] = fbo;
		for (IPostProcessPass deferredPass : imagePasses) {
			deferredPass.process(camera, previousViewMatrix, previousCameraPosition, auxs, quad);
		}
		previousViewMatrix = Maths.createViewMatrix(camera);
		previousCameraPosition = camera.getPosition();
	}

	@Override
	public void render() {
		finalShader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, auxs[0].getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		finalShader.stop();
	}

	@Override
	public void dispose() {
		fbo.cleanUp();
		for (IPostProcessPass deferredPass : imagePasses) {
			deferredPass.dispose();
		}
		finalShader.dispose();
	}

	@Override
	public FBO getFBO() {
		return fbo;
	}

	public String getName() {
		return name;
	}

}
