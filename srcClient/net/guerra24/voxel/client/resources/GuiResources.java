package net.guerra24.voxel.client.resources;

import net.guerra24.voxel.client.kernel.Engine;
import net.guerra24.voxel.client.kernel.render.textures.types.GuiTexture;

import org.lwjgl.util.vector.Vector2f;

public class GuiResources {

	public GuiTexture gui;
	public GuiTexture menu;
	public static GuiTexture load;
	public GuiTexture loadW;
	public GuiTexture world;
	private GuiTexture button1;
	private GuiTexture button2;
	public GuiTexture button3;
	public GuiTexture button4;
	public GuiTexture button5;
	public GuiTexture wselect;
	public GuiTexture wnoselect;
	public GuiTexture refraction;
	public GuiTexture reflection;
	public GuiTexture loadBar;

	public GuiResources() {
		loadGuiTexture();
		addGuiTextures();
	}

	public void loadGuiTexture() {
		gui = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		menu = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("MainMenu"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		world = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("WSelection"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		button1 = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.3f), new Vector2f(0.3f, 0.12f));
		button2 = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("ButtonPlay"),
				new Vector2f(0.0f, 0.3f), new Vector2f(0.3f, 0.12f));
		button3 = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.5f, -0.7f), new Vector2f(0.2f, 0.12f));
		button4 = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("ButtonMulti"),
				new Vector2f(0.0f, 0.0f), new Vector2f(0.3f, 0.12f));
		button5 = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.7f), new Vector2f(0.2f, 0.12f));
		wselect = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("WorldSelect"),
				new Vector2f(-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		wnoselect = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("WorldnoSelect"),
				new Vector2f(-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		loadW = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("LoadingW"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		loadBar = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("LoadBar"),
				new Vector2f(-0.9f, 0f), new Vector2f(0.5f, 0.5f));
		refraction = new GuiTexture(
				Engine.gameResources.fbos.getReflectionTexture(), new Vector2f(
						0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		reflection = new GuiTexture(
				Engine.gameResources.fbos1.getReflectionTexture(),
				new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
	}

	public static void loadingGui() {
		load = new GuiTexture(
				Engine.gameResources.loader.loadTextureGui("Loading"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));

		Engine.gameResources.guis5.add(load);
	}

	public void addGuiTextures() {
		Engine.gameResources.guis.add(gui);
		Engine.gameResources.guis.add(refraction);
		Engine.gameResources.guis.add(reflection);
		Engine.gameResources.guis2.add(button1);
		Engine.gameResources.guis2.add(button2);
		Engine.gameResources.guis2.add(button4);
		Engine.gameResources.guis2.add(menu);
		Engine.gameResources.guis3.add(wnoselect);
		Engine.gameResources.guis3.add(button3);
		Engine.gameResources.guis3.add(world);
		Engine.gameResources.guis4.add(button5);
	}

}
