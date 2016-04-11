package net.luxvacuos.voxel.client.sound.openal;

import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.ALC_FREQUENCY;
import static org.lwjgl.openal.ALC10.ALC_REFRESH;
import static org.lwjgl.openal.ALC10.ALC_SYNC;
import static org.lwjgl.openal.ALC10.ALC_TRUE;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetInteger;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.openal.ALC11.ALC_ALL_DEVICES_SPECIFIER;
import static org.lwjgl.openal.ALC11.ALC_MONO_SOURCES;
import static org.lwjgl.openal.ALC11.ALC_STEREO_SOURCES;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALUtil;

import net.luxvacuos.igl.Logger;

public class AL {

	static long device;
	static long context;

	private static boolean created = false;

	public static void create() {
		device = alcOpenDevice((ByteBuffer) null);
		if (device == NULL)
			throw new IllegalStateException("Failed to open the default device.");

		ALCCapabilities deviceCaps = ALC.createCapabilities(device);

		Logger.log("OpenALC10: " + deviceCaps.OpenALC10);
		Logger.log("OpenALC11: " + deviceCaps.OpenALC11);
		Logger.log("caps.ALC_EXT_EFX = " + deviceCaps.ALC_EXT_EFX);

		if (deviceCaps.OpenALC11) {
			List<String> devices = ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);
			if (devices == null)
				ALUtil.checkALCError(NULL);
			else {
				for (int i = 0; i < devices.size(); i++)
					Logger.log(i + ": " + devices.get(i));
			}
		}

		String defaultDeviceSpecifier = alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER);
		Logger.log("Default device: " + defaultDeviceSpecifier);

		context = alcCreateContext(device, (ByteBuffer) null);
		alcMakeContextCurrent(context);
		org.lwjgl.openal.AL.createCapabilities(deviceCaps);

		Logger.log("ALC_FREQUENCY: " + alcGetInteger(device, ALC_FREQUENCY) + "Hz");
		Logger.log("ALC_REFRESH: " + alcGetInteger(device, ALC_REFRESH) + "Hz");
		Logger.log("ALC_SYNC: " + (alcGetInteger(device, ALC_SYNC) == ALC_TRUE));
		Logger.log("ALC_MONO_SOURCES: " + alcGetInteger(device, ALC_MONO_SOURCES));
		Logger.log("ALC_STEREO_SOURCES: " + alcGetInteger(device, ALC_STEREO_SOURCES));

		created = true;
	}

	public static boolean isCreated() {
		return created;
	}

	public static void destroy() {
		alcDestroyContext(context);
		alcCloseDevice(device);
		created = false;
	}

}
