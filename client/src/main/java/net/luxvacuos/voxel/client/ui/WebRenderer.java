/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;

public class WebRenderer {

	private String webpage;

	private List<WebText> texts;

	private float yScale, xScale, x, y;

	private boolean updating = false;

	public WebRenderer(String webpage, float x, float y) {
		this.webpage = webpage;
		texts = new ArrayList<WebText>();
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		this.x = x;
		this.y = y;
	}

	public void render() {
		float ypos = 0;
		if (!updating)
			for (int y = 0; y < texts.size(); y++) {
				VectorsRendering.renderText(texts.get(y).getText(), "Roboto-Bold", this.x, (this.y + ypos),
						texts.get(y).getFontSize(), VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
						VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
				ypos += texts.get(y).getFontSize();
				if (texts.get(y).isTitle())
					ypos += 5;
			}
		if (updating)
			VectorsRendering.renderText("Updating... Please Wait", "Roboto-Bold", x + ((375 - 100) * xScale),
					y + 200 * yScale, 20 * yScale, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
	}

	public void update() {
		if (!updating) {
			updating = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					texts.clear();
					URL url;
					try {
						url = new URL(webpage);
						URLConnection conn = url.openConnection();
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(conn.getInputStream()));
						float font = 0;
						while (true) {
							String text = bufferedReader.readLine();
							String[] currentLine = text.split(" ");
							if (text.startsWith("#TITLE ")) {
								String textFinal = "";
								for (int i = 1; i < currentLine.length; i++) {
									textFinal += currentLine[i] + " ";
								}
								texts.add(new WebText(textFinal, font, true));
							} else if (text.startsWith("#FONT ")) {
								font = Float.parseFloat(currentLine[1]) * yScale;
							} else if (text.startsWith("#TEXT ")) {
								String textFinal = "";
								for (int i = 1; i < currentLine.length; i++) {
									textFinal += currentLine[i] + " ";
								}
								texts.add(new WebText(textFinal, font, false));
							} else if (text.contains("#END")) {
								break;
							}
						}
						bufferedReader.close();
					} catch (MalformedURLException e) {
						texts.add(new WebText("Unable to get data.", 20, false));
						e.printStackTrace();
					} catch (IOException e) {
						texts.add(new WebText("Unable to get data.", 20, false));
						e.printStackTrace();
					}
					updating = false;
				}
			}).start();
		}
	}

}
