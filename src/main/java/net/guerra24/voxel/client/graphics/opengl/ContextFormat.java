package net.guerra24.voxel.client.graphics.opengl;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFW;

public class ContextFormat {

	private boolean forwardCompat;
	private int profile;
	private int minor;
	private int major;
	private int api;

	/**
	 * <p>
	 * Creates a ContextFormat that stores information of OpenGL Context like
	 * profile, version and API.
	 * </p>
	 * 
	 * @param minor
	 *            Minor version of OpenGL API
	 * 
	 * @param major
	 *            Major version of OpenGL API
	 * 
	 * @param api
	 *            API to use:
	 *            <p>
	 *            Use {@link GLFW#GLFW_OPENGL_API} to create a classic desktop
	 *            context, this provides access to all OpenGL API.
	 *            </p>
	 *            <p>
	 *            Use {@link GLFW#GLFW_OPENGL_ES_API} to create a mobile
	 *            context, this is a sub-set of OpenGL and only provides access
	 *            to a selected set of OpenGL API.
	 *            </p>
	 * 
	 * @param profile
	 *            Profile to use:
	 *            <p>
	 *            Use {@link GLFW#GLFW_OPENGL_ANY_PROFILE} to use any available
	 *            profile.
	 *            </p>
	 *            <p>
	 *            Use {@link GLFW#GLFW_OPENGL_COMPAT_PROFILE} to create a
	 *            profile with access to all OpenGL API, from OpenGL 1.1 to the
	 *            latest version. Warning: Not works on OSX.
	 *            </p>
	 *            <p>
	 *            Use {@link GLFW#GLFW_OPENGL_CORE_PROFILE} to create a profile
	 *            with access to the specified version before. Only available in
	 *            OpenGL 3.2+.
	 *            </p>
	 * 
	 * @param forwardCompat
	 *            <p>
	 *            This enables the use of forward compatibility, the version
	 *            specified before only acts as a minimum required version to
	 *            run the app allowing to use any OpenGL API call to the latest
	 *            version available.
	 *            </p>
	 */
	public ContextFormat(int minor, int major, int api, int profile, boolean forwardCompat) {
		this.minor = minor;
		this.major = major;
		this.api = api;
		this.profile = profile;
		this.forwardCompat = forwardCompat;
	}
	
	public void create() {
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, forwardCompat ? 1 : 0);
		glfwWindowHint(GLFW_OPENGL_PROFILE, profile);
		glfwWindowHint(GLFW_CLIENT_API, api);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, minor);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, major);
	}

}
