package io.github.guerra24.voxel.client.kernel.console;

import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends Thread {

	private static JFrame frame;
	private static JTextArea txtConsole;

	public void setPanel() {
		txtConsole = new JTextArea();
		frame = new JFrame();
		PrintStream out = new PrintStream(new TextAreaOutputStream(txtConsole));
		JScrollPane scroll = new JScrollPane(txtConsole);
		scroll.setViewportView(txtConsole);

		System.setOut(out);
		System.setErr(out);

		txtConsole.setEditable(false);
		txtConsole.setBounds(0, 0, 856, 450);

		frame.add(scroll, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		Logger.log(currentThread(), "Starting Console");
	}

	public void close() {
		frame.dispose();
	}

	public void run() {
		setPanel();
		Insets insets = frame.getInsets();
		frame.setSize(856 + insets.left + insets.right, 482 + insets.top
				+ insets.bottom);
	}
}