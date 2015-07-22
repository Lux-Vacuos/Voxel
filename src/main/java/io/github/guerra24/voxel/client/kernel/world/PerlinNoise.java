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

package io.github.guerra24.voxel.client.kernel.world;

import io.github.guerra24.voxel.client.kernel.Kernel;

import java.util.Random;

public class PerlinNoise {
	private static Random random = Kernel.world.seed;

	public static <T> float[][] GetEmptyArray(int width, int height) {
		float[][] image = new float[width][];

		for (int i = 0; i < width; i++) {
			image[i] = new float[height];
		}
		return image;
	}

	public static float Interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	public static float[][] GenerateWhiteNoise(int width, int height) {
		float[][] noise = GetEmptyArray(width, height);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				noise[i][j] = (float) random.nextDouble() % 1;
			}
		}
		return noise;
	}

	public static float[][] GenerateSmoothNoise(float[][] baseNoise, int octave) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][] smoothNoise = GetEmptyArray(width, height);
		int samplePeriod = 1 << octave;
		float sampleFrequency = 1.0f / samplePeriod;

		for (int i = 0; i < width; i++) {
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width;
			float horizontal_blend = (i - sample_i0) * sampleFrequency;

			for (int j = 0; j < height; j++) {
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height;
				float vertical_blend = (j - sample_j0) * sampleFrequency;

				float top = Interpolate(baseNoise[sample_i0][sample_j0],
						baseNoise[sample_i1][sample_j0], horizontal_blend);

				float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
						baseNoise[sample_i1][sample_j1], horizontal_blend);

				smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}

	public static float[][] GeneratePerlinNoise(float[][] baseNoise,
			int octaveCount) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][][] smoothNoise = new float[octaveCount][][];

		float persistance = 0.45f;

		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
		}
		float[][] perlinNoise = GetEmptyArray(width, height);
		float amplitude = 1.00f;
		@SuppressWarnings("unused")
		float totalAmplitude = 0.0f;
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}

		return perlinNoise;
	}

	public static float[][] GeneratePerlinNoise(int width, int height,
			int octaveCount) {
		float[][] baseNoise = GenerateWhiteNoise(width, height);

		return GeneratePerlinNoise(baseNoise, octaveCount);
	}
}