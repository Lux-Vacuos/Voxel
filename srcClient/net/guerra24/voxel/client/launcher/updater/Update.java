package net.guerra24.voxel.client.launcher.updater;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import net.guerra24.voxel.client.kernel.util.Logger;
import net.guerra24.voxel.client.launcher.ConstantsLauncher;
import net.guerra24.voxel.client.launcher.updater.downloader.AssetsDownloader;
import net.guerra24.voxel.client.launcher.updater.downloader.Downloader;

public class Update {
	public static void getUpdate() {
		try {
			Downloader.download(
					ConstantsLauncher.download3,
					ConstantsLauncher.versionpath, false, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Reader readerUnicode = null;
		try {
			readerUnicode = new InputStreamReader(new FileInputStream(
					ConstantsLauncher.versionpath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int e = 0;
		try {
			while ((e = readerUnicode.read()) != -1) {
				char f = (char) e;
				char v = '9';// Local version of the launcher, please use only "ints".
				if (f > v) {
					Logger.log("New update avaiable!");
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static void getUpdateAssets() {
		try {
			Downloader.download(
					ConstantsLauncher.download4,
					ConstantsLauncher.assetspath, false, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Reader readerUnicode = null;
		try {
			readerUnicode = new InputStreamReader(new FileInputStream(
					ConstantsLauncher.assetspath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int e = 1;
		try {
			while ((e = readerUnicode.read()) != -1) {
				char f = (char) e;
				char v = '0';// Local version of the assets please use only "ints".
				if (f > v) {
					Logger.log("Downloading Assets");
					AssetsDownloader.Assets();
					Logger.log("Assets downloaded");
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
