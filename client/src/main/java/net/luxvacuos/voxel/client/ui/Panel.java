package net.luxvacuos.voxel.client.ui;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Panel extends Component {
	private NVGColor fillColor = UIRendering.rgba(255, 255, 255, 200), gradientColor = UIRendering.rgba(32, 32, 32, 32),
			borderColor = UIRendering.rgba(0, 0, 0, 255);
	private OnAction onPress;

	public Panel(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void render() {
		UIRendering.renderBox(rootX + x,
				GameResources.getInstance().getDisplay().getDisplayHeight() - rootY - y - height, width, height,
				fillColor, gradientColor, borderColor, fadeAlpha);
		super.render();
	}

	@Override
	public void update() {
		if (pressed() && onPress != null)
			onPress.onAction();
		super.update();
	}

	public boolean inside() {
		if (Mouse.getX() > rootX + x && Mouse.getY() > rootY + y && Mouse.getX() < rootX + x + width
				&& Mouse.getY() < rootY + y + height)
			return true;
		else
			return false;
	}

	public boolean pressed() {
		if (inside())
			if (Mouse.isButtonDown(0))
				return true;
			else
				return false;
		else
			return false;
	}

	public void setBorderColor(int r, int g, int b, int a) {
		UIRendering.rgba(r, g, b, a, borderColor);
	}

	public void setGradientColor(int r, int g, int b, int a) {
		UIRendering.rgba(r, g, b, a, gradientColor);
	}

	public void setFillColor(int r, int g, int b, int a) {
		UIRendering.rgba(r, g, b, a, fillColor);
	}

	public void setOnPress(OnAction onPress) {
		this.onPress = onPress;
	}

}
