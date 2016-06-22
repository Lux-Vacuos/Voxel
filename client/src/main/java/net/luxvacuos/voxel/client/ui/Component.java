package net.luxvacuos.voxel.client.ui;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.luxvacuos.voxel.client.util.Maths;

public abstract class Component {

	protected Queue<Component> childrens;
	protected int x, y;
	protected int rootX, rootY;
	protected int rootW, rootH;
	protected int width, height;
	protected float fadeAlpha = 1;
	protected boolean enabled = true;
	protected boolean positionRelativeToRoot = true;

	public Component() {
		childrens = new ConcurrentLinkedQueue<>();
	}

	public void render() {
		if (enabled)
			for (Component component : childrens) {
				if (component.positionRelativeToRoot) {
					component.rootX = x + rootX;
					component.rootY = y + rootY;
					component.rootW = width;
					component.rootH = height;
				}
				component.fadeAlpha = fadeAlpha;
				component.render();
			}
	}

	public void update() {
		Maths.clamp(fadeAlpha, 0, 1);
		if (enabled)
			for (Component component : childrens) {
				component.update();
			}
	}

	public void addChildren(Component comp) {
		childrens.add(comp);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Queue<Component> getChildrens() {
		return childrens;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setPositionRelativeToRoot(boolean positionRelativeToRoot) {
		this.positionRelativeToRoot = positionRelativeToRoot;
	}

	public boolean isPositionRelativeToRoot() {
		return positionRelativeToRoot;
	}

}
