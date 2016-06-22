package net.luxvacuos.voxel.client.ui;

import org.lwjgl.nanovg.NanoVG;

public class World extends Component {

	private String name;
	private Window info;
	private boolean pressed = false;

	public World(int x, int y, int w, int h, String name) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		Panel panel = new Panel(0, 0, w, h);
		panel.setBorderColor(0, 0, 0, 255);
		if (pressed)
			panel.setFillColor(255, 255, 255, 255);
		else
			panel.setFillColor(255, 255, 255, 100);
		panel.setOnPress(() -> {
			pressed = !pressed;
			if (pressed)
				panel.setFillColor(255, 255, 255, 255);
			else
				panel.setFillColor(255, 255, 255, 100);
		});
		addChildren(panel);

		Text text = new Text(name, 10, h / 2);
		text.setColor(80, 80, 80, 255);
		addChildren(text);

		Button btn = new Button(w - 60, 2, 58, h - 4, "Info");
		btn.setOnButtonPress(() -> {
			info.setEnabled(!info.isEnabled());
		});
		addChildren(btn);

		info = new Window(w + 10, h, 300, 200, "World " + name + " info");
		info.setEnabled(false);
		Text infoText = new Text("WORK IN PROGRESS", 150, -100);
		infoText.setAlign(NanoVG.NVG_ALIGN_CENTER);
		info.addChildren(infoText);

		addChildren(info);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
