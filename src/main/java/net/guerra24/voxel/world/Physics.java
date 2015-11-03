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

package net.guerra24.voxel.world;

import net.guerra24.voxel.resources.GameResources;

/**
 * Physics
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public class Physics {
	/**
	 * Physics Data
	 */
	private MobManager mobManager;

	/**
	 * Constructor
	 * 
	 * @param gm
	 *            Game Resources
	 */
	public Physics(GameResources gm) {
		mobManager = new VoxelMobManager(gm);
	}
	
	public void dispose(){
		mobManager.cleanUp();
	}

	/**
	 * Default Mob Manager
	 * 
	 * @return Mob Manager
	 */
	public MobManager getMobManager() {
		return mobManager;
	}

	/**
	 * Set Mob Manager
	 * 
	 * @param mobManager
	 *            Mob Manager
	 */
	public void setMobManager(MobManager mobManager) {
		this.mobManager = mobManager;
	}
}
