package net.launcher.core;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.launcher.core.execute.Exec;
import net.launcher.core.logger.Logger;
import net.launcher.core.login.LoginDialog;
import net.launcher.core.properties.Reader;
import net.launcher.core.webpage.LoadingWebPage;

public class Launcher {

	public static void LauncherStart() {
		ImageIcon imgicon = new ImageIcon(Reader.IconPath);
		ImageIcon imgicon1 = new ImageIcon(Reader.BackPath);

		Logger.log(Constants.launcherVersion);

		final JFrame frame = new JFrame("Launcher " + Constants.gameName);
		final JButton btnLogin = new JButton("Login");
		final JButton btnExit = new JButton("Exit");
		final JButton btnWeb = new JButton("News");
		
		Insets insets = frame.getInsets();
		Dimension size = btnLogin.getPreferredSize();
		btnLogin.setBounds(560 + insets.left, 630 + insets.top, size.width,
				size.height);
		size = btnExit.getPreferredSize();
		btnExit.setBounds(640 + insets.left, 630 + insets.top, size.width,
				size.height);
		btnWeb.setBounds(740 + insets.left, 630 + insets.top, 100, size.height);
		Logger.log("Launcher Started");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginDialog loginDlg = new LoginDialog(frame);
				loginDlg.setVisible(true);
				// if logon successfully
				if (loginDlg.isSucceeded()) {
					Logger.log("Starting ");
					frame.dispose();
					try {
						Exec.Main();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.log("Launcher Closed");
				frame.dispose();
				System.exit(0);
			}

		});
		btnWeb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.log("Loading News");
				Logger.log("Loading " + Constants.url);
				LoadingWebPage.main();
			}

		});
		frame.setSize(1280 + insets.left + insets.right, 720 + insets.top
				+ insets.bottom);
		frame.setLayout(null);
		frame.setIconImage(imgicon.getImage());
		frame.setContentPane(new JLabel(new ImageIcon(imgicon1.getImage())));
		frame.getContentPane().add(btnLogin);
		frame.getContentPane().add(btnExit);
		frame.getContentPane().add(btnWeb);
		frame.setVisible(true);
		frame.setLocation(85, 85);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}