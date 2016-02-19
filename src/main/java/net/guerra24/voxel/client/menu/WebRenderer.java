/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.nanovg.rendering.VectorsRendering;

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
