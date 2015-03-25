package voxel.server.core;

import voxel.server.core.world.World;

public class MainServer {

	private static World world;

	public static void init() {
		world = new World();
	}

	public static void initGL() {
	}

	public static void render() {
		world.render();
	}

	public static void update() {
		world.update();
	}

	public static void dispose() {
		world.dispose();
	}

}
