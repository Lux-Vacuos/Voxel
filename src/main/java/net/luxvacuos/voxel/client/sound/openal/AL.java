package net.luxvacuos.voxel.client.sound.openal;

import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.ALC_FREQUENCY;
import static org.lwjgl.openal.ALC10.ALC_REFRESH;
import static org.lwjgl.openal.ALC10.ALC_SYNC;
import static org.lwjgl.openal.ALC10.ALC_TRUE;
import static org.lwjgl.openal.ALC10.alcGetInteger;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC11.ALC_ALL_DEVICES_SPECIFIER;
import static org.lwjgl.openal.ALC11.ALC_MONO_SOURCES;
import static org.lwjgl.openal.ALC11.ALC_STEREO_SOURCES;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;
import org.lwjgl.openal.ALUtil;

import net.luxvacuos.voxel.client.util.Logger;

public class AL {

	static ALContext alContext;
	static ALCdevice alcDevice;
	static ALDevice alDevice;

	private static boolean created = false;

	public static void create() {
		if (alContext == null) {

			alDevice = ALDevice.create(null);
			if (alDevice == null)
				throw new IllegalStateException("Failed to open the default device.");

			ALCCapabilities caps = alDevice.getCapabilities();

			Logger.log("OpenALC10: " + caps.OpenALC10);
			Logger.log("OpenALC11: " + caps.OpenALC11);
			Logger.log("ALC_EXT_EFX: " + caps.ALC_EXT_EFX);

			if (caps.OpenALC11) {
				List<String> devices = ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);
				if (devices == null)
					ALUtil.checkALCError(NULL);
				else {
					for (int i = 0; i < devices.size(); i++)
						Logger.log("Device " + i + ": " + devices.get(i));
				}
			}

			String defaultDeviceSpecifier = alcGetString(0L, ALC_DEFAULT_DEVICE_SPECIFIER);
			Logger.log("Default device: " + defaultDeviceSpecifier);

			long contextHandle = org.lwjgl.openal.ALC10.alcCreateContext(alDevice.address(), (ByteBuffer) null);
			alContext = ALContext.create(alDevice);
			alcDevice = new ALCdevice(contextHandle);

			Logger.log("ALC_FREQUENCY: " + alcGetInteger(alDevice.address(), ALC_FREQUENCY) + "Hz");
			Logger.log("ALC_REFRESH: " + alcGetInteger(alDevice.address(), ALC_REFRESH) + "Hz");
			Logger.log("ALC_SYNC: " + (alcGetInteger(alDevice.address(), ALC_SYNC) == ALC_TRUE));
			Logger.log("ALC_MONO_SOURCES: " + alcGetInteger(alDevice.address(), ALC_MONO_SOURCES));
			Logger.log("ALC_STEREO_SOURCES: " + alcGetInteger(alDevice.address(), ALC_STEREO_SOURCES));

			created = true;
		}
	}

	public static boolean isCreated() {
		return created;
	}

	public static void destroy() {
		alContext.destroy();
		alDevice.close();
		alContext = null;
		alcDevice = null;
		created = false;
	}

	public static ALCdevice getDevice() {
		return alcDevice;
	}

}
