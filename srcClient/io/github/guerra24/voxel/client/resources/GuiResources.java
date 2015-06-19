package io.github.guerra24.voxel.client.resources;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.render.textures.GuiTexture;

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
	public GuiTexture button5;
	public GuiTexture wselect;
	public GuiTexture wnoselect;
	public GuiTexture loadBar;

	public GuiResources() {
		loadGuiTexture();
		addGuiTextures();
	}

	public void loadGuiTexture() {
		gui = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		menu = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("MainMenu"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		world = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("WSelection"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		button1 = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.3f), new Vector2f(0.3f, 0.12f));
		button2 = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("ButtonPlay"),
				new Vector2f(0.0f, 0.3f), new Vector2f(0.3f, 0.12f));
		button3 = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.5f, -0.7f), new Vector2f(0.2f, 0.12f));
		button5 = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.7f), new Vector2f(0.2f, 0.12f));
		wselect = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("WorldSelect"),
				new Vector2f(-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		wnoselect = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("WorldnoSelect"),
				new Vector2f(-0.42f, 0.5f), new Vector2f(0.5f, 0.14f));
		loadW = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("LoadingW"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		loadBar = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("LoadBar"),
				new Vector2f(-0.9f, 0f), new Vector2f(0.5f, 0.5f));
	}

	public static void loadingGui() {
		load = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("Loading"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));

		Kernel.gameResources.guis5.add(load);
	}

	public void addGuiTextures() {
		Kernel.gameResources.guis.add(gui);
		Kernel.gameResources.guis2.add(button1);
		Kernel.gameResources.guis2.add(button2);
		Kernel.gameResources.guis2.add(menu);
		Kernel.gameResources.guis3.add(wnoselect);
		Kernel.gameResources.guis3.add(button3);
		Kernel.gameResources.guis3.add(world);
		Kernel.gameResources.guis4.add(button5);
	}

}
