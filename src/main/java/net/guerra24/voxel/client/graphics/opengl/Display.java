package net.guerra24.voxel.client.graphics.opengl;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_DEBUG;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreateGL3;
import static org.lwjgl.nanovg.NanoVGGL3.nvgDeleteGL3;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.NVXGPUMemoryInfo;
import org.lwjgl.opengl.WGLAMDGPUAssociation;

import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.util.Logger;

/**
 * 
 * A new complete re-writed Display Manager.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * 
 */
public class Display extends Window {

	private GLFWVidMode vidmode;
	private double lastLoopTime;
	private float timeCount;

	private DisplayUtils displayUtils;

	private IntBuffer maxVram = BufferUtils.createIntBuffer(1);
	private IntBuffer usedVram = BufferUtils.createIntBuffer(1);
	private boolean nvidia;
	private boolean amd;

	public Display() {
		displayUtils = new DisplayUtils();
	}

	public void create(int width, int height, String title, boolean vsync, boolean visible, boolean resizable,
			ContextFormat format, String[] icons) {
		Logger.log("Creating Window");
		super.displayWidth = width;
		super.displayHeight = height;
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, visible ? 1 : 0);
		super.displayResizable = resizable;
		glfwWindowHint(GLFW_RESIZABLE, resizable ? 1 : 0);

		format.create();

		super.window = glfwCreateWindow(displayWidth, displayHeight, title, NULL, NULL);
		if (super.window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		super.createCallbacks();
		super.setCallbacks();
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(super.window, (vidmode.width() - super.displayWidth) / 2,
				(vidmode.height() - super.displayHeight) / 2);
		glfwMakeContextCurrent(super.window);
		glfwSwapInterval(vsync ? 1 : 0);

		try {
			ByteBuffer[] icon_array = new ByteBuffer[icons.length];
			for (int i = 0; i < icons.length; i++) {
				icon_array[i] = ByteBuffer.allocateDirect(1);
				String path = icons[i];
				icon_array[i] = displayUtils.loadIcon(path);
			}
		} catch (IOException e) {
			Logger.error("Failed to load icons");
			e.printStackTrace();
			System.exit(-1);
		}
		createCapabilities();
		super.vg = nvgCreateGL3(NVG_ANTIALIAS | NVG_STENCIL_STROKES | NVG_DEBUG);
		if (vg == NULL)
			throw new RuntimeException("Fail to create NanoVG");

		lastLoopTime = getTime();
		ByteBuffer w = BufferUtils.createByteBuffer(4);
		ByteBuffer h = BufferUtils.createByteBuffer(4);
		glfwGetFramebufferSize(window, w, h);
		super.displayFramebufferWidth = w.getInt(0);
		super.displayFramebufferHeight = h.getInt(0);

		glfwGetWindowSize(super.window, w, h);
		super.displayWidth = w.getInt(0);
		super.displayHeight = h.getInt(0);
		super.pixelRatio = (float) displayFramebufferWidth / (float) displayWidth;
		glViewport(0, 0, (int) (displayWidth * pixelRatio), (int) (displayHeight * pixelRatio));

		if (glGetString(GL_VENDOR).contains("NVIDIA"))
			nvidia = true;
		else if (glGetString(GL_VENDOR).contains("AMD"))
			amd = true;
		if (nvidia)
			glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_DEDICATED_VIDMEM_NVX, maxVram);
		else if (amd)
			glGetIntegerv(WGLAMDGPUAssociation.WGL_GPU_RAM_AMD, maxVram);
		if (nvidia)
			Logger.log("Max VRam: " + maxVram.get(0) + "KB");
		else if (amd)
			Logger.log("Max VRam: " + maxVram.get(0) + "MB");
		super.displayCreated = true;
	}

	public void beingNVGFrame() {
		nvgBeginFrame(super.vg, super.displayWidth, super.displayHeight, super.pixelRatio);
	}

	public void endNVGFrame() {
		nvgEndFrame(super.vg);
	}

	public void updateDisplay(int fps) {
		glfwSwapBuffers(super.window);
		glfwPollEvents();
		Mouse.poll();
		displayUtils.sync(fps);
	}

	public void closeDisplay() {
		nvgDeleteGL3(super.vg);
		releaseCallbacks();
		glfwDestroyWindow(super.window);
		glfwTerminate();
	}

	public void setVisible() {
		glfwShowWindow(window);
	}

	public void setInvisible() {
		glfwHideWindow(super.window);
	}

	public static double getTime() {
		return glfwGetTime();
	}

	public static long getNanoTime() {
		return (long) (glfwGetTime() * (1000L * 1000L * 1000L));
	}

	public float getDelta() {
		double time = getTime();
		float delta = (float) (time - lastLoopTime);
		lastLoopTime = time;
		timeCount += delta;
		return delta;
	}

	public int getUsedVRAM() {
		if (nvidia)
			glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX, usedVram);
		return maxVram.get(0) - usedVram.get(0);
	}

	public boolean isNvidia() {
		return nvidia;
	}

	public boolean isAmd() {
		return amd;
	}

	public float getTimeCount() {
		return timeCount;
	}

	public void setTimeCount(float timeCount) {
		this.timeCount = timeCount;
	}

}
