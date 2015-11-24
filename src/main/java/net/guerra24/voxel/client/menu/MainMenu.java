/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
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

import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.MenuResources;
import net.guerra24.voxel.client.resources.models.ButtonModel;
import net.guerra24.voxel.client.resources.models.FontType;
import net.guerra24.voxel.client.resources.models.GUIText;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class MainMenu {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;
	private List<ButtonModel> list;

	private List<GUIText> texts;

	public MainMenu(GameResources gm) {
		FontType font = gm.getTextHandler().getFont();
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		float yScale = height / 720f;
		float xScale = width / 1280f;
		playButton = new Button(new Vector2f(177 * xScale, 532 * yScale), new Vector2f(215, 80));
		exitButton = new Button(new Vector2f(177 * xScale, 224 * yScale), new Vector2f(215, 80));
		optionsButton = new Button(new Vector2f(177 * xScale, 376 * yScale), new Vector2f(215, 80));

		list = new ArrayList<ButtonModel>();
		list.add(new ButtonModel(MenuResources.getModel1Final(), new Vector3f(-0.7f, 0.4f, 0), new Vector3f(90, 0, 0),
				0.07f));
		list.add(new ButtonModel(MenuResources.getModel1Final(), new Vector3f(-0.7f, 0.1f, 0), new Vector3f(90, 0, 0),
				0.07f));
		list.add(new ButtonModel(MenuResources.getModel1Final(), new Vector3f(-0.7f, -0.2f, 0), new Vector3f(90, 0, 0),
				0.07f));
		list.add(new ButtonModel(MenuResources.getMainMenuBackFinal(), new Vector3f(0.1f, -0.92f, -1f),
				new Vector3f(90, 0, 0), 4));

		texts = new ArrayList<GUIText>();
		GUIText textVersion = new GUIText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build, 1,
				font, new Vector2f(0.002f, 0.97f), 1, false);
		textVersion.setColour(0.79f, 0.79f, 0.79f);
		texts.add(textVersion);
		GUIText textVersionApi = new GUIText("Voxel API " + VoxelVariables.apiVersion, 1, font,
				new Vector2f(0.002f, 0.94f), 1, false);
		textVersionApi.setColour(0.79f, 0.79f, 0.79f);
		texts.add(textVersionApi);
		GUIText textMAC = new GUIText("Voxel is running on OSX, some things did not work well", 1, font,
				new Vector2f(0.002f, 0.002f), 1, false);
		textMAC.setColour(1, 0, 0);
		if (VoxelVariables.runningOnMac)
			texts.add(textMAC);
		GUIText textPlay = new GUIText("Play", 2, font, new Vector2f(0.185f, 0.18f), 1, false);
		textPlay.setColour(0.79f, 0.79f, 0.79f);
		texts.add(textPlay);
		GUIText textOptions = new GUIText("Options", 2, font, new Vector2f(0.167f, 0.395f), 1, false);
		textOptions.setColour(0.79f, 0.79f, 0.79f);
		texts.add(textOptions);
		GUIText textExit = new GUIText("Exit", 2, font, new Vector2f(0.189f, 0.61f), 1, false);
		textExit.setColour(0.79f, 0.79f, 0.79f);
		texts.add(textExit);
	}

	public void load(GameResources gm) {
		gm.getTextHandler().removeFromActive(gm.getMenuSystem().optionsMenu.getTextsUpdating());
		gm.getTextHandler().switchTo(texts);
	}

	public Button getPlayButton() {
		return playButton;
	}

	public Button getExitButton() {
		return exitButton;
	}

	public List<ButtonModel> getList() {
		return list;
	}

	public Button getOptionsButton() {
		return optionsButton;
	}

}
