package voxel.server.core.glutil;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {

	public static void createWindow(int width, int height, boolean mouseCaptured) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Mouse.setGrabbed(mouseCaptured);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void createWindow(int width, int height, String title,
			boolean mouseCaptured) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			Mouse.setGrabbed(mouseCaptured);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void createWindow(int width, int height, String title,
			boolean mouseCaptured, boolean fullScreen) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Window.setDisplayResolution(width, height, fullScreen);
			Display.setTitle(title);
			Mouse.setGrabbed(mouseCaptured);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void setVSYNC(boolean enabled) {
		Display.setVSyncEnabled(enabled);
	}

	/**
	 * Set the display mode to be used
	 * 
	 * @param width
	 *            The width of the display required
	 * @param height
	 *            The height of the display required
	 * @param fullscreen
	 *            True if we want fullscreen mode
	 */
	public static void setDisplayResolution(int width, int height,
			boolean fullscreen) {

		// return if requested DisplayMode is already set
		if ((Display.getDisplayMode().getWidth() == width)
				&& (Display.getDisplayMode().getHeight() == height)
				&& (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width)
							&& (current.getHeight() == height)) {
						if ((targetDisplayMode == null)
								|| (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null)
									|| (current.getBitsPerPixel() > targetDisplayMode
											.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against
						// the
						// original display mode then it's probably best to go
						// for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display
								.getDesktopDisplayMode().getBitsPerPixel())
								&& (current.getFrequency() == Display
										.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x"
						+ height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height
					+ " fullscreen=" + fullscreen + e);
		}
	}

	public static void update() {
		Display.update();
	}

	public static void update(int targetFPS) {
		Display.update();
		Display.sync(targetFPS);
	}

	public static boolean isWindowCloseRequested() {
		return Display.isCloseRequested();
	}

	public static void dispose() {
		Display.destroy();
	}
}
