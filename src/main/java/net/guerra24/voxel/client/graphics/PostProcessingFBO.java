package net.guerra24.voxel.client.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import net.guerra24.voxel.client.util.Logger;

public class PostProcessingFBO {

	private int fbo, diffuseRT, positionRT, normalRT, depthBuffer;

	private int diffuseTex, positionTex, normalTex, depthTex;

	public PostProcessingFBO(int width, int height) {
		init(width, height);
	}

	private void init(int width, int height) {

		fbo = glGenFramebuffers();
		diffuseRT = glGenRenderbuffers();
		positionRT = glGenRenderbuffers();
		normalRT = glGenRenderbuffers();
		depthBuffer = glGenRenderbuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);

		glBindRenderbuffer(GL_RENDERBUFFER, diffuseRT);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_RGBA, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, diffuseRT);

		glBindRenderbuffer(GL_RENDERBUFFER, positionRT);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_RGBA32F, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_RENDERBUFFER, positionRT);

		glBindRenderbuffer(GL_RENDERBUFFER, normalRT);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_RGBA16F, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, GL_RENDERBUFFER, normalRT);

		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);

		diffuseTex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, diffuseTex);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, diffuseTex, 0);

		positionTex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, positionTex);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, positionTex, 0);

		normalTex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, normalTex);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, GL_TEXTURE_2D, normalTex, 0);

		depthTex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, depthTex);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT,
				(ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTex, 0);

		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		if (status != GL_FRAMEBUFFER_COMPLETE)
			Logger.error("FBO initialization failed");

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void cleanUp() {
		glDeleteTextures(diffuseTex);
		glDeleteTextures(positionTex);
		glDeleteTextures(normalTex);
		glDeleteTextures(depthTex);
		glDeleteFramebuffers(fbo);
		glDeleteRenderbuffers(diffuseRT);
		glDeleteRenderbuffers(positionRT);
		glDeleteRenderbuffers(normalRT);
		glDeleteRenderbuffers(depthBuffer);
	}

	public void begin() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		int buffer[] = { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2};
		IntBuffer buffers = BufferUtils.createIntBuffer(buffer.length);
		buffers.put(buffer);
		buffers.flip();
		glDrawBuffers(buffers);
	}

	public void end() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int getDepthBuffer() {
		return depthBuffer;
	}

	public int getDiffuseRT() {
		return diffuseRT;
	}

	public int getPositionRT() {
		return positionRT;
	}

	public int getNormalRT() {
		return normalRT;
	}

	public int getDiffuseTex() {
		return diffuseTex;
	}

	public int getPositionTex() {
		return positionTex;
	}

	public int getNormalTex() {
		return normalTex;
	}

	public int getFbo() {
		return fbo;
	}

	public int getDepthTex() {
		return depthTex;
	}
}
