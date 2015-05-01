package net.guerra24.voxel.client.engine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class Loading extends JWindow {
	private static final long serialVersionUID = -7798756340668169121L;

	public Loading() {
		showSplash();
	}

	private void showSplash() {
		JPanel content = (JPanel) getContentPane();
		content.setBackground(Color.white);
		int width = 874;
		int height = 500;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);
		JLabel label = new JLabel(new ImageIcon(
				"assets/textures/menu/Splash.png"));
		JLabel load = new JLabel("Voxel Game", JLabel.CENTER);
		load.setFont(new Font("Sans-Serif", Font.BOLD, 12));
		content.add(label, BorderLayout.CENTER);
		content.add(load, BorderLayout.SOUTH);
		Color oraRed = new Color(100, 100, 100, 255);
		content.setBorder(BorderFactory.createLineBorder(oraRed, 10));
		setVisible(true);
	}
}