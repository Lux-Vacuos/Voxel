package net.guerra24.voxel.client.util;

import net.guerra24.voxel.client.sound.soundsystem.SoundSystemLogger;

import static net.guerra24.voxel.client.util.Logger.*;

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
