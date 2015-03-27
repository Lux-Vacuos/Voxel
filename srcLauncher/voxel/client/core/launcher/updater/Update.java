package voxel.client.core.launcher.updater;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import voxel.client.core.launcher.Constants;
import voxel.client.core.launcher.updater.downloader.AssetsDownloader;
import voxel.client.core.launcher.updater.downloader.Downloader;
import voxel.client.core.util.Logger;

public class Update {
	public static void getUpdate() {
		try {
			Downloader.download(
					Constants.download3,
					Constants.versionpath, false, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Reader readerUnicode = null;
		try {
			readerUnicode = new InputStreamReader(new FileInputStream(
					Constants.versionpath));
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
					Constants.download4,
					Constants.assetspath, false, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Reader readerUnicode = null;
		try {
			readerUnicode = new InputStreamReader(new FileInputStream(
					Constants.assetspath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int e = 0;
		try {
			while ((e = readerUnicode.read()) != -1) {
				char f = (char) e;
				char v = '1';// Local version of the assets please use only "ints".
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
