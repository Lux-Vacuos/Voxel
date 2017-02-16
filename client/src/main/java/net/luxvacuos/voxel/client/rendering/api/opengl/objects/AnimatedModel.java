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

package net.luxvacuos.voxel.client.rendering.api.opengl.objects;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.voxel.client.resources.animation.Joint;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public class AnimatedModel implements IDisposable {

	private final VAO model;
	private final Material material;

	private final Joint rootJoint;
	private final int jointCount;

	public AnimatedModel(VAO model, Material material, Joint rootJoint, int jointCount) {
		this.model = model;
		this.material = material;
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;
		rootJoint.calcInverseBindTransform(new Matrix4d());
	}

	public void doAnimation() {

	}

	public void update() {

	}

	public Matrix4d[] getJointTransform() {
		Matrix4d[] jointMatrices = new Matrix4d[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}

	private void addJointsToArray(Joint headJoint, Matrix4d[] jointMatrices) {
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();
		for (Joint childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}

	@Override
	public void dispose() {
		model.dispose();
	}

	public VAO getModel() {
		return model;
	}

	public Material getMaterial() {
		return material;
	}

}
