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

package net.luxvacuos.voxel.server.world.block;

import net.luxvacuos.voxel.server.world.block.types.BlockAir;
import net.luxvacuos.voxel.server.world.block.types.BlockCobblestone;
import net.luxvacuos.voxel.server.world.block.types.BlockDiamondOre;
import net.luxvacuos.voxel.server.world.block.types.BlockDirt;
import net.luxvacuos.voxel.server.world.block.types.BlockGlass;
import net.luxvacuos.voxel.server.world.block.types.BlockGoldOre;
import net.luxvacuos.voxel.server.world.block.types.BlockGrass;
import net.luxvacuos.voxel.server.world.block.types.BlockIce;
import net.luxvacuos.voxel.server.world.block.types.BlockIndes;
import net.luxvacuos.voxel.server.world.block.types.BlockLava;
import net.luxvacuos.voxel.server.world.block.types.BlockLeaves;
import net.luxvacuos.voxel.server.world.block.types.BlockNode;
import net.luxvacuos.voxel.server.world.block.types.BlockPedestal;
import net.luxvacuos.voxel.server.world.block.types.BlockSand;
import net.luxvacuos.voxel.server.world.block.types.BlockStone;
import net.luxvacuos.voxel.server.world.block.types.BlockTorch;
import net.luxvacuos.voxel.server.world.block.types.BlockWater;
import net.luxvacuos.voxel.server.world.block.types.BlockWood;

/**
 * Block
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public class Block {

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
	public static final BlockBase Lava = new BlockLava();
	public static final BlockBase Leaves = new BlockLeaves();
	public static final BlockBase Wood = new BlockWood();
	public static final BlockBase Ice = new BlockIce();
	public static final BlockBase Indes = new BlockIndes();
	public static final BlockBase Cobblestone = new BlockCobblestone();
	public static final BlockBase Pedestal = new BlockPedestal();
	public static final BlockBase Node = new BlockNode();

}
