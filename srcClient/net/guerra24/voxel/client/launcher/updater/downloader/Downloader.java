package net.guerra24.voxel.client.launcher.updater.downloader;

import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import net.guerra24.voxel.client.kernel.util.Logger;

public class Downloader extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void download(String a1, String a2, boolean showUI,
			boolean exit) throws Exception {
		Logger.log("Starting download");
		String site = a1;
		String filename = a2;
		JFrame frame = new JFrame("Download Progress");
		JProgressBar current = new JProgressBar(0, 100);
		JProgressBar DownloadProg = new JProgressBar(0, 100);
		JLabel downloadSize = new JLabel();
		current.setSize(50, 50);
		current.setValue(0);
		current.setStringPainted(true);
		frame.add(downloadSize);
		frame.setLocation(100, 100);
		frame.add(current);
		frame.add(DownloadProg);
		frame.setVisible(showUI);
		frame.setLayout(new GridLayout(1, 3, 5, 5));
		frame.pack();
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
		try {
			URL url = new URL(site);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			int filesize = connection.getContentLength();
			float totalDataRead = 0.0F;
			BufferedInputStream in = new BufferedInputStream(
					connection.getInputStream());
			FileOutputStream fos = new FileOutputStream(filename);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			int i = 0;
			while ((i = in.read(data, 0, 1024)) >= 0) {
				totalDataRead += i;
				float prog = 100.0F - totalDataRead * 100.0F / filesize;
				DownloadProg.setValue((int) prog);
				bout.write(data, 0, i);
				float Percent = totalDataRead * 100.0F / filesize;
				current.setValue((int) Percent);
				double kbSize = filesize / 1000;

				String unit = "kb";
				double Size;
				if (kbSize > 999.0D) {
					Size = kbSize / 1000.0D;
					unit = "mb";
				} else {
					Size = kbSize;
				}
				downloadSize.setText("Filesize: " + Double.toString(Size)
						+ unit);
			}
			bout.close();
			in.close();
			Logger.log("File downloaded");
			frame.dispose();
		} catch (Exception e) {
			Logger.log("Download failed");
			JOptionPane.showConfirmDialog(null, e.getMessage(),
					"No internet conection", -1);
		}
	}
}