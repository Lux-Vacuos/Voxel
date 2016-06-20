package net.luxvacuos.voxel.client.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class Component {

	protected List<Component> childrens;
	protected int x, y;
	protected int rootX, rootY;
	protected int width, height;

	public Component() {
		childrens = new ArrayList<>();
	}

	public abstract void render();

	public abstract void update();

	public void addChilder(Component comp) {
		childrens.add(comp);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
