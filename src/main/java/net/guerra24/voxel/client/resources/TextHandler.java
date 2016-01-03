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


package net.guerra24.voxel.client.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.resources.models.FontType;
import net.guerra24.voxel.client.resources.models.GUIText;

public class TextHandler {

	private FontType font;

	private List<GUIText> activeText;

	public TextHandler(GameResources gm) {
		font = new FontType(gm.getLoader().loadTextureFont("tahoma"), new File("assets/fonts/tahoma.fnt"));
		activeText = new ArrayList<GUIText>();
	}

	public void loadActiveText() {
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public void addToActive(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove();
		}
		activeText.addAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public void removeFromActive(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove();
		}
		activeText.removeAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public void switchTo(List<GUIText> dest) {
		for (GUIText guiText : activeText) {
			guiText.remove();
		}
		activeText.clear();
		activeText.addAll(dest);
		for (GUIText guiText : activeText) {
			guiText.add();
		}
	}

	public FontType getFont() {
		return font;
	}
}
