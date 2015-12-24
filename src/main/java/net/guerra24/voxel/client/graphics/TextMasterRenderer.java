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

package net.guerra24.voxel.client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.FontType;
import net.guerra24.voxel.client.resources.models.GUIText;
import net.guerra24.voxel.client.resources.models.TextMeshData;

public class TextMasterRenderer {

	private static TextMasterRenderer instance = null;

	public static TextMasterRenderer getInstance() {
		if (instance == null) {
			instance = new TextMasterRenderer();
		}
		return instance;
	}

	private Loader loader;
	private Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private FontRenderer renderer;

	private TextMasterRenderer() {
	}

	public void init(Loader loader) {
		this.loader = loader;
		renderer = new FontRenderer();
	}

	public void render() {
		renderer.render(texts);
	}

	public void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}

	public void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	public void cleanUp() {
		renderer.cleanUp();
	}

	public FontRenderer getRenderer() {
		return renderer;
	}
}
