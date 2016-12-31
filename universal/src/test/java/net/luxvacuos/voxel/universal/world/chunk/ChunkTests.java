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

package net.luxvacuos.voxel.universal.world.chunk;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.luxvacuos.voxel.universal.tests.util.TestBlock;
import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class ChunkTests {
	private ChunkData chunkData;
	private Chunk chunk;
	private ChunkNode node;
	private Random random;
	private TestBlock testBlock;

	@Before
	public void setUp() throws Exception {
		this.random = new Random();
		this.node = new ChunkNode(0,0,0);
		this.chunkData = new ChunkData();
		this.chunk = new Chunk(null, this.node, this.chunkData);
		this.testBlock = new TestBlock(this.random.nextInt());
		Blocks.startRegister("testing");
		Blocks.register(this.testBlock);
		Blocks.finishRegister();
	}
	
	@Test
	public void chunkBlockLookupTest() {
		System.out.println("Testing Block lookup in Chunk... ");
		boolean flag;
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				for(int y = 0; y < 256; y++) {
					this.chunk.setBlockAt(x, y, z, this.testBlock);
					this.chunk.getChunkData().rebuild();
					flag = (this.chunk.getBlockAt(x, y, z).getID() == this.testBlock.getID());
					
					assertTrue("Block ID mismatch! Got "+ this.chunk.getBlockAt(x, y, z).getID()+" at "
					+x+", "+y+", "+z+", expected "+this.testBlock.getID(), flag);
				}
			}
		}
		System.out.println("Done!");
	}

	@After
	public void tearDown() throws Exception {
	}

}
