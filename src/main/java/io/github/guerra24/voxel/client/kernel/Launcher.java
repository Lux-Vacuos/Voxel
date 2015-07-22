package io.github.guerra24.voxel.client.kernel;

import java.io.File;

import io.github.guerra24.voxel.client.kernel.console.Console;
import io.github.guerra24.voxel.client.kernel.menu.ConfigGUI;

public class Launcher {

	private static Platform platform;
	public static Console thread1;
	public static ConfigGUI config;

	public static void run() {
		thread1 = new Console();
		thread1.start();

		while (!thread1.isReady) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Thread.currentThread().setName("Voxel Main");
		config = new ConfigGUI();
		while (!config.ready) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		new Kernel();
	}

	public static Platform getPlatform() {
		if (platform == null) {
			final String OS = System.getProperty("os.name").toLowerCase();
			final String ARCH = System.getProperty("os.arch").toLowerCase();

			boolean isWindows = OS.contains("windows");
			boolean isLinux = OS.contains("linux");
			boolean isMac = OS.contains("mac");
			boolean is64Bit = ARCH.equals("amd64") || ARCH.equals("x86_64");

			platform = Platform.UNKNOWN;

			if (isWindows)
				platform = is64Bit ? Platform.WINDOWS_64 : Platform.WINDOWS_32;
			if (isLinux)
				platform = is64Bit ? Platform.LINUX_64 : Platform.LINUX_32;
			if (isMac)
				platform = Platform.MACOSX;
		}

		return platform;
	}

	public enum Platform {
		WINDOWS_32, WINDOWS_64, MACOSX, LINUX_32, LINUX_64, UNKNOWN;

	}

	public static void main(String[] args) {
		if (KernelConstants.postPro) {
			System.setProperty("org.lwjgl.librarypath",
					new File("natives").getAbsolutePath());
		}
		run();
	}

}
