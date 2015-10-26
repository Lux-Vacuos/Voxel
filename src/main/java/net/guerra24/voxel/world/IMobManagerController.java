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

import java.util.List;

import net.guerra24.voxel.api.VAPI;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.GuiResources;
import net.guerra24.voxel.world.entities.IEntity;
import net.guerra24.voxel.world.entities.Player;

/**
 * Mob Manager Controller
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 *
 */
public interface IMobManagerController {
	/**
	 * Initialize Data
	 * 
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void init(GameResources gm);

	/**
	 * Update Data
	 * 
	 * @param delta
	 *            Game Delta
	 * @param gm
	 *            Game Resources
	 * @param gi
	 *            Gui Resources
	 * @param world
	 *            Dimensional World
	 * @param api
	 *            Voxel API
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void update(float delta, GameResources gm, GuiResources gi, DimensionalWorld world, VAPI api);

	/**
	 * Dispose Data
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void dispose();

	/**
	 * Register a mob
	 * 
	 * @param mob
	 *            Mob
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void registerMob(IEntity mob);

	/**
	 * Get Mobs
	 * 
	 * @return List of mobs
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public List<IEntity> getMobs();

	/**
	 * Get Player
	 * 
	 * @return Player
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Player getPlayer();
}
