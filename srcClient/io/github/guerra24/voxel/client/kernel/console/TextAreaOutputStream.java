package io.github.guerra24.voxel.client.kernel.console;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
	private JTextArea textControl;

	public TextAreaOutputStream(JTextArea control) {
		textControl = control;
	}

	public void write(int b) throws IOException {
		textControl.append(String.valueOf((char) b));
	}
}