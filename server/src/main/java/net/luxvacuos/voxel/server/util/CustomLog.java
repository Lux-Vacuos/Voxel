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

package net.luxvacuos.voxel.server.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.esotericsoftware.minlog.Log.*;
import com.esotericsoftware.minlog.Log.Logger;

public class CustomLog extends Logger {

	private static CustomLog instance;

	public static CustomLog getInstance() {
		if (instance == null)
			instance = new CustomLog();
		return instance;
	}

	private CustomLog() {
		setLogger(this);
	}

	@Override
	public void log(int level, String category, String message, Throwable ex) {
		StringBuilder builder = new StringBuilder(256);

		if (category != null) {
			builder.append('[');
			builder.append(category);
			builder.append("] ");
		}

		builder.append(message);

		if (ex != null) {
			StringWriter writer = new StringWriter(256);
			ex.printStackTrace(new PrintWriter(writer));
			builder.append('\n');
			builder.append(writer.toString().trim());
		}

		switch (level) {
		case LEVEL_ERROR:
			net.luxvacuos.voxel.server.util.Logger.error(builder.toString());
			break;
		case LEVEL_WARN:
			net.luxvacuos.voxel.server.util.Logger.warn(builder.toString());
			break;
		case LEVEL_INFO:
			net.luxvacuos.voxel.server.util.Logger.log(builder.toString());
			break;
		case LEVEL_DEBUG:
			net.luxvacuos.voxel.server.util.Logger.error(builder.toString());
			break;
		case LEVEL_TRACE:
			net.luxvacuos.voxel.server.util.Logger.error(builder.toString());
			break;
		}
	}

}
