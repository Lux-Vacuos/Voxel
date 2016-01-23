/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
