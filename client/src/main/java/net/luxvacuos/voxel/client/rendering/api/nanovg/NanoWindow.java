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

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Sync;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.BackgroundStyle;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.ButtonStyle;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Direction;
import net.luxvacuos.voxel.client.ui.FlowLayout;
import net.luxvacuos.voxel.client.ui.ITitleBar;
import net.luxvacuos.voxel.client.ui.TitleBar;
import net.luxvacuos.voxel.client.ui.TitleBarButton;
import net.luxvacuos.voxel.client.ui.TitleBarText;

public abstract class NanoWindow implements IWindow {

	private boolean draggable = true, decorations = true, resizable = true, maximized, hidden = false, exit,
			alwaysOnTop, background, blurBehind = true, running = true, resizing, minimized;
	private BackgroundStyle backgroundStyle = BackgroundStyle.SOLID;
	private NVGColor backgroundColor = NRendering.rgba(0, 0, 0, 255);
	protected float x, y, w, h, minW = 300, minH = 300;
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
	}

	@Override
	public void init(Window wind) {
		fbo = nvgluCreateFramebuffer(wind.getNVGID(), (int) (wind.getWidth() * wind.getPixelRatio()),
				(int) (wind.getHeight() * wind.getPixelRatio()), 0);
		titleBar.getLeft().setLayout(new FlowLayout(Direction.RIGHT, 1, 0));
		titleBar.getRight().setLayout(new FlowLayout(Direction.LEFT, 1, 0));
		initApp(wind);
		TitleBarButton closeBtn = new TitleBarButton(0, -1, 28, 28);
		closeBtn.setOnButtonPress(() -> {
			onClose();
			closeWindow();
		});
		closeBtn.setColor("#646464C8");
		closeBtn.setHighlightColor("#FF0000C8");
		closeBtn.setWindowAlignment(Alignment.RIGHT_TOP);
		closeBtn.setAlignment(Alignment.LEFT_BOTTOM);
		closeBtn.setStyle(ButtonStyle.CLOSE);

		TitleBarButton maximizeBtn = new TitleBarButton(0, -1, 28, 28);
		maximizeBtn.setOnButtonPress(() -> {
			toggleMaximize();
		});
		maximizeBtn.setColor("#646464C8");
		maximizeBtn.setHighlightColor("#FFFFFFC8");
		maximizeBtn.setWindowAlignment(Alignment.RIGHT_TOP);
		maximizeBtn.setAlignment(Alignment.LEFT_BOTTOM);
		maximizeBtn.setStyle(ButtonStyle.MAXIMIZE);

		TitleBarButton minimizeBtn = new TitleBarButton(0, -1, 28, 28);
		minimizeBtn.setOnButtonPress(() -> {
			minimized = true;
		});
		minimizeBtn.setColor("#646464C8");
		minimizeBtn.setHighlightColor("#FFFFFFC8");
		minimizeBtn.setWindowAlignment(Alignment.RIGHT_TOP);
		minimizeBtn.setAlignment(Alignment.LEFT_BOTTOM);
		minimizeBtn.setStyle(ButtonStyle.MINIMIZE);

		TitleBarText titleText = new TitleBarText(title, 0, 0);
		titleText.setWindowAlignment(Alignment.CENTER);
		titleText.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);

		titleBar.getRight().addComponent(closeBtn);
		if (resizable)
			titleBar.getRight().addComponent(maximizeBtn);
		titleBar.getRight().addComponent(minimizeBtn);
		titleBar.getCenter().addComponent(titleText);

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
		if (!hidden && !minimized) {
			nvgluBindFramebuffer(window.getNVGID(), fbo);
			window.resetViewport();
			Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Renderer.clearColors(0, 0, 0, 0);

			window.beingNVGFrame();
			NRendering.renderWindow(window.getNVGID(), x, window.getHeight() - y, w, h, backgroundStyle,
					backgroundColor, decorations, titleBar.isEnabled(), maximized);
			if (decorations)
				titleBar.render(window);
			nvgScissor(window.getNVGID(), x, window.getHeight() - y, w, h);
			renderApp(window);
			window.endNVGFrame();
			nvgluBindFramebuffer(window.getNVGID(), null);
		}
	}

	@Override
	public void update(float delta, Window window, IWindowManager nanoWindowManager) {
		if (decorations && !hidden && !minimized) {
			titleBar.update(delta, window);
			if ((Mouse.isButtonDown(0) && canResize()) || resizing) {
				resizing = Mouse.isButtonDown(0);
				if (w > minW)
					w += Mouse.getDX();
				else {
					if (Mouse.getDX() > 0)
						w += Mouse.getDX();
				}
				if (h > minH)
					h -= Mouse.getDY();
				else {
					if (Mouse.getDY() < 0)
						h -= Mouse.getDY();
				}

			}
		}
		if (!resizing && !minimized)
			updateApp(delta, window);
	}

	@Override
	public void alwaysUpdate(float delta, Window window, IWindowManager nanoWindowManager) {
		titleBar.alwaysUpdate(delta, window);
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
				&& Mouse.getY() > y - h - 20 && resizable && !maximized && !minimized;
	}

	@Override
	public boolean insideWindow() {
		float borderSize = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		if (titleBar.isEnabled() && decorations)
			return Mouse.getX() > x - borderSize && Mouse.getX() < x + w + borderSize
					&& Mouse.getY() > y - h - borderSize && Mouse.getY() < y
							+ (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
		else if (!decorations)
			return Mouse.getX() > x && Mouse.getX() < x + w && Mouse.getY() > y - h && Mouse.getY() < y;
		else
			return Mouse.getX() > x - borderSize && Mouse.getX() < x + w + borderSize * 2f
					&& Mouse.getY() > y - h - borderSize * 2f && Mouse.getY() < y + borderSize;
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
	public void toggleMaximize() {
		if (resizable) {
			maximized = !maximized;
			if (maximized) {
				int height = (int) REGISTRY.getRegistryItem("/Voxel/Display/height");
				oldX = this.x;
				oldY = this.y;
				oldW = this.w;
				oldH = this.h;
				this.x = 0;
				this.y = height - (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
				this.w = (int) REGISTRY.getRegistryItem("/Voxel/Display/width");
				this.h = height - (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight")
						- (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/shellHeight");
			} else {
				this.x = oldX;
				this.y = oldY;
				this.w = oldW;
				this.h = oldH;
			}
		}
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
	public String getTitle() {
		return title;
	}

	@Override
	public NVGLUFramebuffer getFBO() {
		return fbo;
	}

	@Override
	public void toggleMinimize() {
		minimized = !minimized;
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
	public boolean isDragging() {
		if (minimized || hidden)
			return false;
		else
			return titleBar.isDragging();
	}

	@Override
	public boolean isResizing() {
		if (minimized || hidden)
			return false;
		else
			return resizing;
	}

	@Override
	public boolean isMinimized() {
		return minimized;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public boolean isMaximized() {
		return maximized;
	}

	@Override
	public ITitleBar getTitleBar() {
		return titleBar;
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
