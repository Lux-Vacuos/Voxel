/*
 * This file is part of Infinity Game Library
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

package net.luxvacuos.igl;

import static com.esotericsoftware.minlog.Log.LEVEL_DEBUG;
import static com.esotericsoftware.minlog.Log.LEVEL_ERROR;
import static com.esotericsoftware.minlog.Log.LEVEL_INFO;
import static com.esotericsoftware.minlog.Log.LEVEL_TRACE;
import static com.esotericsoftware.minlog.Log.LEVEL_WARN;
import static com.esotericsoftware.minlog.Log.setLogger;

import java.io.PrintWriter;
import java.io.StringWriter;

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
			net.luxvacuos.igl.Logger.error(builder.toString());
			break;
		case LEVEL_WARN:
			net.luxvacuos.igl.Logger.warn(builder.toString());
			break;
		case LEVEL_INFO:
			net.luxvacuos.igl.Logger.log(builder.toString());
			break;
		case LEVEL_DEBUG:
			net.luxvacuos.igl.Logger.error(builder.toString());
			break;
		case LEVEL_TRACE:
			net.luxvacuos.igl.Logger.error(builder.toString());
			break;
		}
	}

}
