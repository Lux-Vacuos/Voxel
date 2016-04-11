package net.luxvacuos.voxel.client.ui;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.menu.ItemGui;
import net.luxvacuos.voxel.client.world.block.Block;

public class Inventory {

	private ItemGui[][] items;
	private int sizeX, sizeY;
	private float xPos, yPos;
	private transient float xScale, yScale;
	private transient ItemGui tmp;
	private transient boolean push = false;
	private transient int x, y;

	public Inventory() {
		tmp = new ItemGui();
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
	}

	public Inventory(int sizeX, int sizeY, float xPos, float yPos) {
		items = new ItemGui[sizeX][sizeY];
		tmp = new ItemGui();
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xPos = xPos;
		this.yPos = yPos;
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				items[x][y] = new ItemGui(new Vector3f((xPos * 0.0164f + x - 0.1f) * 1.321f, (yPos + y - 0.44f) * 2.63f,
						(xPos * 0.0164f + x - 0.1f) * 1.321f), Block.Air);
			}
		}
	}

	public void render(GameResources gm) {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				items[x][y].getPosition().set((xPos * 0.0164f + x - 0.1f) * 1.321f, (yPos + y - 0.44f) * 2.63f,
						(xPos * 0.0164f + x - 0.1f) * 1.321f);
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				VectorsRendering.renderBox((xPos + (60 * x) + x) * xScale, (660 + (60 * -y) + yPos + -y) * yScale,
						60 * xScale, 60 * yScale, VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorA),
						VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorB),
						VectorsRendering.rgba(0, 0, 0, 255, VectorsRendering.colorC));
				if (items[x][y].getTotal() < 1)
					items[x][y].setBlock(Block.Air);

			}
		}
		if (push)
			VectorsRendering.renderBox((xPos + x * 60.8f) * xScale, 440 - (yPos + y * 60.8f) * yScale, 60 * xScale,
					60 * yScale, VectorsRendering.rgba(100, 255, 100, 120, VectorsRendering.colorA),
					VectorsRendering.rgba(0, 255, 0, 120, VectorsRendering.colorB),
					VectorsRendering.rgba(0, 255, 0, 120, VectorsRendering.colorC));
		else
			VectorsRendering.renderBox((xPos + x * 60.8f) * xScale, 440 - (yPos + y * 60.8f) * yScale, 60 * xScale,
					60 * yScale, VectorsRendering.rgba(255, 255, 255, 120, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 120, VectorsRendering.colorB),
					VectorsRendering.rgba(255, 255, 255, 120, VectorsRendering.colorC));
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				if (items[x][y].getBlock().getId() != Block.Air.getId()) {
					items[x][y].generateModel(gm.getItemsGuiRenderer().getTess());
					VectorsRendering.renderText("" + items[x][y].getTotal(), "Roboto-Bold",
							(xPos + (60 * x) + 2 + x) * xScale, (720 + (60 * -y) - 10 + yPos + -y) * yScale,
							20 * yScale, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
							VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));

				}
			}
		}
	}

	public void render(int sizeX, int sizeY, float xPos, float yPos, float cursorX, float cursorY, GameResources gm) {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				items[x][y].getPosition().set((xPos * 0.0164f + x - 0.1f) * 1.321f, (yPos + y - 0.44f) * 2.63f,
						(xPos * 0.0164f + x - 0.1f) * 1.321f);
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				VectorsRendering.renderBox((xPos + (60 * x) + x) * xScale, (660 + (60 * -y) + yPos + -y) * yScale,
						60 * xScale, 60 * yScale, VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorA),
						VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorB),
						VectorsRendering.rgba(0, 0, 0, 255, VectorsRendering.colorC));
				if (items[x][y].getTotal() < 1)
					items[x][y].setBlock(Block.Air);

			}
		}

		VectorsRendering.renderBox((xPos + cursorX * 60.8f) * xScale, 440 - (yPos + cursorY * 60.8f) * yScale,
				60 * xScale, 60 * yScale, VectorsRendering.rgba(255, 255, 255, 120, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 120, VectorsRendering.colorB),
				VectorsRendering.rgba(255, 255, 255, 120, VectorsRendering.colorC));
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				if (items[x][y].getBlock().getId() != Block.Air.getId()) {
					items[x][y].generateModel(gm.getItemsGuiRenderer().getTess());
					VectorsRendering.renderText("" + items[x][y].getTotal(), "Roboto-Bold",
							(xPos + (60 * x) + 2 + x) * xScale, (720 + (60 * -y) - 10 + yPos + -y) * yScale,
							20 * yScale, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
							VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));

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
					System.out.println("ll");
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

}