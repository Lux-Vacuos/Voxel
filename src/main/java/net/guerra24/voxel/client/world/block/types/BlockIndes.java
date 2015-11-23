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

package net.guerra24.voxel.client.world.block.types;

import net.guerra24.voxel.client.resources.models.WaterTile;
import net.guerra24.voxel.client.world.block.BlockEntity;
import net.guerra24.voxel.client.world.block.BlocksResources;
import net.guerra24.voxel.client.world.block.IBlock;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class BlockIndes extends IBlock {

	@Override
	public byte getId() {
		return -1;
	}

	@Override
	public BlockEntity getFaceUp(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeIndesUP, pos, 0, 0, 0, 1, "UP", getId());
	}

	@Override
	public BlockEntity getFaceDown(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeIndesDOWN, pos, 0, 0, 0, 1, "DOWN", getId());
	}

	@Override
	public BlockEntity getFaceEast(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeIndesEAST, pos, 0, 0, 0, 1, "EAST", getId());
	}

	@Override
	public BlockEntity getFaceWest(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeIndesWEST, pos, 0, 0, 0, 1, "WEST", getId());
	}

	@Override
	public BlockEntity getFaceNorth(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeIndesNORTH, pos, 0, 0, 0, 1, "NORTH", getId());
	}

	@Override
	public BlockEntity getFaceSouth(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeIndesSOUTH, pos, 0, 0, 0, 1, "SOUTH", getId());
	}

	@Override
	public WaterTile getWaterTitle(Vector3f pos) {
		return null;
	}

	@Override
	public BlockEntity getSingleModel(Vector3f pos) {
		return null;
	}

	@Override
	public boolean usesSingleModel() {
		return false;
	}

}
