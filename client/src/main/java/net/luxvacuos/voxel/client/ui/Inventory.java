/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.ui;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.menu.ItemGui;
import net.luxvacuos.voxel.client.world.block.Block;

public class Inventory {

	private ItemGui[][] items;
	private transient int sizeX, sizeY;
	private transient float xPos, yPos;
	private transient ItemGui tmp;
	private transient boolean push = false;
	private transient int x, y;

	public Inventory() {
		tmp = new ItemGui();
	}

	public Inventory(int sizeX, int sizeY, float xPos, float yPos) {
		items = new ItemGui[sizeX][sizeY];
		tmp = new ItemGui();
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xPos = xPos;
		this.yPos = yPos;
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				items[x][y] = new ItemGui(new Vector3f((xPos * 0.0164f + x - 0.1f) * 1.321f, (yPos + y - 0.44f) * 2.63f,
						(xPos * 0.0164f + x - 0.1f) * 1.321f), Block.Air);
			}
		}
	}

	public void render(GameResources gm) {
		Window window = gm.getGameWindow();
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				items[x][y].getPosition().set((xPos * 0.0242f + x) * 1.321f, (yPos + y - 0.45f) * 2.659f,
						(xPos * 0.0242f + x) * 1.321f);
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				UIRendering.renderBox(window.getID(), (xPos + (40 * x) + x),
						(window.getHeight() + (40 * (-y - 1)) + yPos + -y), 40,
						40, UIRendering.rgba(255, 255, 255, 100, UIRendering.colorA),
						UIRendering.rgba(255, 255, 255, 100, UIRendering.colorB),
						UIRendering.rgba(0, 0, 0, 255, UIRendering.colorC));
				if (items[x][y].getTotal() < 1)
					items[x][y].setBlock(Block.Air);

			}
		}
		if (push)
			UIRendering.renderBox(window.getID(), (xPos + x * 41f),
					(window.getHeight() - (yPos + (1 + y) * 41f) + 1f), 40, 40,
					UIRendering.rgba(100, 255, 100, 120, UIRendering.colorA),
					UIRendering.rgba(0, 255, 0, 120, UIRendering.colorB),
					UIRendering.rgba(0, 255, 0, 120, UIRendering.colorC));
		else
			UIRendering.renderBox(window.getID(), (xPos + x * 41f),
					(window.getHeight() - (yPos + (1 + y) * 41f) + 1f), 40, 40,
					UIRendering.rgba(255, 255, 255, 120, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 120, UIRendering.colorB),
					UIRendering.rgba(255, 255, 255, 120, UIRendering.colorC));

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				if (items[x][y].getBlock().getId() != Block.Air.getId()) {
					UIRendering.renderText(window.getID(), "" + items[x][y].getTotal(), "Roboto-Bold", (xPos + (40 * x) + 2 + x),
							(window.getHeight() + (40 * -y) - 10 + yPos + -y),
							15, UIRendering.rgba(255, 255, 255, 255, UIRendering.colorA),
							UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));

				}
			}
		}
	}

	public void render(int sizeX, int sizeY, float xPos, float yPos, float cursorX, float cursorY, GameResources gm) {
		Window window = gm.getGameWindow();
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				items[x][y].getPosition().set((xPos * 0.0241f + x) * 1.321f - 0.12f, (yPos + y - 0.45f) * 2.659f,
						(xPos * 0.0242f + x) * 1.321f - 0.12f);
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				UIRendering.renderBox(window.getID(), (xPos + (40 * x) + x),
						(window.getHeight() + (40 * (-y + -1)) + yPos + -y),
						40, 40, UIRendering.rgba(255, 255, 255, 100, UIRendering.colorA),
						UIRendering.rgba(255, 255, 255, 100, UIRendering.colorB),
						UIRendering.rgba(0, 0, 0, 255, UIRendering.colorC));
				if (items[x][y].getTotal() < 1)
					items[x][y].setBlock(Block.Air);

			}
		}
		UIRendering
				.renderBox(window.getID(), (xPos + cursorX * 41f),
						(window.getHeight() - (yPos + (cursorY + 1) * 41f)
								+ 1f),
						40, 40, UIRendering.rgba(255, 255, 255, 120, UIRendering.colorA),
						UIRendering.rgba(255, 255, 255, 120, UIRendering.colorB),
						UIRendering.rgba(255, 255, 255, 120, UIRendering.colorC));
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				if (items[x][y].getBlock().getId() != Block.Air.getId()) {
					UIRendering.renderText(window.getID(), "" + items[x][y].getTotal(), 
							"Roboto-Bold", (xPos + (40 * x) + 2 + x),
							(window.getHeight() + (40 * -y) - 10 + yPos + -y),
							15, UIRendering.rgba(255, 255, 255, 255, UIRendering.colorA),
							UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));

				}
			}
		}
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void push(int x, int y) {
		if (!push) {
			if (x >= 0 && y >= 0 && x < sizeX && y < sizeY) {
				push = true;
				tmp.setBlock(items[x][y].getBlock());
				tmp.setTotal(items[x][y].getTotal());
				items[x][y].setBlock(Block.Air);
				items[x][y].setTotal(0);
			}
		}
	}

	public void pop(int x, int y) {
		if (push) {
			if (x >= 0 && y >= 0 && x < sizeX && y < sizeY) {
				if (items[x][y].getBlock() == Block.Air) {
					push = false;
					items[x][y].setBlock(tmp.getBlock());
					items[x][y].setTotal(tmp.getTotal());
					tmp.setBlock(Block.Air);
					tmp.setTotal(0);
				} else if (items[x][y].getBlock().getId() == tmp.getBlock().getId()) {
					push = false;
					items[x][y].setTotal(items[x][y].getTotal() + tmp.getTotal());
					tmp.setBlock(Block.Air);
					tmp.setTotal(0);
				}
			}
		}
	}

	public void clearInventorty() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				items[x][y].setBlock(Block.Air);
			}
		}
	}

	public ItemGui[][] getItems() {
		return items;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}
	
	public void setItems(ItemGui[][] items) {
		this.items = items;
	}

}