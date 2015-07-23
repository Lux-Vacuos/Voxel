/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.graphics;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class WaterFrameBuffers {

	private int reflectionFrameBuffer;
	private int reflectionTexture;
	private int reflectionDepthBuffer;

	private int refractionFrameBuffer;
	private int refractionTexture;
	private int refractionDepthTexture;

	public WaterFrameBuffers() {// call when loading the game
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}

	public void cleanUp() {// call when closing the game
		glDeleteFramebuffers(reflectionFrameBuffer);
		glDeleteTextures(reflectionTexture);
		glDeleteRenderbuffers(reflectionDepthBuffer);
		glDeleteFramebuffers(refractionFrameBuffer);
		glDeleteTextures(refractionTexture);
		glDeleteTextures(refractionDepthTexture);
	}

	public void bindReflectionFrameBuffer() {// call before rendering to this
												// FBO
		bindFrameBuffer(reflectionFrameBuffer,
				KernelConstants.REFLECTION_WIDTH,
				KernelConstants.REFLECTION_HEIGHT);
	}

	public void bindRefractionFrameBuffer() {// call before rendering to this
		// FBO
		bindFrameBuffer(refractionFrameBuffer,
				KernelConstants.REFRACTION_WIDTH,
				KernelConstants.REFRACTION_HEIGHT);
	}

	public void unbindCurrentFrameBuffer() {// call to switch to default frame
											// buffer
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public int getReflectionTexture() {
		return reflectionTexture;
	}

	public int getRefractionTexture() {
		return refractionTexture;
	}

	public int getRefractionDepthTexture() {
		return refractionDepthTexture;
	}

	private void initialiseReflectionFrameBuffer() {
		reflectionFrameBuffer = createFrameBuffer();
		reflectionTexture = createTextureAttachment(
				KernelConstants.REFLECTION_WIDTH,
				KernelConstants.REFLECTION_HEIGHT);
		reflectionDepthBuffer = createDepthBufferAttachment(
				KernelConstants.REFLECTION_WIDTH,
				KernelConstants.REFLECTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}

	private void initialiseRefractionFrameBuffer() {
		refractionFrameBuffer = createFrameBuffer();
		refractionTexture = createTextureAttachment(
				KernelConstants.REFRACTION_WIDTH,
				KernelConstants.REFRACTION_HEIGHT);
		refractionDepthTexture = createDepthTextureAttachment(
				KernelConstants.REFRACTION_WIDTH,
				KernelConstants.REFRACTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}

	private void bindFrameBuffer(int frameBuffer, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, 0);// To make sure the texture isn't bound
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glViewport(0, 0, width, height);
	}

	private int createFrameBuffer() {
		int frameBuffer = glGenFramebuffers();
		// generate name for frame buffer
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		// create the framebuffer
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		// indicate that we will always render to color attachment 0
		return frameBuffer;
	}

	private int createTextureAttachment(int width, int height) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB,
				GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
		return texture;
	}

	private int createDepthTextureAttachment(int width, int height) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0,
				GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
		return texture;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width,
				height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
				GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

}