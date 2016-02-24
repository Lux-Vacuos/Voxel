/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.util;

import static net.luxvacuos.voxel.client.util.Logger.*;

import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemLogger;

public class LoggerSoundSystem extends SoundSystemLogger {

	@Override
	public void message(String message, int indent) {
		String messageText;
		String spacer = "";
		for (int x = 0; x < indent; x++) {
			spacer += "    ";
		}
		messageText = spacer + message;

		log(messageText);
	}

	@Override
	public void importantMessage(String message, int indent) {
		String messageText;
		String spacer = "";
		for (int x = 0; x < indent; x++) {
			spacer += "    ";
		}
		messageText = spacer + message;

		log(messageText);
	}

	@Override
	public boolean errorCheck(boolean error, String classname, String message, int indent) {
		if (error)
			errorMessage(classname, message, indent);
		return error;
	}

	@Override
	public void errorMessage(String classname, String message, int indent) {
		String headerLine, messageText;
		String spacer = "";
		for (int x = 0; x < indent; x++) {
			spacer += "    ";
		}
		headerLine = spacer + "Error in class '" + classname + "'";
		messageText = "    " + spacer + message;
		warn(headerLine);
		warn(messageText);
	}

	@Override
	public void printStackTrace(Exception e, int indent) {
		printExceptionMessage(e, indent);
		importantMessage("STACK TRACE:", indent);
		if (e == null)
			return;

		StackTraceElement[] stack = e.getStackTrace();
		if (stack == null)
			return;

		StackTraceElement line;
		for (int x = 0; x < stack.length; x++) {
			line = stack[x];
			if (line != null)
				message(line.toString(), indent + 1);
		}
	}

	@Override
	public void printExceptionMessage(Exception e, int indent) {
		importantMessage("ERROR MESSAGE:", indent);
		if (e.getMessage() == null)
			message("(none)", indent + 1);
		else
			message(e.getMessage(), indent + 1);
	}
}
