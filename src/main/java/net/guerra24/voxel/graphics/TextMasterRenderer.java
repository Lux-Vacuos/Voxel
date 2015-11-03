package net.guerra24.voxel.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.guerra24.voxel.resources.Loader;
import net.guerra24.voxel.resources.models.FontType;
import net.guerra24.voxel.resources.models.GUIText;
import net.guerra24.voxel.resources.models.TextMeshData;

public class TextMasterRenderer {

	private Loader loader;
	private Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private FontRenderer renderer;

	public TextMasterRenderer(Loader loader) {
		init(loader);
	}

	private void init(Loader loader) {
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
