/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.rendering.api.glfw;

import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_is_hdr_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memUTF8;
import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_ERROR_BIT_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_WARNING_BIT_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME;
import static org.lwjgl.vulkan.EXTDebugReport.VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.vkCreateDebugReportCallbackEXT;
import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.VK_MAKE_VERSION;
import static org.lwjgl.vulkan.VK10.VK_QUEUE_GRAPHICS_BIT;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkCreateDevice;
import static org.lwjgl.vulkan.VK10.vkCreateInstance;
import static org.lwjgl.vulkan.VK10.vkEnumeratePhysicalDevices;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceQueueFamilyProperties;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.NVXGPUMemoryInfo;
import org.lwjgl.opengl.WGLAMDGPUAssociation;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;
import org.lwjgl.vulkan.VkDebugReportCallbackEXT;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import com.badlogic.gdx.utils.Array;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.exception.DecodeTextureException;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.vulkan.VKUtil;
import net.luxvacuos.voxel.client.resources.ResourceLoader;

public final class WindowManager {

	private static Array<Window> windows = new Array<>();

	private static final boolean validation = Boolean.parseBoolean(System.getProperty("vulkan.validation", "false"));

	private static ByteBuffer[] layers = { memUTF8("VK_LAYER_LUNARG_standard_validation"), };

	private WindowManager() {
	}

	public static WindowHandle generateHandle(int width, int height, String title) {
		return new WindowHandle(width, height, title);
	}

	public static long createWindow(WindowHandle handle, boolean vsync) {
		Logger.log("Creating new Window '" + handle.title + "'");
		// TODO: Move Vulkan Stuff
		VkInstance instance = null;
		DeviceAndGraphicsQueueFamily deviceAndGraphicsQueueFamily = null;
		if (glfwVulkanSupported()) {
			Logger.log("Enabling Vulkan API");
			PointerBuffer requiredExtensions = glfwGetRequiredInstanceExtensions();
			if (requiredExtensions == null) {
				throw new AssertionError("Failed to find list of required Vulkan extensions");
			}

			instance = createInstance(requiredExtensions);
			VkDebugReportCallbackEXT debugCallback = new VkDebugReportCallbackEXT() {
				public int invoke(int flags, int objectType, long object, long location, int messageCode,
						long pLayerPrefix, long pMessage, long pUserData) {
					Logger.error("ERROR OCCURED: " + VkDebugReportCallbackEXT.getString(pMessage));
					return 0;
				}
			};
			long debugCallbackHandle = setupDebugging(instance,
					VK_DEBUG_REPORT_ERROR_BIT_EXT | VK_DEBUG_REPORT_WARNING_BIT_EXT, debugCallback);
			VkPhysicalDevice physicalDevice = getFirstPhysicalDevice(instance);
			deviceAndGraphicsQueueFamily = createDeviceAndGetGraphicsQueueFamily(physicalDevice);
			CoreInfo.VkVersion = "1.0.30";
		}

		long windowID = GLFW.glfwCreateWindow(handle.width, handle.height, handle.title, NULL, NULL);
		if (windowID == NULL) {
			throw new RuntimeException("Failed to create GLFW Window '" + handle.title + "'");
		}

		Window window = new Window(instance, deviceAndGraphicsQueueFamily, windowID, handle.width, handle.height);

		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(windowID, (vidmode.width() - window.width) / 2, (vidmode.height() - window.height) / 2);
		GLFW.glfwMakeContextCurrent(windowID);
		GLFW.glfwSwapInterval(vsync ? 1 : 0);

		if (handle.icons.size != 0) {
			GLFWImage.Buffer iconsbuff = GLFWImage.malloc(handle.icons.size);
			int i = 0;
			for (String path : handle.icons) {
				ByteBuffer imageBuffer;
				ByteBuffer image;
				try {
					imageBuffer = ResourceLoader.ioResourceToByteBuffer(path, 8 * 1024);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				IntBuffer w = BufferUtils.createIntBuffer(1);
				IntBuffer h = BufferUtils.createIntBuffer(1);
				IntBuffer comp = BufferUtils.createIntBuffer(1);

				if (!stbi_info_from_memory(imageBuffer, w, h, comp))
					throw new DecodeTextureException("Failed to read image information: " + stbi_failure_reason());

				Logger.log("Image width: " + w.get(0), "Image height: " + h.get(0), "Image components: " + comp.get(0),
						"Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

				image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
				if (image == null)
					throw new DecodeTextureException("Failed to load image: " + stbi_failure_reason());

				image.flip();
				iconsbuff.position(i).width(w.get(0)).height(h.get(0)).pixels(image);
				i++;
			}
			iconsbuff.position(0);
			GLFW.glfwSetWindowIcon(windowID, iconsbuff);
			iconsbuff.free();
		}

		boolean forwardCompat = GLFW.glfwGetWindowAttrib(windowID, GLFW.GLFW_OPENGL_FORWARD_COMPAT) == GLFW.GLFW_TRUE;
		window.capabilities = GL.createCapabilities(forwardCompat);

		int nvgFlags = NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES;
		if (ClientVariables.debug)
			nvgFlags = (nvgFlags | NanoVGGL3.NVG_DEBUG);
		window.nvgID = NanoVGGL3.nvgCreate(nvgFlags);

		if (window.nvgID == NULL)
			throw new RuntimeException("Fail to create NanoVG context for Window '" + handle.title + "'");

		window.lastLoopTime = getTime();

		int[] h = new int[1];
		int[] w = new int[1];

		GLFW.glfwGetFramebufferSize(windowID, w, h);
		window.framebufferHeight = h[0];
		window.framebufferWidth = w[0];
		GLFW.glfwGetWindowSize(windowID, w, h);
		window.height = h[0];
		window.width = w[0];
		window.pixelRatio = (float) window.framebufferWidth / (float) window.width;
		window.resetViewport();

		window.created = true;

		windows.add(window);

		return windowID;
	}

	private static VkInstance createInstance(PointerBuffer requiredExtensions) {
		VkApplicationInfo appInfo = VkApplicationInfo.calloc().sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
				.pApplicationName(memUTF8("Voxel")).pEngineName(memUTF8("")).apiVersion(VK_MAKE_VERSION(1, 0, 30));

		ByteBuffer VK_EXT_DEBUG_REPORT_EXTENSION = memUTF8(VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
		PointerBuffer ppEnabledExtensionNames = memAllocPointer(requiredExtensions.remaining() + 1);
		ppEnabledExtensionNames.put(requiredExtensions)

				.put(VK_EXT_DEBUG_REPORT_EXTENSION).flip();

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
			throw new AssertionError("Failed to create VkInstance: " + VKUtil.translateVulkanResult(err));
		}
		VkInstance ret = new VkInstance(instance, pCreateInfo);

		pCreateInfo.free();
		memFree(ppEnabledLayerNames);
		memFree(VK_EXT_DEBUG_REPORT_EXTENSION);
		memFree(ppEnabledExtensionNames);
		memFree(appInfo.pApplicationName());
		memFree(appInfo.pEngineName());
		appInfo.free();
		return ret;
	}

	private static long setupDebugging(VkInstance instance, int flags, VkDebugReportCallbackEXT callback) {
		VkDebugReportCallbackCreateInfoEXT dbgCreateInfo = VkDebugReportCallbackCreateInfoEXT.calloc()
				.sType(VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT).pNext(NULL).pfnCallback(callback)
				.pUserData(NULL).flags(flags);
		LongBuffer pCallback = memAllocLong(1);
		int err = vkCreateDebugReportCallbackEXT(instance, dbgCreateInfo, null, pCallback);
		long callbackHandle = pCallback.get(0);
		memFree(pCallback);
		dbgCreateInfo.free();
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to create VkInstance: " + VKUtil.translateVulkanResult(err));
		}
		return callbackHandle;
	}

	private static VkPhysicalDevice getFirstPhysicalDevice(VkInstance instance) {
		IntBuffer pPhysicalDeviceCount = memAllocInt(1);
		int err = vkEnumeratePhysicalDevices(instance, pPhysicalDeviceCount, null);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to get number of physical devices: " + VKUtil.translateVulkanResult(err));
		}
		PointerBuffer pPhysicalDevices = memAllocPointer(pPhysicalDeviceCount.get(0));
		err = vkEnumeratePhysicalDevices(instance, pPhysicalDeviceCount, pPhysicalDevices);
		long physicalDevice = pPhysicalDevices.get(0);
		memFree(pPhysicalDeviceCount);
		memFree(pPhysicalDevices);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to get physical devices: " + VKUtil.translateVulkanResult(err));
		}
		return new VkPhysicalDevice(physicalDevice, instance);
	}

	public static class DeviceAndGraphicsQueueFamily {
		VkDevice device;
		int queueFamilyIndex;
	}

	private static DeviceAndGraphicsQueueFamily createDeviceAndGetGraphicsQueueFamily(VkPhysicalDevice physicalDevice) {
		IntBuffer pQueueFamilyPropertyCount = memAllocInt(1);
		vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, pQueueFamilyPropertyCount, null);
		int queueCount = pQueueFamilyPropertyCount.get(0);
		VkQueueFamilyProperties.Buffer queueProps = VkQueueFamilyProperties.calloc(queueCount);
		vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, pQueueFamilyPropertyCount, queueProps);
		memFree(pQueueFamilyPropertyCount);
		int graphicsQueueFamilyIndex;
		for (graphicsQueueFamilyIndex = 0; graphicsQueueFamilyIndex < queueCount; graphicsQueueFamilyIndex++) {
			if ((queueProps.get(graphicsQueueFamilyIndex).queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0)
				break;
		}
		queueProps.free();
		FloatBuffer pQueuePriorities = memAllocFloat(1).put(0.0f);
		pQueuePriorities.flip();
		VkDeviceQueueCreateInfo.Buffer queueCreateInfo = VkDeviceQueueCreateInfo.calloc(1)
				.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO).queueFamilyIndex(graphicsQueueFamilyIndex)
				.pQueuePriorities(pQueuePriorities);

		PointerBuffer extensions = memAllocPointer(1);
		ByteBuffer VK_KHR_SWAPCHAIN_EXTENSION = memUTF8(VK_KHR_SWAPCHAIN_EXTENSION_NAME);
		extensions.put(VK_KHR_SWAPCHAIN_EXTENSION);
		extensions.flip();
		PointerBuffer ppEnabledLayerNames = memAllocPointer(layers.length);
		for (int i = 0; validation && i < layers.length; i++)
			ppEnabledLayerNames.put(layers[i]);
		ppEnabledLayerNames.flip();

		VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.calloc().sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
				.pNext(NULL).pQueueCreateInfos(queueCreateInfo).ppEnabledExtensionNames(extensions)
				.ppEnabledLayerNames(ppEnabledLayerNames);

		PointerBuffer pDevice = memAllocPointer(1);
		int err = vkCreateDevice(physicalDevice, deviceCreateInfo, null, pDevice);
		long device = pDevice.get(0);
		memFree(pDevice);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to create device: " + VKUtil.translateVulkanResult(err));
		}

		DeviceAndGraphicsQueueFamily ret = new DeviceAndGraphicsQueueFamily();
		ret.device = new VkDevice(device, physicalDevice, deviceCreateInfo);
		ret.queueFamilyIndex = graphicsQueueFamilyIndex;

		deviceCreateInfo.free();
		memFree(ppEnabledLayerNames);
		memFree(VK_KHR_SWAPCHAIN_EXTENSION);
		memFree(extensions);
		memFree(pQueuePriorities);
		return ret;
	}

	public static Window getWindow(long windowID) {
		for (Window window : windows) {
			if (window.windowID == windowID) {
				int index = windows.indexOf(window, true);
				if (index != 0)
					windows.swap(0, index); // Swap the window to the front of
											// the array to speed up future
											// recurring searches
				if (GLFW.glfwGetCurrentContext() != windowID)
					GLFW.glfwMakeContextCurrent(windowID);
				return window;
			}

		}

		return null;
	}

	public static void closeAllDisplays() {
		for (Window window : windows) {
			window.closeDisplay();
			window.dispose();
		}
	}

	public static double getTime() {
		return GLFW.glfwGetTime();
	}

	public static long getNanoTime() {
		return (long) (getTime() * (1000L * 1000L * 1000L));
	}

	protected static GLFWCursorEnterCallback cursorEnterCallback;
	protected static GLFWCursorPosCallback cursorPosCallback;
	protected static GLFWMouseButtonCallback mouseButtonCallback;
	protected static GLFWWindowFocusCallback windowFocusCallback;
	protected static GLFWWindowSizeCallback windowSizeCallback;
	protected static GLFWWindowPosCallback windowPosCallback;
	protected static GLFWWindowRefreshCallback windowRefreshCallback;
	protected static GLFWFramebufferSizeCallback framebufferSizeCallback;
	protected static GLFWScrollCallback scrollCallback;
	protected static GLFWWindowRefreshCallback refreshCallback;

	private static IntBuffer maxVram = BufferUtils.createIntBuffer(1);
	private static IntBuffer usedVram = BufferUtils.createIntBuffer(1);
	private static boolean nvidia = false;
	private static boolean amd = false;
	private static boolean detected = false;

	static {
		cursorEnterCallback = new GLFWCursorEnterCallback() {
			@Override
			public void invoke(long windowID, boolean entered) {
				Mouse.setMouseInsideWindow(entered);
			}
		};

		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long windowID, double xpos, double ypos) {
				Mouse.addMoveEvent(xpos, ypos);
			}
		};

		mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long windowID, int button, int action, int mods) {
				Mouse.addButtonEvent(button, action == GLFW.GLFW_PRESS ? true : false);
			}
		};

		scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				Mouse.addWheelEvent((int) yoffset);
			}
		};

		windowFocusCallback = new GLFWWindowFocusCallback() {
			@Override
			public void invoke(long windowID, boolean focused) {
				if (focused && getWindow(windowID) != null && !getWindow(windowID).isWindowFocused())
					GLFW.glfwFocusWindow(windowID);
			}
		};

		windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long windowID, int width, int height) {
				Window window = getWindow(windowID);
				if (window == null)
					return;

				IntBuffer w = BufferUtils.createIntBuffer(1);
				IntBuffer h = BufferUtils.createIntBuffer(1);
				glfwGetFramebufferSize(windowID, w, h);
				window.framebufferWidth = w.get(0);
				window.framebufferHeight = h.get(0);

				glfwGetWindowSize(windowID, w, h);
				window.width = w.get(0);
				window.height = h.get(0);
				window.pixelRatio = (float) window.framebufferWidth / (float) window.width;
				window.resetViewport();
			}
		};

		windowPosCallback = new GLFWWindowPosCallback() {
			@Override
			public void invoke(long windowID, int xpos, int ypos) {
				Window window = getWindow(windowID);
				if (window == null)
					return;
				window.posX = xpos;
				window.posY = ypos;
			}
		};

		windowRefreshCallback = new GLFWWindowRefreshCallback() {
			@Override
			public void invoke(long windowID) {
				Window window = getWindow(windowID);
				if (window == null)
					return;
				window.dirty = true;
			}
		};

		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long windowID, int width, int height) {
				Window window = getWindow(windowID);
				if (window == null)
					return;
				window.framebufferWidth = width;
				window.framebufferHeight = height;
			}
		};

		refreshCallback = new GLFWWindowRefreshCallback() {
			@Override
			public void invoke(long window) {
				glfwSwapBuffers(window);
			}
		};

	}

	public static int getUsedVRAM() {
		if (!detected)
			detectGraphicsCard();

		if (nvidia)
			glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX, usedVram);
		return maxVram.get(0) - usedVram.get(0);
	}

	public static boolean isNvidia() {
		if (!detected)
			detectGraphicsCard();
		return nvidia;
	}

	public static boolean isAmd() {
		if (!detected)
			detectGraphicsCard();
		return amd;
	}

	private static void detectGraphicsCard() {
		if (glGetString(GL_VENDOR).contains("NVIDIA")) {
			nvidia = true;
			glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_DEDICATED_VIDMEM_NVX, maxVram);
			Logger.log("Max VRam: " + maxVram.get(0) + "KB");
		} else if (glGetString(GL_VENDOR).contains("AMD")) {
			amd = true;
			glGetIntegerv(WGLAMDGPUAssociation.WGL_GPU_RAM_AMD, maxVram);
			Logger.log("Max VRam: " + maxVram.get(0) + "MB");
		}

		detected = true;
	}

}
