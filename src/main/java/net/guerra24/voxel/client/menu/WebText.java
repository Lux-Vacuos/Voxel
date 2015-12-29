package net.guerra24.voxel.client.menu;

public class WebText {

	private String text;
	private float fontSize;
	private boolean title;

	public WebText(String text, float fontSize, boolean title) {
		this.text = text;
		this.fontSize = fontSize;
		this.title = title;
	}

	public boolean isTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public float getFontSize() {
		return fontSize;
	}

}
