package org.newdawn.slick.opengl.renderer;

/**
 * The static holder for the current GL implementation. Note that this renderer
 * can only be set before the game has been started.
 * 
 * @author kevin
 */
public class Renderer {
	private static SGL renderer = new ImmediateModeOGLRenderer();

	/**
	 * Get the renderer to be used when accessing GL
	 * 
	 * @return The renderer to be used when accessing GL
	 */
	public static SGL get() {
		return renderer;
	}


}
