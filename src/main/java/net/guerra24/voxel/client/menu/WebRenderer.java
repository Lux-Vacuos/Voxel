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
import net.guerra24.voxel.client.graphics.MenuRendering;

public class WebRenderer {

	private String webpage;

	private List<WebText> texts;

	private float yScale, xScale, x, y;

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
		for (int y = 0; y < texts.size(); y++) {
			MenuRendering.renderText(texts.get(y).getText(), "Roboto-Bold", this.x, (this.y + ypos),
					texts.get(y).getFontSize());
			ypos += texts.get(y).getFontSize();
			if (texts.get(y).isTitle())
				ypos += 5;
		}
	}

	public void update() {
		texts.clear();
		URL url;
		try {
			url = new URL(webpage);
			URLConnection conn = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
	}

}
