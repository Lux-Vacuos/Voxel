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

package io.github.guerra24.voxel.client.kernel.world.block.types;

import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;

import org.lwjgl.util.vector.Vector3f;

public class BlockSand extends Block {

	@Override
	public byte getId() {
		return 3;
	}

	@Override
	public Entity getFaceUp(Vector3f pos) {
		return new Entity(BlocksResources.cubeSandUP, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceDown(Vector3f pos) {
		return new Entity(BlocksResources.cubeSandDOWN, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceEast(Vector3f pos) {
		return new Entity(BlocksResources.cubeSandEAST, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceWest(Vector3f pos) {
		return new Entity(BlocksResources.cubeSandWEST, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceNorth(Vector3f pos) {
		return new Entity(BlocksResources.cubeSandNORTH, pos, 0, 0, 0, 1);
	}

	@Override
	public Entity getFaceSouth(Vector3f pos) {
		return new Entity(BlocksResources.cubeSandSOUTH, pos, 0, 0, 0, 1);
	}

	@Override
	public WaterTile getWaterTitle(Vector3f pos) {
		return null;
	}

	@Override
	public Entity getSingleModel(Vector3f pos) {
		return null;
	}

}
