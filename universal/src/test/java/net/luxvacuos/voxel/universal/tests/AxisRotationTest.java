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

package net.luxvacuos.voxel.universal.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.luxvacuos.voxel.universal.world.utils.AxisRotation;
import net.luxvacuos.voxel.universal.world.utils.AxisRotation.*;

public class AxisRotationTest {
	private AxisRotation ar;
	

	@Before
	public void setUp() throws Exception {
		this.ar = new AxisRotation();
	}

	@After
	public void tearDown() throws Exception {
		this.ar = null;
	}

	@Test
	public void test() {
		assertTrue("Freshly made class has data and it shouldn't!", this.ar.getPackedData() == 0);
		
		this.ar.rotate(Direction.CLOCKWISE, Axis.X_AXIS, Rotation.NINETY);
		
		assertTrue("Something fucked up rotating clockwise 90 degrees on X axis!", this.ar.getPackedData() == 0b00000001);
		
		this.ar.rotate(Direction.COUNTERCLOCKWISE, Axis.X_AXIS, Rotation.NINETY);
		
		assertTrue("Something fucked up rotating counterclockwise 90 degrees on X axis!", this.ar.getPackedData() == 0b00000000);
		
		this.ar.rotate(Direction.COUNTERCLOCKWISE, Axis.X_AXIS, Rotation.NINETY);
		
		assertTrue("Something fucked up rotating counterclockwise 90 degrees on X axis!", this.ar.getPackedData() == 0b00000011);
		
		this.ar.rotate(Direction.COUNTERCLOCKWISE, Axis.Y_AXIS, Rotation.NINETY);
		
		assertTrue("Something fucked up rotating counterclockwise 90 degrees on Y axis!", this.ar.getPackedData() == 0b00001111);
		
		this.ar.rotate(Direction.COUNTERCLOCKWISE, Axis.Z_AXIS, Rotation.NINETY);
		
		assertTrue("Something fucked up rotating counterclockwise 90 degrees on Z axis!", this.ar.getPackedData() == 0b00111111);
		
		this.ar.rotate(Direction.CLOCKWISE, Axis.Y_AXIS, Rotation.NINETY);
		
		assertTrue("Something fucked up rotating clockwise 90 degrees on Y axis!", this.ar.getPackedData() == 0b00110011);
	}

}
