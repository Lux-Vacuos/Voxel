package net.guerra24.voxel.client.engine.resources;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.render.textures.types.GuiTexture;

import org.lwjgl.util.vector.Vector2f;

public class GuiResources {

	private static GuiTexture gui;
	private static GuiTexture menu;
	public static GuiTexture world;
	private static GuiTexture button1;
	private static GuiTexture button2;
	public static GuiTexture button3;
	public static GuiTexture button5;
	public static GuiTexture wselect;
	public static GuiTexture wnoselect;
	public static GuiTexture gui2;

	public static void loadGuiTexture() {
		gui = new GuiTexture(Engine.loader.loadTextureGui("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		menu = new GuiTexture(Engine.loader.loadTextureGui("MainMenu"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		world = new GuiTexture(Engine.loader.loadTextureGui("WSelection"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		button1 = new GuiTexture(Engine.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, 0.0f), new Vector2f(0.2f, 0.12f));
		button2 = new GuiTexture(Engine.loader.loadTextureGui("ButtonPlay"),
				new Vector2f(0.0f, 0.3f), new Vector2f(0.2f, 0.12f));
		button3 = new GuiTexture(Engine.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.5f, -0.7f), new Vector2f(0.2f, 0.12f));
		button5 = new GuiTexture(Engine.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.7f), new Vector2f(0.2f, 0.12f));
		wselect = new GuiTexture(Engine.loader.loadTextureGui("WorldSelect"),
				new Vector2f(-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		wnoselect = new GuiTexture(
				Engine.loader.loadTextureGui("WorldnoSelect"), new Vector2f(
						-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		// gui2 = new GuiTexture(fbos.getReflectionTexture(),
		// new Vector2f(-0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
	}

	public static void addGuiTextures() {
		Engine.guis.add(gui);
		Engine.guis2.add(button1);
		Engine.guis2.add(button2);
		Engine.guis2.add(menu);
		Engine.guis3.add(wnoselect);
		Engine.guis3.add(button3);
		Engine.guis3.add(world);
		Engine.guis4.add(button5);
	}

}
