package voxel.server.core.glutil;

public class Font {

	private Texture texture;

	public Texture loadFont(String name, String location) {
		texture = Texture.loadTexture(location);
		ResourceManager.addTexture(name, texture);
		return texture;
	}

	public void bind() {
		texture.bind();
	}

	public void unbind() {
		texture.unbind();
	}

	public void delete() {
		texture.delete();
	}

}
