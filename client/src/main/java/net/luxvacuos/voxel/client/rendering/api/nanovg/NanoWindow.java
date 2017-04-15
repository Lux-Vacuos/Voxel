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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.nvgScissor;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluBindFramebuffer;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluCreateFramebuffer;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluDeleteFramebuffer;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGLUFramebuffer;

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Sync;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.BackgroundStyle;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.ButtonStyle;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.ITitleBar;
import net.luxvacuos.voxel.client.ui.TitleBar;
import net.luxvacuos.voxel.client.ui.TitleBarButton;
import net.luxvacuos.voxel.client.ui.TitleBarText;

public abstract class NanoWindow implements IWindow {

	private boolean draggable = true, decorations = true, resizable = true, maximized, hidden = false, exit,
			alwaysOnTop, background, blurBehind = true, running = true, resizing;
	private BackgroundStyle backgroundStyle = BackgroundStyle.SOLID;
	private NVGColor backgroundColor = NRendering.rgba(0, 0, 0, 255);
	private float x, y, w, h;
	protected float appX, appY, appW, appH;
	private float oldX, oldY, oldW, oldH;
	private WindowClose windowClose = WindowClose.DISPOSE;
	private ITitleBar titleBar;
	private NVGLUFramebuffer fbo;
	private String title;
	private double lastLoopTime;
	private Thread thread;
	private int UPS = 60;

	public NanoWindow(float x, float y, float w, float h, String title) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.title = title;
		titleBar = new TitleBar(this);
		if (titleBar.isEnabled()) {
			appX = x + NRendering.BORDER_SIZE / 2f;
			appY = y - TitleBar.HEIGHT;
			appW = w - NRendering.BORDER_SIZE;
			appH = h - TitleBar.HEIGHT - NRendering.BORDER_SIZE / 2f;
		} else {
			appX = x + NRendering.BORDER_SIZE / 2f;
			appY = y - NRendering.BORDER_SIZE / 2f;
			appW = w - NRendering.BORDER_SIZE;
			appH = h - NRendering.BORDER_SIZE;
		}
	}

	@Override
	public void init(Window wind) {
		fbo = nvgluCreateFramebuffer(wind.getNVGID(), (int) (wind.getWidth() * wind.getPixelRatio()),
				(int) (wind.getHeight() * wind.getPixelRatio()), 0);
		initApp(wind);
		TitleBarButton close = new TitleBarButton(-1, -1, 28, 28);
		close.setOnButtonPress(() -> {
			onClose();
			closeWindow();
		});
		close.setColor("#C80000C8");
		close.setWindowAlignment(Alignment.RIGHT_TOP);
		close.setAlignment(Alignment.LEFT_BOTTOM);
		close.setStyle(ButtonStyle.CLOSE);

		TitleBarButton maximize = new TitleBarButton(-30, -1, 28, 28);
		maximize.setOnButtonPress(() -> {
			if (resizable) {
				maximized = !maximized;
				if (maximized) {
					oldX = this.x;
					oldY = this.y;
					oldW = this.w;
					oldH = this.h;
					this.x = 0;
					this.y = ClientVariables.HEIGHT;
					this.w = ClientVariables.WIDTH;
					this.h = ClientVariables.HEIGHT;
				} else {
					this.x = oldX;
					this.y = oldY;
					this.w = oldW;
					this.h = oldH;
				}
			}
		});
		maximize.setColor("#646464C8");
		maximize.setWindowAlignment(Alignment.RIGHT_TOP);
		maximize.setAlignment(Alignment.LEFT_BOTTOM);
		maximize.setStyle(ButtonStyle.MAXIMIZE);

		TitleBarButton minimize = new TitleBarButton(-59, -1, 28, 28);
		minimize.setOnButtonPress(() -> {
		});
		minimize.setColor("#646464C8");
		minimize.setWindowAlignment(Alignment.RIGHT_TOP);
		minimize.setAlignment(Alignment.LEFT_BOTTOM);
		minimize.setStyle(ButtonStyle.MINIMIZE);

		TitleBarText titleText = new TitleBarText(title, 0, 0);
		titleText.setWindowAlignment(Alignment.CENTER);
		titleText.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);

		titleBar.addComponent(close);
		if (resizable)
			titleBar.addComponent(maximize);
		//titleBar.addComponent(minimize);
		titleBar.addComponent(titleText);

		titleBar.setOnDrag((window) -> {
			if (draggable && !maximized) {
				this.x += Mouse.getDX();
				this.y += Mouse.getDY();
			}
		});

		thread = new Thread(() -> {
			lastLoopTime = WindowManager.getTime();
			float delta = 0;
			float accumulator = 0f;
			float interval = 1f / UPS;
			Sync sync = new Sync();
			while (running) {
				delta = getDelta();
				accumulator += delta;
				while (accumulator >= interval) {
					updateApp(delta, wind);
					accumulator -= interval;
				}
				sync.sync(UPS);
			}
		});
		// thread.start();
	}

	@Override
	public void render(Window window, IWindowManager nanoWindowManager) {
		if (!hidden) {
			nvgluBindFramebuffer(window.getNVGID(), fbo);
			window.resetViewport();
			Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Renderer.clearColors(0, 0, 0, 0);

			window.beingNVGFrame();
			NRendering.renderWindow(window.getNVGID(), x, window.getHeight() - y, w, h, backgroundStyle,
					backgroundColor, decorations, titleBar.isEnabled());
			if (decorations)
				titleBar.render(window);
			nvgScissor(window.getNVGID(), appX, window.getHeight() - appY, appW, appH);
			renderApp(window);
			window.endNVGFrame();
			nvgluBindFramebuffer(window.getNVGID(), null);
		}
	}

	@Override
	public void update(float delta, Window window, IWindowManager nanoWindowManager) {
		if (decorations && !hidden) {
			titleBar.update(delta, window);
			if ((Mouse.isButtonDown(0) && canResize()) || resizing) {
				resizing = Mouse.isButtonDown(0);
				w += Mouse.getDX();
				h -= Mouse.getDY();
			}
		}
		// lastUpdate += 1 * delta;
		updateApp(delta, window);
	}

	@Override
	public void alwaysUpdate(float delta, Window window, IWindowManager nanoWindowManager) {
		titleBar.alwaysUpdate(delta, window);
		if (titleBar.isEnabled()) {
			appX = x + NRendering.BORDER_SIZE / 2f;
			appY = y - TitleBar.HEIGHT;
			appW = w - NRendering.BORDER_SIZE;
			appH = h - TitleBar.HEIGHT - NRendering.BORDER_SIZE / 2f;
		} else {
			appX = x + NRendering.BORDER_SIZE / 2f;
			appY = y - NRendering.BORDER_SIZE / 2f;
			appW = w - NRendering.BORDER_SIZE;
			appH = h - NRendering.BORDER_SIZE;
		}
		alwaysUpdateApp(delta, window);
	}

	@Override
	public void dispose(Window window) {
		running = false;
		nvgluDeleteFramebuffer(window.getNVGID(), fbo);
		disposeApp(window);
	}

	@Override
	public void onClose() {
	}

	private boolean canResize() {
		return Mouse.getX() > x + w - 20 && Mouse.getY() < y - h + 20 && Mouse.getX() < x + w + 20
				&& Mouse.getY() > y - h - 20 && resizable && !maximized;
	}

	@Override
	public boolean insideWindow() {
		return Mouse.getX() > x && Mouse.getX() < x + w && Mouse.getY() > y - h && Mouse.getY() < y;
	}

	@Override
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	@Override
	public void setDecorations(boolean decorations) {
		this.decorations = decorations;
	}

	@Override
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	@Override
	public void setBackgroundStyle(BackgroundStyle backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}

	@Override
	public void setWindowClose(WindowClose windowClose) {
		this.windowClose = windowClose;
	}

	@Override
	public void setBackgroundColor(float r, float g, float b, float a) {
		backgroundColor.r(r);
		backgroundColor.g(g);
		backgroundColor.b(b);
		backgroundColor.a(a);
	}

	@Override
	public void setBackgroundColor(String hex) {
		backgroundColor.r(Integer.valueOf(hex.substring(1, 3), 16) / 255f);
		backgroundColor.g(Integer.valueOf(hex.substring(3, 5), 16) / 255f);
		backgroundColor.b(Integer.valueOf(hex.substring(5, 7), 16) / 255f);
		backgroundColor.a(Integer.valueOf(hex.substring(7, 9), 16) / 255f);
	}

	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public void setAlwaysOnTop(boolean alwaysOnTop) {
		this.alwaysOnTop = alwaysOnTop;
	}

	@Override
	public void setAsBackground(boolean background) {
		this.background = background;
	}

	@Override
	public void setBlurBehind(boolean blur) {
		blurBehind = blur;
	}

	@Override
	public void toggleTitleBar() {
		this.titleBar.setEnabled(!this.titleBar.isEnabled());
	}

	@Override
	public BackgroundStyle getBackgroundStyle() {
		return backgroundStyle;
	}

	@Override
	public float getWidth() {
		return w;
	}

	@Override
	public float getHeight() {
		return h;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public Thread getThread() {
		return thread;
	}

	@Override
	public NVGLUFramebuffer getFBO() {
		return fbo;
	}

	@Override
	public boolean hasDecorations() {
		return decorations;
	}

	@Override
	public boolean isResizable() {
		return resizable;
	}

	@Override
	public boolean isDraggable() {
		return draggable;
	}

	@Override
	public boolean hasBlurBehind() {
		return blurBehind;
	}

	@Override
	public boolean shouldClose() {
		return exit;
	}

	@Override
	public boolean isBackground() {
		return background;
	}

	@Override
	public boolean isAlwaysOnTop() {
		return alwaysOnTop;
	}

	@Override
	public void closeWindow() {
		switch (windowClose) {
		case DISPOSE:
			exit = true;
			break;
		case DO_NOTHING:
			break;
		case HIDE:
			hidden = true;
			break;
		}
	}

	public float getDelta() {
		double time = WindowManager.getTime();
		float delta = (float) (time - this.lastLoopTime);
		this.lastLoopTime = time;
		return delta;
	}

}
