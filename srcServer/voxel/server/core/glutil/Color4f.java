package voxel.server.core.glutil;

public class Color4f {

	public static final Color4f BLACK = new Color4f(0, 0, 0, 1);
	public static final Color4f WHITE = new Color4f(1, 1, 1, 1);
	public static final Color4f BLUE = new Color4f(0, 0, 1, 1);
	public static final Color4f RED = new Color4f(1, 0, 0, 1);
	public static final Color4f GREEN = new Color4f(0, 1, 0, 1);
	public static final Color4f GRAY = new Color4f(0.5f, 0.5f, 0.5f, 1);
	public static final Color4f DEFAULT = WHITE;

	public float r, g, b, a;

	public Color4f(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color4f getColor() {
		return this;
	}

}
