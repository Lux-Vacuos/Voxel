/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.world.block;

import java.util.HashMap;

import net.luxvacuos.voxel.client.world.block.types.BlockAir;
import net.luxvacuos.voxel.client.world.block.types.BlockCobblestone;
import net.luxvacuos.voxel.client.world.block.types.BlockDiamondOre;
import net.luxvacuos.voxel.client.world.block.types.BlockDirt;
import net.luxvacuos.voxel.client.world.block.types.BlockGlass;
import net.luxvacuos.voxel.client.world.block.types.BlockGoldOre;
import net.luxvacuos.voxel.client.world.block.types.BlockGrass;
import net.luxvacuos.voxel.client.world.block.types.BlockIce;
import net.luxvacuos.voxel.client.world.block.types.BlockIndes;
import net.luxvacuos.voxel.client.world.block.types.BlockLava;
import net.luxvacuos.voxel.client.world.block.types.BlockLeaves;
import net.luxvacuos.voxel.client.world.block.types.BlockSand;
import net.luxvacuos.voxel.client.world.block.types.BlockStone;
import net.luxvacuos.voxel.client.world.block.types.BlockTorch;
import net.luxvacuos.voxel.client.world.block.types.BlockWater;
import net.luxvacuos.voxel.client.world.block.types.BlockWood;

/**
 * Block
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public class Block {
	/**
	 * Blocks Data
	 */
	public static final HashMap<Byte, BlockBase> blockMap = new HashMap<Byte, BlockBase>();

	public static final BlockBase Air = new BlockAir();
	public static final BlockBase Stone = new BlockStone();
	public static final BlockBase Grass = new BlockGrass();
	public static final BlockBase Sand = new BlockSand();
	public static final BlockBase Dirt = new BlockDirt();
	public static final BlockBase DiamondOre = new BlockDiamondOre();
	public static final BlockBase GoldOre = new BlockGoldOre();
	public static final BlockBase Water = new BlockWater();
	public static final BlockBase Glass = new BlockGlass();
	public static final BlockBase Torch = new BlockTorch();
	public static final BlockBase Leaves = new BlockLeaves();
	public static final BlockBase Wood = new BlockWood();
	public static final BlockBase Ice = new BlockIce();
	public static final BlockBase Indes = new BlockIndes();
	public static final BlockBase Cobblestone = new BlockCobblestone();
	public static final BlockBase Lava = new BlockLava();

	public static void initBasicBlocks() {
		registerBlock(Indes);
		registerBlock(Air);
		registerBlock(Stone);
		registerBlock(Grass);
		registerBlock(Sand);
		registerBlock(Dirt);
		registerBlock(DiamondOre);
		registerBlock(GoldOre);
		registerBlock(Water);
		registerBlock(Glass);
		registerBlock(Torch);
		registerBlock(Lava);
		registerBlock(Leaves);
		registerBlock(Wood);
		registerBlock(Ice);
		registerBlock(Cobblestone);
	}

	/**
	 * Register a Block to the map
	 * 
	 * @param id
	 *            Block ID
	 * @param block
	 *            Block
	 */
	public static void registerBlock(BlockBase block) {
		blockMap.put(block.getId(), block);
	}

	/**
	 * Get the block in the position id of the map
	 * 
	 * @param id
	 *            Block ID
	 * @return Block
	 */
	public static BlockBase getBlock(byte id) {
		return blockMap.get(id);
	}

}
