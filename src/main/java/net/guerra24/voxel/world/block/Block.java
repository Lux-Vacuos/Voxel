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

package net.guerra24.voxel.world.block;

import java.util.HashMap;

import net.guerra24.voxel.world.block.types.BlockAir;
import net.guerra24.voxel.world.block.types.BlockDimOre;
import net.guerra24.voxel.world.block.types.BlockDirt;
import net.guerra24.voxel.world.block.types.BlockGlass;
import net.guerra24.voxel.world.block.types.BlockGoldOre;
import net.guerra24.voxel.world.block.types.BlockGrass;
import net.guerra24.voxel.world.block.types.BlockIndes;
import net.guerra24.voxel.world.block.types.BlockLeaves;
import net.guerra24.voxel.world.block.types.BlockNull;
import net.guerra24.voxel.world.block.types.BlockPortal;
import net.guerra24.voxel.world.block.types.BlockSand;
import net.guerra24.voxel.world.block.types.BlockStone;
import net.guerra24.voxel.world.block.types.BlockTorch;
import net.guerra24.voxel.world.block.types.BlockWater;
import net.guerra24.voxel.world.block.types.BlockWood;

/**
 * Block
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public abstract class Block {
	/**
	 * Blocks Data
	 */
	public static HashMap<Byte, IBlock> blockMap = new HashMap<Byte, IBlock>();

	public static IBlock Grass = new BlockGrass();
	public static IBlock Stone = new BlockStone();
	public static IBlock NULL = new BlockNull();
	public static IBlock Indes = new BlockIndes();
	public static IBlock Sand = new BlockSand();
	public static IBlock Dirt = new BlockDirt();
	public static IBlock DiamondOre = new BlockDimOre();
	public static IBlock GoldOre = new BlockGoldOre();
	public static IBlock Water = new BlockWater();
	public static IBlock Glass = new BlockGlass();
	public static IBlock Air = new BlockAir();
	public static IBlock Torch = new BlockTorch();
	public static IBlock Portal = new BlockPortal();
	public static IBlock Leaves = new BlockLeaves();
	public static IBlock Wood = new BlockWood();

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
		registerBlock(Portal.getId(), Portal);
		registerBlock(Leaves.getId(), Leaves);
		registerBlock(Wood.getId(), Wood);
	}

	/**
	 * Register a Block to the map
	 * 
	 * @param id
	 *            Block ID
	 * @param block
	 *            Block
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void registerBlock(byte id, IBlock block) {
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
	public static IBlock getBlock(byte id) {
		return blockMap.get(id);
	}

}
