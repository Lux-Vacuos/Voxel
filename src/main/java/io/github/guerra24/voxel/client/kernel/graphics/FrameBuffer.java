package io.github.guerra24.voxel.client.kernel.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;

import io.github.guerra24.voxel.client.kernel.graphics.opengl.Display;

public class FrameBuffer {
	private int FrameBuffer;
	private int DepthBuffer;
	private int DepthTexture;

	public FrameBuffer(boolean depth) {
		initialiseFrameBuffer(depth);
	}

	private void initialiseFrameBuffer(boolean depth) {
		FrameBuffer = createFrameBuffer();
		if (depth) {
			DepthTexture = createDepthTextureAttachment(512, 512);
		} else {
			DepthTexture = createTextureAttachment(512, 512);
		}
		DepthBuffer = createDepthBufferAttachment(512, 512);
		end();
	}

	public void begin() {
		bindFrameBuffer(FrameBuffer, 512, 512);
	}

	public void end() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	private void bindFrameBuffer(int frameBuffer, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glViewport(0, 0, width, height);
	}

	private int createFrameBuffer() {
		int frameBuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		return frameBuffer;
	}

	private int createTextureAttachment(int width, int height) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
		return texture;
	}

	private int createDepthTextureAttachment(int width, int height) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT,
				(ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
		return texture;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

	public void cleanUp() {
		glDeleteFramebuffers(FrameBuffer);
		glDeleteRenderbuffers(DepthBuffer);
		glDeleteTextures(DepthTexture);
	}

	public int getFrameBuffer() {
		return FrameBuffer;
	}

	public int getDepthBuffer() {
		return DepthBuffer;
	}

	public int getDepthTexture() {
		return DepthTexture;
	}

}
