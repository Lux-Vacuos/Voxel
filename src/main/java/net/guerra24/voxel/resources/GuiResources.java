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

package net.guerra24.voxel.resources;

import net.guerra24.voxel.resources.models.GuiTexture;
import net.guerra24.voxel.util.vector.Vector2f;

/**
 * Gui Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class GuiResources {

	/**
	 * GuiResources Data
	 */
	private GuiTexture worldLoading;
	private GuiTexture loadBar;
	private GuiTexture button1;
	private GuiTexture button2;
	public GuiTexture button5;
	public GuiTexture gui;
	public GuiTexture stats;
	public GuiTexture life;
	private int button;

	/**
	 * Constructor
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public GuiResources(GameResources gm) {
		loadGuiTexture(gm);
		addGuiTextures(gm);
	}

	/**
	 * Load all Gui Assets
	 * 
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadGuiTexture(GameResources gm) {
		button = gm.getLoader().loadTextureGui("Button");
		gui = new GuiTexture(gm.getLoader().loadTextureGui("HotBar"), new Vector2f(0.6f, -0.425f),
				new Vector2f(1.6f, 1.425f));
		stats = new GuiTexture(gm.getLoader().loadTextureGui("Stats"), new Vector2f(0.6f, -0.425f),
				new Vector2f(1.6f, 1.425f));
		life = new GuiTexture(gm.getLoader().loadTextureGui("Life"), new Vector2f(-0.958f, 0.735f),
				new Vector2f(0.346f, 0.02f));
		button1 = new GuiTexture(button, new Vector2f(0.0f, -0.3f), new Vector2f(0.3f, 0.12f));
		button2 = new GuiTexture(button, new Vector2f(0.0f, 0.3f), new Vector2f(0.3f, 0.12f));
		worldLoading = new GuiTexture(gm.getLoader().loadTextureGui("WorldLoading"), new Vector2f(0.6f, -0.425f),
				new Vector2f(1.6f, 1.425f));
		loadBar = new GuiTexture(gm.getLoader().loadTextureGui("LoadBar"), new Vector2f(-0.3f, 0.15f),
				new Vector2f(0.0f, 0.1f));
		button5 = new GuiTexture(gm.getLoader().loadTextureGui("ButtonExit"), new Vector2f(0.0f, -0.7f),
				new Vector2f(0.2f, 0.12f));
	}

	/**
	 * Sends GUIs to the List
	 * 
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void addGuiTextures(GameResources gm) {
		gm.guis.add(stats);
		gm.guis.add(life);
		gm.guis.add(gui);
		gm.guis2.add(button1);
		gm.guis2.add(button2);
		gm.guis3.add(worldLoading);
		gm.guis3.add(loadBar);
		gm.guis4.add(button5);
	}
}
