package net.luxvacuos.voxel.launcher.bootstrap;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.luxvacuos.voxel.launcher.core.Library;
import net.luxvacuos.voxel.launcher.core.Version;
import net.luxvacuos.voxel.launcher.core.VersionKey;
import net.luxvacuos.voxel.launcher.core.VersionsHandler;

/**
 * 
 * This class is only used for storing templates that are used by the launcher
 * to download all the assets.
 * 
 * @author Guerra24 <pablo230699@hotmal.com>
 *
 */
@SuppressWarnings("unused")
public class Templates {

	/**
	 * Latest version template
	 */
	public static void generateVoxelTemplate() {

		Version voxel = new Version("client", "net.luxvacuos.voxel", "0.0.10", "alpha");

		voxel.getLibs().add(new Library("universal", "net.luxvacuos.voxel", "0.0.1"));

		Library utils = new Library("utils", "net.luxvacuos.voxel", "0.0.1");
		utils.getDependencies().add(new Library("jsonbeans", "com.esotericsoftware", "0.7"));
		voxel.getLibs().add(utils);

		Library lwjgl = new Library("lwjgl", "org.lwjgl", "3.0.0-SNAPSHOT");
		lwjgl.getDependencies().add(new Library("lwjgl-platform", "org.lwjgl", "3.0.0-SNAPSHOT-natives-linux"));
		lwjgl.getDependencies().add(new Library("lwjgl-platform", "org.lwjgl", "3.0.0-SNAPSHOT-natives-osx"));
		lwjgl.getDependencies().add(new Library("lwjgl-platform", "org.lwjgl", "3.0.0-SNAPSHOT-natives-windows"));
		voxel.getLibs().add(lwjgl);

		voxel.getLibs().add(new Library("ashley", "com.badlogicgames.ashley", "1.7.3"));

		Library kryo = new Library("kryo", "com.esotericsoftware", "3.0.3");
		Library reflectams = new Library("reflectasm", "com.esotericsoftware", "1.10.1");
		reflectams.getDependencies().add(new Library("asm", "org.ow2.asm", "5.0.3"));
		kryo.getDependencies().add(reflectams);
		kryo.getDependencies().add(new Library("minlog", "com.esotericsoftware", "1.3.0"));
		kryo.getDependencies().add(new Library("objenesis", "org.objenesis", "2.1"));
		voxel.getLibs().add(kryo);

		voxel.getLibs().add(new Library("j-ogg-all", "de.jarnbjo", "1.0.0"));
		voxel.getLibs().add(new Library("jorbis", "org.jcraft", "0.0.17"));
		voxel.getLibs().add(new Library("pngdecoder", "org.l33tlabs.twl", "1.0"));
		voxel.getLibs().add(new Library("log4j", "log4j", "1.2.17"));

		voxel.download();

		Gson gson = new Gson();
		String json = gson.toJson(voxel);
		System.out.println(json);

		try (FileWriter writer = new FileWriter("client-0.0.10.json")) {
			gson.toJson(voxel, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Version ld = gson.fromJson(new FileReader("client-0.0.10.json"), Version.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Latest VersionHandler template
	 */
	public static void generateVersionHandlerTemplate() {
		VersionsHandler vers = new VersionsHandler();
		vers.getVersions().add(new VersionKey("client", "0.0.10", "alpha"));

		Gson gson = new Gson();
		String json = gson.toJson(vers);
		System.out.println(json);

		try (FileWriter writer = new FileWriter("remote.json")) {
			gson.toJson(vers, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			VersionsHandler lod = gson.fromJson(new FileReader("remote.json"), VersionsHandler.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
