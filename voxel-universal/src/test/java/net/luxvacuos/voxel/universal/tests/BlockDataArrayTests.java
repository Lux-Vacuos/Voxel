/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.universal.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.luxvacuos.voxel.universal.world.utils.BlockIntDataArray;
import net.luxvacuos.voxel.universal.world.utils.BlockLongDataArray;

public class BlockDataArrayTests {
	private BlockIntDataArray bda0;
	private BlockLongDataArray bda1;
	private Random random;

	@Before
	public void setUp() throws Exception {
		this.bda0 = new BlockIntDataArray();
		this.bda1 = new BlockLongDataArray();
		this.random = new Random(System.currentTimeMillis());
	}

	@After
	public void tearDown() throws Exception {
		this.bda0 = null;
		this.bda1 = null;
		this.random = null;
	}

	@Test
	public void dataArrayZeroEqualityTestInt() {
		System.out.print("Testing BlockIntDataArray for Zero Equality... ");
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < 16; y++) {
				for(int z = 0; z < 16; z++) {
					assertTrue("Int Array Index not 0 at position (X: " + x +", Y: "+ y + ", Z: " + z + ")", this.bda0.get(x, y, z) == 0);
				}
			}
		}
		System.out.println("Done!");
	}
	
	@Test
	public void dataArrayZeroEqualityTestLong() {
		System.out.print("Testing BlockLongDataArray for Zero Equality... ");
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < 16; y++) {
				for(int z = 0; z < 16; z++) {
					assertTrue("Long Array Index not 0 at position (X: " + x +", Y: "+ y + ", Z: " + z + ")", this.bda1.get(x, y, z) == 0);
				}
			}
		}
		System.out.println("Done!");
	}
	
	@Test
	public void dataArrayRandomTestInt() {
		System.out.print("Testing BlockIntDataArray lookup equality with Random Array... ");
		int[] randArray = new int[this.bda0.getData().length];
		int i;
		
		for(i = 0; i < randArray.length; i++) {
			randArray[i] = this.random.nextInt();
		}
		
		this.bda0 = new BlockIntDataArray(randArray);
		
		boolean flag;
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < 16; y++) {
				for(int z = 0; z < 16; z++) {
					flag = this.bda0.get(x, y, z) == randArray[((16 * 16 * z) + (16 * y)) + x];
					assertTrue("Int Array Index not " + randArray[((16 * 16 * z) + (16 * y)) + x]
							+ " at position (X: " + x +", Y: "+ y + ", Z: " + z + "), got " + this.bda0.get(x, y, z), flag);
				}
			}
		}
		System.out.println("Done!");
	}
	
	@Test
	public void dataArrayRandomTestLong() {
		System.out.print("Testing BlockLongDataArray lookup equality with Random Array... ");
		long[] randArray = new long[this.bda1.getData().length];
		int i;
		
		for(i = 0; i < randArray.length; i++) {
			randArray[i] = this.random.nextLong();
		}
		
		this.bda1 = new BlockLongDataArray(randArray);
		
		boolean flag;
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < 16; y++) {
				for(int z = 0; z < 16; z++) {
					flag = this.bda1.get(x, y, z) == randArray[((16 * 16 * z) + (16 * y)) + x];
					assertTrue("Long Array Index not " + randArray[((16 * 16 * z) + (16 * y)) + x]
							+ " at position (X: " + x +", Y: "+ y + ", Z: " + z + "), got " + this.bda1.get(x, y, z), flag);
				}
			}
		}
		System.out.println("Done!");
	}

}
