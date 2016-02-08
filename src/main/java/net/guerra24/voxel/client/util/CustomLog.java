/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.util;

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
			net.guerra24.voxel.client.util.Logger.error(builder.toString());
			break;
		case LEVEL_WARN:
			net.guerra24.voxel.client.util.Logger.warn(builder.toString());
			break;
		case LEVEL_INFO:
			net.guerra24.voxel.client.util.Logger.log(builder.toString());
			break;
		case LEVEL_DEBUG:
			net.guerra24.voxel.client.util.Logger.error(builder.toString());
			break;
		case LEVEL_TRACE:
			net.guerra24.voxel.client.util.Logger.error(builder.toString());
			break;
		}
	}

}
