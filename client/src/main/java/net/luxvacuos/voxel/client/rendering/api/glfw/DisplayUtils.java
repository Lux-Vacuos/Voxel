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

package net.luxvacuos.voxel.client.rendering.api.glfw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;

public class DisplayUtils {

	private Sync sync;

	public DisplayUtils() {
		String osName = System.getProperty("os.name");

		if (osName.startsWith("Win")) {
			Thread timerAccuracyThread = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(Long.MAX_VALUE);
					} catch (Exception e) {
					}
				}
			});

			timerAccuracyThread.setName("LWJGL Timer");
			timerAccuracyThread.setDaemon(true);
			timerAccuracyThread.start();
		}
		sync = new Sync();
	}

	public ByteBuffer loadIcon(String path) throws IOException {
		InputStream file = getClass().getClassLoader().getResourceAsStream(path);
		PNGDecoder decoder = new PNGDecoder(file);
		ByteBuffer bytebuf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
		decoder.decode(bytebuf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
		bytebuf.flip();
		return bytebuf;
	}

	public void checkErrors() {
	}

	public void sync(int fps) {
		sync.sync(fps);
	}

}
