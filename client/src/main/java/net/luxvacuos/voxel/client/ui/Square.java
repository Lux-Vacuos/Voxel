package net.luxvacuos.voxel.client.ui;

import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Square extends Component {

	public Square(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void render() {
		VectorsRendering.renderBox(rootX + x, GameResources.getInstance().getDisplay().getDisplayHeight() - rootY + y,
				width, height, VectorsRendering.rgba(255, 255, 255, 200, VectorsRendering.colorA),
				VectorsRendering.rgba(32, 32, 32, 32, VectorsRendering.colorB),
				VectorsRendering.rgba(0, 0, 0, 48, VectorsRendering.colorC));
	}

	@Override
	public void update() {
	}
}
