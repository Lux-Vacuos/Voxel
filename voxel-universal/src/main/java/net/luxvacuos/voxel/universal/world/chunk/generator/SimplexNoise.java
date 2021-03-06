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

package net.luxvacuos.voxel.universal.world.chunk.generator;

import java.util.Random;

/**
 *
 * Alternative SimplexNoise Implementation
 *
 */
public class SimplexNoise implements INoiseGenerator {

	SimplexNoise_octave[] octaves;
	double[] frequencys;
	double[] amplitudes;

	int largestFeature;
	double persistence;
	int seed;

	public SimplexNoise(int largestFeature, double persistence, int seed) {
		this.largestFeature = largestFeature;
		this.persistence = persistence;
		this.seed = seed;

		// recieves a number (eg 128) and calculates what power of 2 it is (eg
		// 2^7)
		int numberOfOctaves = (int) Math.ceil(Math.log10(largestFeature) / Math.log10(2));

		octaves = new SimplexNoise_octave[numberOfOctaves];
		frequencys = new double[numberOfOctaves];
		amplitudes = new double[numberOfOctaves];

		Random rnd = new Random(seed);

		for (int i = 0; i < numberOfOctaves; i++) {
			octaves[i] = new SimplexNoise_octave(rnd.nextInt());

			frequencys[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(persistence, octaves.length - i);

		}

	}

	@Override
	public double eval(double x, double y) {

		double result = 0;

		for (int i = 0; i < octaves.length; i++) {
			// double frequency = Math.pow(2,i);
			// double amplitude = Math.pow(persistence,octaves.length-i);

			result = result + octaves[i].noise(x / frequencys[i], y / frequencys[i]) * amplitudes[i];
		}

		return result;

	}

	@Override
	public double eval(double x, double y, double z) {

		double result = 0;

		for (int i = 0; i < octaves.length; i++) {
			double frequency = Math.pow(2, i);
			double amplitude = Math.pow(persistence, octaves.length - i);

			result = result + octaves[i].noise(x / frequency, y / frequency, z / frequency) * amplitude;
		}

		return result;

	}
}