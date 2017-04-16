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

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.RootComponent;
import net.luxvacuos.voxel.client.ui.Text;

public final class WM {

	private static IWindowManager iwm;
	
	public static void setWM(IWindowManager iwm) {
		if (WM.iwm != null)
			WM.iwm.dispose();
		WM.iwm = iwm;
		Logger.log("Window Manager: " + iwm.getClass().getSimpleName());
	}

	public static IWindowManager getWM() {
		return iwm;
	}
	
	public static void generateTestWindows(){
		Text win0T = new Text("Default Window", 0, 0);
		win0T.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		win0T.setWindowAlignment(Alignment.CENTER);

		Text win1T = new Text("Can't be resized", 0, 0);
		win1T.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		win1T.setWindowAlignment(Alignment.CENTER);

		Text win2T = new Text("Disabled titlebar", 0, 0);
		win2T.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		win2T.setWindowAlignment(Alignment.CENTER);

		Text win3T = new Text("No decorations", 0, 0);
		win3T.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		win3T.setWindowAlignment(Alignment.CENTER);

		Text win4T = new Text("No decorations and disabled titlebar", 0, 0);
		win4T.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		win4T.setWindowAlignment(Alignment.CENTER);
		win4T.setFontSize(15);

		RootComponent win0 = new RootComponent(20, 700, 200, 200, "Window0");
		win0.addComponent(win0T);
		RootComponent win1 = new RootComponent(240, 700, 200, 200, "Window1");
		win1.addComponent(win1T);
		win1.setResizable(false);
		RootComponent win2 = new RootComponent(480, 700, 200, 200, "Window2");
		win2.addComponent(win2T);
		win2.toggleTitleBar();
		RootComponent win3 = new RootComponent(700, 700, 200, 200, "Window3");
		win3.addComponent(win3T);
		win3.setDecorations(false);
		RootComponent win4 = new RootComponent(920, 700, 200, 200, "Window4");
		win4.addComponent(win4T);
		win4.toggleTitleBar();
		win4.setDecorations(false);

		RootComponent win5 = new RootComponent(20, 480, 200, 200, "Window5");
		win5.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		RootComponent win6 = new RootComponent(240, 480, 200, 200, "Window6");
		win6.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		win6.setResizable(false);
		RootComponent win7 = new RootComponent(480, 480, 200, 200, "Window7");
		win7.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		win7.toggleTitleBar();
		RootComponent win8 = new RootComponent(700, 480, 200, 200, "Window8");
		win8.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		win8.setDecorations(false);
		RootComponent win9 = new RootComponent(920, 480, 200, 200, "Window9");
		win9.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		win9.toggleTitleBar();
		win9.setDecorations(false);

		WM.getWM().addWindow(win0);
		WM.getWM().addWindow(win1);
		WM.getWM().addWindow(win2);
		WM.getWM().addWindow(win3);
		WM.getWM().addWindow(win4);
		WM.getWM().addWindow(win5);
		WM.getWM().addWindow(win6);
		WM.getWM().addWindow(win7);
		WM.getWM().addWindow(win8);
		WM.getWM().addWindow(win9);
	}

}
