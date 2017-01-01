/*
 * This file is part of Voxel
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

package net.luxvacuos.voxel.client.util;

import static net.luxvacuos.igl.Logger.*;

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
