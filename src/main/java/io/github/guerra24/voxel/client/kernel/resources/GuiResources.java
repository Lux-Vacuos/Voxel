/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.resources;

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.resources.models.GuiTexture;

import org.lwjglx.util.vector.Vector2f;

/**
 * Gui Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.3 Build-59
 * @since 0.0.1 Build-52
 * @category Assets
 */
public class GuiResources {

	private GuiTexture button1;
	private GuiTexture button2;
	private GuiTexture worldLoading;
	private GuiTexture loadBar;

	public GuiTexture button5;
	public GuiTexture gui;
	public GuiTexture stats;
	public GuiTexture life;

	/**
	 * Constructor
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public GuiResources() {
		loadGuiTexture();
		addGuiTextures();
	}

	/**
	 * Load all Gui Assets
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadGuiTexture() {
		gui = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		stats = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("Stats"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		life = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("Life"),
				new Vector2f(-0.958f, 0.735f), new Vector2f(0.346f, 0.02f));
		button1 = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("ButtonExit"),
				new Vector2f(0.0f, -0.3f), new Vector2f(0.3f, 0.12f));
		button2 = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("ButtonPlay"),
				new Vector2f(0.0f, 0.3f), new Vector2f(0.3f, 0.12f));
		worldLoading = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("WorldLoading"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		loadBar = new GuiTexture(
				Kernel.gameResources.loader.loadTextureGui("LoadBar"),
				new Vector2f(-0.3f, 0.15f), new Vector2f(0.1f, 0.1f));
		button5 = new GuiTexture(button1.getTexture(),
				new Vector2f(0.0f, -0.7f), new Vector2f(0.2f, 0.12f));
	}

	/**
	 * Sends GUIs to the List
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void addGuiTextures() {
		Kernel.gameResources.guis.add(stats);
		Kernel.gameResources.guis.add(life);
		Kernel.gameResources.guis.add(gui);
		Kernel.gameResources.guis2.add(button1);
		Kernel.gameResources.guis2.add(button2);
		Kernel.gameResources.guis3.add(worldLoading);
		Kernel.gameResources.guis3.add(loadBar);
		Kernel.gameResources.guis4.add(button5);
	}
}
