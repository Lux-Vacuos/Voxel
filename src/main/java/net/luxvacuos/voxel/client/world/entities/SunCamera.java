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

package net.luxvacuos.voxel.client.world.entities;

import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.Ray;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.util.vector.Matrix4f;
import net.luxvacuos.voxel.universal.util.vector.Vector2f;

public class SunCamera extends Camera {

	private Vector2f center;

	public SunCamera(Matrix4f proj) {
		super(proj);
		center = new Vector2f(2048, 2048);
	}

	public void updateShadowRay(GameResources gm, boolean inverted) {
		if (inverted)
			ray = new Ray(gm.getMasterShadowRenderer().getProjectionMatrix(),
					Maths.createViewMatrixPos(positionComponent.position,
							Maths.createViewMatrixRot(pitch + 180, yaw, roll, null)),
					center, 4096, 4096);
		else
			ray = new Ray(gm.getMasterShadowRenderer().getProjectionMatrix(), Maths.createViewMatrix(this), center,
					4096, 4096);
	}

}
