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

import static net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.colorA;
import static net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.rgba;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwSetTime;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_BOTTOM;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgMoveTo;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;
import static org.lwjgl.opengl.ARBTimerQuery.GL_TIME_ELAPSED;
import static org.lwjgl.opengl.ARBTimerQuery.glGetQueryObjectui64v;
import static org.lwjgl.opengl.GL15.GL_QUERY_RESULT;
import static org.lwjgl.opengl.GL15.GL_QUERY_RESULT_AVAILABLE;
import static org.lwjgl.opengl.GL15.glBeginQuery;
import static org.lwjgl.opengl.GL15.glEndQuery;
import static org.lwjgl.opengl.GL15.glGenQueries;
import static org.lwjgl.opengl.GL15.glGetQueryObjectiv;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import net.luxvacuos.voxel.client.core.GraphicalSubsystem;

public class Timers {

	private static long vg;
	private static double t, dt;
	private static GPUtimer gpuTimer;
	private static PerfGraph fps;
	private static PerfGraph cpuGraph;
	private static PerfGraph gpuGraph;
	private static double prevt;
	private static final FloatBuffer gpuTimes = BufferUtils.createFloatBuffer(3);
	static final int GRAPH_RENDER_FPS = 0, GRAPH_RENDER_MS = 1, GRAPH_RENDER_PERCENT = 2;

	private static final int GRAPH_HISTORY_COUNT = 200;
	private static final int GPU_QUERY_COUNT = 5;

	public static void initDebugDisplay() {
		vg = GraphicalSubsystem.getMainWindow().getNVGID();

		gpuTimer = new GPUtimer();
		fps = new PerfGraph();
		cpuGraph = new PerfGraph();
		gpuGraph = new PerfGraph();

		initGraph(fps, GRAPH_RENDER_FPS, "Frame Time");
		initGraph(cpuGraph, GRAPH_RENDER_MS, "CPU Time");
		initGraph(gpuGraph, GRAPH_RENDER_MS, "GPU Time");
		initGPUTimer(gpuTimer);

		glfwSetTime(0);
		prevt = glfwGetTime();
	}

	public static void renderDebugDisplay(float x, float y, float w, float h) {
		renderGraph(vg, x, y, w, h, fps);
		renderGraph(vg, x + w + 5, y, w, h, cpuGraph);
		if (gpuTimer.supported)
			renderGraph(vg, x + w + 5 + w + 5, y, w, h, gpuGraph);

	}

	private static void updateDebugDisplay() {
		updateGraph(fps, (float) dt);
	}

	public static void startCPUTimer() {
		t = glfwGetTime();
		dt = t - prevt;
		prevt = t;
	}

	public static void stopCPUTimer() {
		double cpuTime = glfwGetTime() - t;
		updateGraph(cpuGraph, (float) cpuTime);
	}

	public static void startGPUTimer() {
		startGPUTimer(gpuTimer);
	}

	public static void stopGPUTimer() {
		int n = stopGPUTimer(gpuTimer, gpuTimes, 3);
		for (int i = 0; i < n; i++)
			updateGraph(gpuGraph, gpuTimes.get(i));
	}

	public static void update() {
		updateDebugDisplay();
	}

	private static class PerfGraph {
		int style;
		ByteBuffer name = BufferUtils.createByteBuffer(32);
		float[] values = new float[GRAPH_HISTORY_COUNT];
		int head;
	}

	static class GPUtimer {
		boolean supported;
		int cur, ret;
		IntBuffer queries = BufferUtils.createIntBuffer(GPU_QUERY_COUNT);
	}

	private static void initGPUTimer(GPUtimer timer) {
		// memset(timer, 0, sizeof(*timer))2
		timer.supported = GL.getCapabilities().GL_ARB_timer_query;
		timer.cur = 0;
		timer.ret = 0;
		BufferUtils.zeroBuffer(timer.queries);

		if (timer.supported)
			glGenQueries(timer.queries);
	}

	private static void startGPUTimer(GPUtimer timer) {
		if (!timer.supported)
			return;
		glBeginQuery(GL_TIME_ELAPSED, timer.queries.get(timer.cur % GPU_QUERY_COUNT));
		timer.cur++;
	}

	private static int stopGPUTimer(GPUtimer timer, FloatBuffer times, int maxTimes) {
		int n = 0;
		if (!timer.supported)
			return 0;

		glEndQuery(GL_TIME_ELAPSED);

		IntBuffer available = memAllocInt(1);
		available.put(0, 1);
		while (available.get(0) != 0 && timer.ret <= timer.cur) {
			// check for results if there are any
			glGetQueryObjectiv(timer.queries.get(timer.ret % GPU_QUERY_COUNT), GL_QUERY_RESULT_AVAILABLE, available);
			if (available.get(0) != 0) {
				LongBuffer timeElapsed = memAllocLong(1);
				glGetQueryObjectui64v(timer.queries.get(timer.ret % GPU_QUERY_COUNT), GL_QUERY_RESULT, timeElapsed);
				timer.ret++;
				if (n < maxTimes) {
					times.put(n, (float) ((double) timeElapsed.get(0) * 1e-9));
					n++;
				}
				memFree(timeElapsed);
			}
		}
		memFree(available);
		return n;
	}

	private static void initGraph(PerfGraph fps, int style, String name) {
		fps.style = style;
		fps.name = memUTF8(name);
		Arrays.fill(fps.values, 0);
		fps.head = 0;
	}

	private static void updateGraph(PerfGraph fps, float frameTime) {
		fps.head = (fps.head + 1) % GRAPH_HISTORY_COUNT;
		fps.values[fps.head] = frameTime;
	}

	private static float getGraphAverage(PerfGraph fps) {
		float avg = 0;
		for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
			avg += fps.values[i];
		}
		return avg / (float) GRAPH_HISTORY_COUNT;
	}

	private static void renderGraph(long vg, float x, float y, float w, float h, PerfGraph fps) {
		float avg = getGraphAverage(fps);

		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		nvgFillColor(vg, rgba(0, 0, 0, 128, colorA));
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgMoveTo(vg, x, y + h);
		if (fps.style == GRAPH_RENDER_FPS) {
			for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
				float v = 1.0f / (0.00001f + fps.values[(fps.head + i) % GRAPH_HISTORY_COUNT]);
				float vx, vy;
				if (v > 1000.0f)
					v = 1000.0f;
				vx = x + ((float) i / (GRAPH_HISTORY_COUNT - 1)) * w;
				vy = y + h - ((v / 1000.0f) * h);
				nvgLineTo(vg, vx, vy);
			}
		} else if (fps.style == GRAPH_RENDER_PERCENT) {
			for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
				float v = fps.values[(fps.head + i) % GRAPH_HISTORY_COUNT] * 1.0f;
				float vx, vy;
				if (v > 100.0f)
					v = 100.0f;
				vx = x + ((float) i / (GRAPH_HISTORY_COUNT - 1)) * w;
				vy = y + h - ((v / 100.0f) * h);
				nvgLineTo(vg, vx, vy);
			}
		} else {
			for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
				float v = fps.values[(fps.head + i) % GRAPH_HISTORY_COUNT] * 1000.0f;
				float vx, vy;
				if (v > 20.0f)
					v = 20.0f;
				vx = x + ((float) i / (GRAPH_HISTORY_COUNT - 1)) * w;
				vy = y + h - ((v / 20.0f) * h);
				nvgLineTo(vg, vx, vy);
			}
		}
		nvgLineTo(vg, x + w, y + h);
		nvgFillColor(vg, rgba(255, 192, 0, 128, colorA));
		nvgFill(vg);

		nvgFontFace(vg, "Roboto-Bold");

		if (fps.name.get(0) != '\0') {
			nvgFontSize(vg, 14.0f);
			nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
			nvgFillColor(vg, rgba(240, 240, 240, 192, colorA));
			nvgText(vg, x + 3, y + 1, fps.name);
		}

		if (fps.style == GRAPH_RENDER_FPS) {
			nvgFontSize(vg, 18.0f);
			nvgTextAlign(vg, NVG_ALIGN_RIGHT | NVG_ALIGN_TOP);
			nvgFillColor(vg, rgba(240, 240, 240, 255, colorA));
			nvgText(vg, x + w - 3, y + 1, String.format("%.2f FPS", 1.0f / avg));

			nvgFontSize(vg, 15.0f);
			nvgTextAlign(vg, NVG_ALIGN_RIGHT | NVG_ALIGN_BOTTOM);
			nvgFillColor(vg, rgba(240, 240, 240, 160, colorA));
			nvgText(vg, x + w - 3, y + h - 1, String.format("%.2f ms", avg * 1000.0f));
		} else if (fps.style == GRAPH_RENDER_PERCENT) {
			nvgFontSize(vg, 18.0f);
			nvgTextAlign(vg, NVG_ALIGN_RIGHT | NVG_ALIGN_TOP);
			nvgFillColor(vg, rgba(240, 240, 240, 255, colorA));
			nvgText(vg, x + w - 3, y + 1, String.format("%.1f %%", avg * 1.0f));
		} else {
			nvgFontSize(vg, 18.0f);
			nvgTextAlign(vg, NVG_ALIGN_RIGHT | NVG_ALIGN_TOP);
			nvgFillColor(vg, rgba(240, 240, 240, 255, colorA));
			nvgText(vg, x + w - 3, y + 1, String.format("%.2f ms", avg * 1000.0f));
		}
	}

}
