package net.luxvacuos.voxel.client.ui;

import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Image extends Component {

	private int image;

	public Image(int x, int y, int w, int h, int image) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.image = image;
	}

	@Override
	public void render() {
		UIRendering.renderImage(rootX + x, GameResources.getInstance().getDisplay().getDisplayHeight() - rootY - y,
				width, height, image, fadeAlpha);
		super.render();
	}

}
