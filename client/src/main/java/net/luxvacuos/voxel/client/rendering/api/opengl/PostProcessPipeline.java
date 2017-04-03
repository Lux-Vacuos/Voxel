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

import static org.lwjgl.nanovg.NanoVG.NVG_IMAGE_FLIPY;
import static org.lwjgl.nanovg.NanoVG.nvgDeleteImage;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
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
	private String name;
	private int texture = 0;
	private Window window;

	public PostProcessPipeline(String name, Window window) {
		this.name = name;
		this.window = window;
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
	public void preRender(long nvg, CameraEntity camera) {
		auxs[0] = fbo;
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		for (IPostProcessPass deferredPass : imagePasses) {
			deferredPass.process(camera, previousViewMatrix, previousCameraPosition, auxs, quad);
		}
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		previousViewMatrix = Maths.createViewMatrix(camera);
		previousCameraPosition = camera.getPosition();
		if (texture == 0)
			texture = NRendering.generateImageFromTexture(nvg, auxs[0].getTexture(), width, height, NVG_IMAGE_FLIPY);
	}

	@Override
	public void dispose() {
		nvgDeleteImage(window.getNVGID(), texture);
		fbo.cleanUp();
		for (IPostProcessPass deferredPass : imagePasses) {
			deferredPass.dispose();
		}
	}

	@Override
	public FBO getFBO() {
		return fbo;
	}

	@Override
	public int getResultTexture() {
		return texture;
	}

	public String getName() {
		return name;
	}

}
