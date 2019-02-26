/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.client.ecs.entities;

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;

import java.util.ArrayList;
import java.util.List;

import org.joml.Rayf;
import org.joml.Vector3f;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.input.MouseHandler;
import net.luxvacuos.lightengine.client.rendering.glfw.Window;
import net.luxvacuos.lightengine.client.resources.CastRay;
import net.luxvacuos.lightengine.universal.core.GlobalVariables;
import net.luxvacuos.lightengine.universal.core.subsystems.EventSubsystem;
import net.luxvacuos.lightengine.universal.util.IEvent;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.ChunkLoader;
import net.luxvacuos.voxel.universal.ecs.entities.IDimensionEntity;
import net.luxvacuos.voxel.universal.tools.ToolTier;
import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class PlayerCamera extends FreeCamera implements IDimensionEntity {

	private Vector3f blockOutlinePos = new Vector3f();
	private ToolTier tool = ToolTier.ZERO;

	private float breakTime, resetTime;
	private static Vector3 tmp = new Vector3();

	private static List<BoundingBox> blocks = new ArrayList<>();
	private static final int MAX_INTERATION = 64;
	private static final float PRECISION = 16f;
	private Vector3f normalTMP = new Vector3f();
	private double depthTMP;
	private IEvent chunkRadius;

	public PlayerCamera(String name, String uuid) {
		super(name, uuid);
		this.add(new ChunkLoader((int) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/chunkRadius"))));
	}

	@Override
	public void init() {
		super.init();
		castRay = new CastRay(getProjectionMatrix(), getViewMatrix(), center,
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")),
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")));
		chunkRadius = EventSubsystem.addEvent("voxel.world.chunkRadius", () -> {
			Components.CHUNK_LOADER.get(this)
					.setChunkRadius((int) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/chunkRadius")));
		});
	}

	@Override
	public void dispose() {
		super.dispose();
		EventSubsystem.removeEvent("voxel.world.chunkRadius", chunkRadius);
	}

	private void setBlock(IBlock block, IDimension dimension, float delta, MouseHandler mh) {

		if (GlobalVariables.TEST_MODE)
			return;

		Rayf ray = castRay.getRay();
		BoundingBox box = new BoundingBox();
		Vector3f org = new Vector3f(ray.oX, ray.oY, ray.oZ);
		Vector3f dir = new Vector3f();
		Vector3f incr = new Vector3f(ray.dX, ray.dY, ray.dZ);
		incr.div(PRECISION);
		Vector3f pos = new Vector3f();
		int it = 0;
		double bcx = 0, bcy = 0, bcz = 0;
		CAST: while (true) {
			dir.add(incr);
			// Vector3f.add(dir, incr, dir);
			pos.set(org);
			pos.add(dir);
			// Vector3f.add(pos, dir, pos);
			box.set(new Vector3(pos.x - 0.1f, pos.y - 0.1f, pos.z - 0.1f),
					new Vector3(pos.x + 0.1f, pos.y + 0.1f, pos.z + 0.1f));
			blocks = dimension.getGlobalBoundingBox(box);
			for (BoundingBox boundingBox : blocks) {
				if (Maths.intersectRayBounds(ray, boundingBox, tmp)) {
					placeDirection(box.min, box.max, boundingBox.min, boundingBox.max);
					bcx = boundingBox.getCenterX();
					bcy = boundingBox.getCenterY();
					bcz = boundingBox.getCenterZ();
					break CAST;
				}
			}
			it++;
			if (it > MAX_INTERATION)
				break;
		}
		int bx = (int) bcx;
		if (pos.x < 0)
			bx = (int) bcx - 1;

		int bz = (int) bcz;
		if (pos.z < 0)
			bz = (int) bcz - 1;

		int by = (int) bcy;
		if (pos.y < 0)
			by = (int) bcy - 1;
		blockOutlinePos.set(bx + 0.5f, by + 0.5f, bz + 0.5f);
		resetTime += 5f * delta;
		if (resetTime >= 1) {
			if (mh.isButtonPressed(0)) {
				IBlock tBlock = dimension.getBlockAt(bx, by, bz);
				if (ToolTier.isSufficient(tool, tBlock.getToolTier())) {
					breakTime += tool.getMultiplier() / tBlock.getToolTier().getMultiplier() * delta;
					if (breakTime > 1) {
						setBlock(bx, by, bz, Blocks.getBlockByName("air"), dimension);
						resetTime = 0;
						breakTime = 0;
					}
				}
			} else if (mh.isButtonPressed(1)) {
				setBlock(bx, by, bz, block, dimension);
				resetTime = 0;
			} else {
				breakTime = 0;
			}
		}
	}

	private void setBlock(int bx, int by, int bz, IBlock block, IDimension dimension) {
		if (block.getID() != 0) {
			bx -= (int) normalTMP.x;
			by -= (int) normalTMP.y;
			bz -= (int) normalTMP.z;
		}
		dimension.setBlockAt(bx, by, bz, block);
	}

	private boolean placeDirection(final Vector3 mina, final Vector3 maxa, final Vector3 minb, final Vector3 maxb) {
		final Vector3f faces[] = { new Vector3f(-1, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, -1, 0),
				new Vector3f(0, 1, 0), new Vector3f(0, 0, -1), new Vector3f(0, 0, 1) };
		double distances[] = { (maxb.x - mina.x), (maxa.x - minb.x), (maxb.y - mina.y), (maxa.y - minb.y),
				(maxb.z - mina.z), (maxa.z - minb.z) };
		for (int i = 0; i < 6; i++) {
			if (distances[i] < 0.0f)
				return false;
			if ((i == 0) || (distances[i] < depthTMP)) {
				normalTMP = faces[i];
				depthTMP = distances[i];
			}

		}
		return true;
	}

	@Override
	public void updateDim(float delta, IDimension dim) {
		castRay.update(getProjectionMatrix(), getViewMatrix(), center,
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")),
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")));
		Window window = GraphicalSubsystem.getMainWindow();
		setBlock(Blocks.getBlockByName("ice"), dim, delta, window.getMouseHandler());
	}

	public Vector3f getBlockOutlinePos() {
		return blockOutlinePos;
	}

}
