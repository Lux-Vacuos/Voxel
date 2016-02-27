/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.rendering.api.glfw;

import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memEncodeASCII;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkCreateInstance;
import static org.lwjgl.vulkan.VKUtil.VK_MAKE_VERSION;
import static org.lwjgl.vulkan.VKUtil.translateVulkanResult;

import java.nio.ByteBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil.BufferAllocator;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

public class ContextFormat {

	private static final boolean validation = Boolean.parseBoolean(System.getProperty("vulkan.validation", "false"));

	private static ByteBuffer[] layers = { memEncodeASCII("VK_LAYER_LUNARG_threading", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_LUNARG_mem_tracker", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_LUNARG_object_tracker", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_LUNARG_draw_state", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_LUNARG_param_checker", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_LUNARG_swapchain", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_LUNARG_device_limits", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_LUNARG_image", BufferAllocator.MALLOC),
			memEncodeASCII("VK_LAYER_GOOGLE_unique_objects", BufferAllocator.MALLOC) };

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

	public VkInstance createVulkan(PointerBuffer requiredExtensions) {
		VkApplicationInfo appInfo = VkApplicationInfo.calloc().sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
				.pApplicationName("Voxel").pEngineName("Infinity").apiVersion(VK_MAKE_VERSION(1, 0, 2));
		ByteBuffer VK_EXT_DEBUG_REPORT_EXTENSION = memEncodeASCII(VK_EXT_DEBUG_REPORT_EXTENSION_NAME,
				BufferAllocator.MALLOC);
		PointerBuffer ppEnabledExtensionNames = memAllocPointer(requiredExtensions.remaining() + 1);
		ppEnabledExtensionNames.put(requiredExtensions).put(VK_EXT_DEBUG_REPORT_EXTENSION).flip();

		PointerBuffer ppEnabledLayerNames = memAllocPointer(layers.length);
		for (int i = 0; validation && i < layers.length; i++)
			ppEnabledLayerNames.put(layers[i]);
		ppEnabledLayerNames.flip();

		VkInstanceCreateInfo pCreateInfo = VkInstanceCreateInfo.calloc().sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
				.pNext(NULL).pApplicationInfo(appInfo).ppEnabledExtensionNames(ppEnabledExtensionNames)
				.ppEnabledLayerNames(ppEnabledLayerNames);
		PointerBuffer pInstance = memAllocPointer(1);
		int err = vkCreateInstance(pCreateInfo, null, pInstance);
		long instance = pInstance.get(0);
		memFree(pInstance);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to create VkInstance: " + translateVulkanResult(err));
		}
		VkInstance ret = new VkInstance(instance, pCreateInfo);

		pCreateInfo.free();
		memFree(ppEnabledLayerNames);
		memFree(VK_EXT_DEBUG_REPORT_EXTENSION);
		memFree(ppEnabledExtensionNames);
		appInfo.free();
		return ret;
	}

}
