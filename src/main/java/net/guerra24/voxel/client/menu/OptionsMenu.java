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

public class OptionsMenu {

	private Button exitButton;
	private Button waterButton;
	private Button shadowsButton;
	private Button godraysButton;
	private List<ButtonModel> list;

	private List<GUIText> texts;
	private List<GUIText> textsUpdating;
	private FontType font;

	public OptionsMenu(GameResources gm) {
		this.font = gm.getTextHandler().getFont();
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		float yScale = height / 720f;
		float xScale = width / 1280f;
		texts = new ArrayList<GUIText>();
		textsUpdating = new ArrayList<GUIText>();
		exitButton = new Button(new Vector2f(530 * xScale, 35 * yScale), new Vector2f(215, 80));
		godraysButton = new Button(new Vector2f(74 * xScale, 582 * yScale), new Vector2f(215, 80));
		shadowsButton = new Button(new Vector2f(74 * xScale, 480 * yScale), new Vector2f(215, 80));
		waterButton = new Button(new Vector2f(74 * xScale, 378 * yScale), new Vector2f(215, 80));
		list = new ArrayList<ButtonModel>();
		list.add(new ButtonModel(MenuResources.getModel1Final(), new Vector3f(-1.4f, -3.95f, 0),
				new Vector3f(90, 0, 00), 0.07f));
		list.add(new ButtonModel(MenuResources.getModel1Final(), new Vector3f(-2.3f, -2.9f, 0), new Vector3f(90, 0, 00),
				0.07f));
		list.add(new ButtonModel(MenuResources.getModel1Final(), new Vector3f(-2.3f, -3.1f, 0), new Vector3f(90, 0, 00),
				0.07f));
		list.add(new ButtonModel(MenuResources.getModel1Final(), new Vector3f(-2.3f, -3.3f, 0), new Vector3f(90, 0, 00),
				0.07f));
		list.add(new ButtonModel(MenuResources.getMainMenuBackFinal(), new Vector3f(0.1f, -0.92f, -1f),
				new Vector3f(90, 0, 0), 4));
		GUIText textOptions = new GUIText("Back", 2, font, new Vector2f(0.467f, 0.86f), 1, false);
		textOptions.setColour(0.79f, 0.79f, 0.79f);
		texts.add(textOptions);
	}

	public void update(GameResources gm) {
		gm.getTextHandler().removeFromActive(textsUpdating);
		textsUpdating.clear();
		if (VoxelVariables.useVolumetricLight) {
			GUIText textGodRays = new GUIText("Light Rays: ON", 1.3f, font, new Vector2f(0.067f, 0.12f), 1, false);
			textGodRays.setColour(0.79f, 0.79f, 0.79f);
			textsUpdating.add(textGodRays);
		} else {
			GUIText textGodRays = new GUIText("Light Rays: OFF", 1.3f, font, new Vector2f(0.067f, 0.12f), 1, false);
			textGodRays.setColour(0.79f, 0.79f, 0.79f);
			textsUpdating.add(textGodRays);
		}
		if (VoxelVariables.useShadows) {
			GUIText textShadows = new GUIText("Shadows: ON", 1.3f, font, new Vector2f(0.075f, 0.265f), 1, false);
			textShadows.setColour(0.79f, 0.79f, 0.79f);
			textsUpdating.add(textShadows);
		} else {
			GUIText textShadows = new GUIText("Shadows: OFF", 1.3f, font, new Vector2f(0.075f, 0.265f), 1, false);
			textShadows.setColour(0.79f, 0.79f, 0.79f);
			textsUpdating.add(textShadows);
		}
		if (VoxelVariables.useDOF) {
			GUIText textDOF = new GUIText("DoF: ON", 1.3f, font, new Vector2f(0.098f, 0.41f), 1, false);
			textDOF.setColour(0.79f, 0.79f, 0.79f);
			textsUpdating.add(textDOF);
		} else {
			GUIText textDOF = new GUIText("DoF: OFF", 1.3f, font, new Vector2f(0.098f, 0.41f), 1, false);
			textDOF.setColour(0.79f, 0.79f, 0.79f);
			textsUpdating.add(textDOF);
		}
		gm.getTextHandler().addToActive(textsUpdating);
	}

	public void load(GameResources gm) {
		gm.getTextHandler().switchTo(texts);
	}

	public Button getExitButton() {
		return exitButton;
	}

	public List<ButtonModel> getList() {
		return list;
	}

	public Button getWaterButton() {
		return waterButton;
	}

	public Button getShadowsButton() {
		return shadowsButton;
	}

	public Button getGodraysButton() {
		return godraysButton;
	}

	public List<GUIText> getTextsUpdating() {
		return textsUpdating;
	}
}
