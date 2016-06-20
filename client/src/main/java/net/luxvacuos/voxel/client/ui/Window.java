package net.luxvacuos.voxel.client.ui;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Window extends Component {

	private String title, font;

	public Window(int x, int y, int w, int h, String title, String font) {
		super();
		this.title = title;
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	@Override
	public void render() {
		VectorsRendering.renderWindow(title, font, x, GameResources.getInstance().getDisplay().getDisplayHeight() - y,
				width, height);
		for (Component component : childrens) {
			component.rootX = x;
			component.rootY = y;
			component.render();
		}
	}

	@Override
	public void update() {
		if (Mouse.isButtonDown(0) && Mouse.getX() > x && Mouse.getY() < y && Mouse.getX() < x + width
				&& Mouse.getY() > y - 32) {
			this.x = Mouse.getX() - width / 2;
			this.y = Mouse.getY() + 32 / 2;
		}
	}

}
