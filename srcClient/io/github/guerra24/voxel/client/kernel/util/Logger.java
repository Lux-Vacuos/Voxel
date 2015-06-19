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

package io.github.guerra24.voxel.client.kernel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {
	private static boolean printTimeStamps;
	private static SimpleDateFormat timeStampFormat;

	private Logger() {
	}

	static {
		setPrintTimeStamps(true);
		setTimeStampFormat(new SimpleDateFormat("MM/dd/yyyy h:mm:ss a"));
	}

	public static void log(Thread thread, Object... messages) {
		for (Object message : messages)
			System.out.println((printTimeStamps ? "[INFO " + getTimeStamp()
					+ "] " + "[" + thread.getName() + "] " : "")
					+ message);
	}

	public static void logS(Object... messages) {
		for (Object message : messages)
			System.out.println((printTimeStamps ? "[CLIENT " + getTimeStamp()
					+ "] " : "")
					+ message);
	}

	public static String getTimeStamp() {
		return timeStampFormat.format(new Date());
	}

	public static void warn(Thread thread, Object... messages) {
		for (Object message : messages)
			System.err.println((printTimeStamps ? "[WARNING " + getTimeStamp()
					+ "] " + "[" + thread.getName() + "]" : "")
					+ message);
	}

	public static void error(Thread thread, Object... messages) {
		for (Object message : messages)
			System.err
					.println((printTimeStamps ? "[FATAL ERROR "
							+ getTimeStamp() + "] " + "[" + thread.getName()
							+ "]" : "")
							+ message);
	}

	public static void setTimeStampFormat(SimpleDateFormat timeStampFormat) {
		Logger.timeStampFormat = timeStampFormat;
	}

	public static void setPrintTimeStamps(boolean printTimeStamps) {
		Logger.printTimeStamps = printTimeStamps;
	}
}