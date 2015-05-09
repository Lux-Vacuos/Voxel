package net.guerra24.voxel.client.engine.resources;

import net.guerra24.voxel.client.engine.render.textures.types.GuiTexture;

import org.lwjgl.util.vector.Vector2f;

public class GuiResources {

	private static GuiTexture gui;
	private static GuiTexture menu;
	public static GuiTexture load;
	public static GuiTexture loadW;
	public static GuiTexture world;
	private static GuiTexture button1;
	private static GuiTexture button2;
	public static GuiTexture button3;
	public static GuiTexture button4;
	public static GuiTexture button5;
	public static GuiTexture wselect;
	public static GuiTexture wnoselect;
	public static GuiTexture gui2;
	public static GuiTexture loadBar;

	public static void loadGuiTexture() {
		gui = new GuiTexture(GameResources.loader.loadTextureGui("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		menu = new GuiTexture(GameResources.loader.loadTextureGui("MainMenu"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		world = new GuiTexture(
				GameResources.loader.loadTextureGui("WSelection"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		button1 = new GuiTexture(
				GameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.3f), new Vector2f(0.3f, 0.12f));
		button2 = new GuiTexture(
				GameResources.loader.loadTextureGui("ButtonPlay"),
				new Vector2f(0.0f, 0.3f), new Vector2f(0.3f, 0.12f));
		button3 = new GuiTexture(
				GameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.5f, -0.7f), new Vector2f(0.2f, 0.12f));
		button4 = new GuiTexture(
				GameResources.loader.loadTextureGui("ButtonMulti"),
				new Vector2f(0.0f, 0.0f), new Vector2f(0.3f, 0.12f));
		button5 = new GuiTexture(
				GameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.7f), new Vector2f(0.2f, 0.12f));
		wselect = new GuiTexture(
				GameResources.loader.loadTextureGui("WorldSelect"),
				new Vector2f(-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		wnoselect = new GuiTexture(
				GameResources.loader.loadTextureGui("WorldnoSelect"),
				new Vector2f(-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		loadW = new GuiTexture(GameResources.loader.loadTextureGui("LoadingW"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		gui2 = new GuiTexture(GameResources.fbos.getReflectionTexture(),
				new Vector2f(-0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		loadBar = new GuiTexture(
				GameResources.loader.loadTextureGui("LoadBar"), new Vector2f(
						-0.9f, 0f), new Vector2f(0.5f, 0.5f));
	}

	public static void loadingGui() {
		load = new GuiTexture(GameResources.loader.loadTextureGui("Loading"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));

		GameResources.guis5.add(load);
	}

	public static void addGuiTextures() {
		GameResources.guis.add(gui);
		// GameResources.guis.add(gui2);
		GameResources.guis2.add(button1);
		GameResources.guis2.add(button2);
		GameResources.guis2.add(button4);
		GameResources.guis2.add(menu);
		GameResources.guis3.add(wnoselect);
		GameResources.guis3.add(button3);
		GameResources.guis3.add(world);
		GameResources.guis4.add(button5);
	}

}
