package net.luxvacuos.voxel.client.ui;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Text extends Component {

	private String text, font = "Roboto-Regular";
	private int align = NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE;
	private int fontSize = 25;
	private NVGColor color = UIRendering.rgba(255, 255, 255, 255, NVGColor.create());

	public Text(String text, int x, int y) {
		this.text = text;
		this.x = x;
		this.y = y;
	}

	@Override
	public void render() {
		UIRendering.renderText(text, font, align, rootX + x,
				GameResources.getInstance().getDisplay().getDisplayHeight() - rootY - y, fontSize, color, fadeAlpha);
		super.render();
	}

	public void setAlign(int align) {
		this.align = align;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setColor(int r, int g, int b, int a) {
		UIRendering.rgba(r, g, b, a, color);
	}
}
