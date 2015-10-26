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

package net.guerra24.voxel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Util
 */
public final class Logger {
	private static boolean printTimeStamps;
	private static SimpleDateFormat timeStampFormat;

	static {
		setPrintTimeStamps(true);
		setTimeStampFormat(new SimpleDateFormat("MM/dd/yyyy h:mm:ss a"));
	}

	public static String getTimeStamp() {
		return timeStampFormat.format(new Date());
	}

	/**
	 * Prints to the Log
	 * 
	 * @param thread
	 *            Thread
	 * @param messages
	 *            Message
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void log(Object... messages) {
		for (Object message : messages)
			System.out.println((printTimeStamps
					? "[INFO " + getTimeStamp() + "] " + "[" + Thread.currentThread().getName() + "] " : "") + message);
	}

	/**
	 * Prints a Warning Message
	 * 
	 * @param thread
	 *            Thread
	 * @param messages
	 *            Message
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void warn(Object... messages) {
		for (Object message : messages)
			System.err.println((printTimeStamps
					? "[WARNING " + getTimeStamp() + "] " + "[" + Thread.currentThread().getName() + "] " : "")
					+ message);
	}

	/**
	 * Prints a Fatal Error
	 * 
	 * @param thread
	 *            Thread
	 * @param messages
	 *            Message
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void error(Object... messages) {
		for (Object message : messages) {
			System.err.println((printTimeStamps
					? "[FATAL ERROR " + getTimeStamp() + "] " + "[" + Thread.currentThread().getName() + "] " : "")
					+ message);
		}
	}

	public static void setTimeStampFormat(SimpleDateFormat timeStampFormat) {
		Logger.timeStampFormat = timeStampFormat;
	}

	public static void setPrintTimeStamps(boolean printTimeStamps) {
		Logger.printTimeStamps = printTimeStamps;
	}

}