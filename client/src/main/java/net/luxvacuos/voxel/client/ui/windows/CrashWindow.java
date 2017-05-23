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

package net.luxvacuos.voxel.client.ui.windows;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import java.io.PrintWriter;
import java.io.StringWriter;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Box;
import net.luxvacuos.voxel.client.ui.TextArea;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.client.ui.Text;

public class CrashWindow extends RootComponentWindow {

	private Throwable t;

	public CrashWindow(float x, float y, float w, float h, Throwable t) {
		super(x, y, w, h, "BSOD");
		this.t = t;
	}

	@Override
	public void initApp(Window window) {
		super.setDecorations(false);
		super.setAlwaysOnTop(true);
		super.toggleTitleBar();
		super.setBackgroundColor(0, 0.5f, 1, 1);
		super.setBlurBehind(false);

		window.getResourceLoader().loadNVGFont("Px437_IBM_VGA8", "Px437_IBM_VGA8");

		Box titleB = new Box(0, 150, -75, 25);
		titleB.setAlignment(Alignment.CENTER);
		titleB.setWindowAlignment(Alignment.CENTER);

		Text title = new Text("Voxel", 0, 150);
		title.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		title.setFont("Px437_IBM_VGA8");
		title.setColor(0, 0.5f, 1, 1);
		title.setWindowAlignment(Alignment.CENTER);

		Text line1 = new Text("An error has ocurred and unfortunately Voxel is unable to", -370, 75);
		line1.setAlign(NVG_ALIGN_MIDDLE);
		line1.setFont("Px437_IBM_VGA8");
		line1.setWindowAlignment(Alignment.CENTER);

		Text line2 = new Text("recover from it and continue. Some information might have", -370, 50);
		line2.setAlign(NVG_ALIGN_MIDDLE);
		line2.setFont("Px437_IBM_VGA8");
		line2.setWindowAlignment(Alignment.CENTER);

		Text line3 = new Text("been lost.", -370, 25);
		line3.setAlign(NVG_ALIGN_MIDDLE);
		line3.setFont("Px437_IBM_VGA8");
		line3.setWindowAlignment(Alignment.CENTER);

		Text error = new Text("Kernel Panic", -370, -25);
		error.setAlign(NVG_ALIGN_MIDDLE);
		error.setFont("Px437_IBM_VGA8");
		error.setWindowAlignment(Alignment.CENTER);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);

		TextArea errorMessage = new TextArea(sw.toString(), -370, -40, 744);
		errorMessage.setFont("Px437_IBM_VGA8");
		errorMessage.setWindowAlignment(Alignment.CENTER);

		super.addComponent(titleB);
		super.addComponent(title);
		super.addComponent(line1);
		super.addComponent(line2);
		super.addComponent(line3);
		super.addComponent(error);
		super.addComponent(errorMessage);

		super.initApp(window);
	}

}
