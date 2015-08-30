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

package io.github.guerra24.voxel.client.kernel.console;

import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * The console thread, handles the Log
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Console extends Thread {
	/**
	 * Console window
	 */
	private static JFrame frame;
	/**
	 * Console text
	 */
	private static JTextArea txtConsole;
	/**
	 * The console has successfully initialized
	 */
	public boolean isReady = false;

	/**
	 * Starts the thread and finally sets the console status
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void run() {
		setPanel();
		Insets insets = frame.getInsets();
		frame.setSize(856 + insets.left + insets.right, 482 + insets.top
				+ insets.bottom);
		isReady = true;
	}

	/**
	 * Initialize the JText and the JFrame
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void setPanel() {
		txtConsole = new JTextArea();
		frame = new JFrame();
		PrintStream out = new PrintStream(new TextAreaOutputStream(txtConsole));
		DefaultCaret caret = (DefaultCaret) txtConsole.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll = new JScrollPane(txtConsole);
		scroll.setViewportView(txtConsole);

		System.setOut(out);
		System.setErr(out);

		txtConsole.setEditable(false);
		txtConsole.setBounds(0, 0, 856, 450);

		frame.add(scroll, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Voxel Log Console");
		currentThread().setName("Console");
		Logger.log(currentThread(), "Starting Console");
	}

	/**
	 * Disposes the JFrame
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void close() {
		frame.dispose();
	}

}