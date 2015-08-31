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

package io.github.guerra24.voxel.client.kernel.world.block;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockAir;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockDimOre;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockDirt;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockGlass;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockGoldOre;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockGrass;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockIndes;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockNull;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockSand;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockStone;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockTorch;
import io.github.guerra24.voxel.client.kernel.world.block.types.BlockWater;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;

import java.util.HashMap;

/**
 * Block
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public abstract class Block {
	/**
	 * Map of blocks
	 */
	public static HashMap<Byte, Block> blockMap = new HashMap<Byte, Block>();

	public static Block Grass = new BlockGrass();
	public static Block Stone = new BlockStone();
	public static Block NULL = new BlockNull();
	public static Block Indes = new BlockIndes();
	public static Block Sand = new BlockSand();
	public static Block Dirt = new BlockDirt();
	public static Block DiamondOre = new BlockDimOre();
	public static Block GoldOre = new BlockGoldOre();
	public static Block Water = new BlockWater();
	public static Block Glass = new BlockGlass();
	public static Block Air = new BlockAir();
	public static Block Torch = new BlockTorch();

	/**
	 * Gets the Block ID
	 * 
	 * @return ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract byte getId();

	/**
	 * Get the Face Up of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceUp(Vector3f pos);

	/**
	 * Get the Face Down of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return Entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceDown(Vector3f pos);

	/**
	 * Get the Face East of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceEast(Vector3f pos);

	/**
	 * Get the Face West of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 */
	public abstract BlockEntity getFaceWest(Vector3f pos);

	/**
	 * Get the Face North of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceNorth(Vector3f pos);

	/**
	 * Get the Face South of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getFaceSouth(Vector3f pos);

	/**
	 * Get the WaterTile of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return WaterTile
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract WaterTile getWaterTitle(Vector3f pos);

	/**
	 * Get a single model
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract BlockEntity getSingleModel(Vector3f pos);

	/**
	 * Initialize the basic Block
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void initBasicBlocks() {
		registerBlock(Indes.getId(), Indes);
		registerBlock(Air.getId(), Air);
		registerBlock(Stone.getId(), Stone);
		registerBlock(Grass.getId(), Grass);
		registerBlock(Sand.getId(), Sand);
		registerBlock(Dirt.getId(), Dirt);
		registerBlock(DiamondOre.getId(), DiamondOre);
		registerBlock(GoldOre.getId(), GoldOre);
		registerBlock(Water.getId(), Water);
		registerBlock(Glass.getId(), Glass);
		registerBlock(Torch.getId(), Torch);
	}

	/**
	 * Register a Block to the map
	 * 
	 * @param id
	 *            Bloc ID
	 * @param block
	 *            Block
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void registerBlock(byte id, Block block) {
		blockMap.put(id, block);
	}

	/**
	 * Get the block in the position id of the map
	 * 
	 * @param id
	 *            Block ID
	 * @return Block
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static Block getBlock(byte id) {
		return blockMap.get(id);
	}

}
