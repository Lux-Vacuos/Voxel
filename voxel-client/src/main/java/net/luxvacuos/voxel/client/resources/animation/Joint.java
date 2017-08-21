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

package net.luxvacuos.voxel.client.resources.animation;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.vector.Matrix4d;

public class Joint {

	public final int index;
	public final String name;
	public final List<Joint> children = new ArrayList<>();

	private Matrix4d animatedTransform = new Matrix4d();

	private final Matrix4d localBindTransform;
	private Matrix4d inverseBindTransform = new Matrix4d();

	public Joint(int index, String name, Matrix4d localBindTransform) {
		this.index = index;
		this.name = name;
		this.localBindTransform = localBindTransform;
	}

	public void calcInverseBindTransform(Matrix4d parentBindTransform) {
		Matrix4d bindTransform = Matrix4d.mul(parentBindTransform, localBindTransform, null);
		Matrix4d.invert(bindTransform, inverseBindTransform);
		for (Joint joint : children) {
			joint.calcInverseBindTransform(bindTransform);
		}
	}

	public void addChild(Joint child) {
		this.children.add(child);
	}

	public Matrix4d getAnimatedTransform() {
		return animatedTransform;
	}

	public void setAnimationTransform(Matrix4d animationTransform) {
		this.animatedTransform = animationTransform;
	}

	public Matrix4d getInverseBindTransform() {
		return inverseBindTransform;
	}

}
