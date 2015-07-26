package io.github.guerra24.voxel.client.kernel.test;

import static org.junit.Assert.assertEquals;
import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.world.World;

import org.junit.Test;

public class KernelTest {
	@Test
	public void kernelWorld() {
		Kernel kernel = new Kernel();
		World world = new World();
		assertEquals(kernel.world, world);
		kernel.gameResources.gameStates.loop = false;
	}

	@Test
	public void kernelGameRes() {
		Kernel kernel = new Kernel();
		GameResources gameResources = new GameResources();
		assertEquals(kernel.gameResources, gameResources);
		kernel.gameResources.gameStates.loop = false;
	}

	@Test
	public void kernelGUIRes() {
		Kernel kernel = new Kernel();
		GuiResources guiResources = new GuiResources();
		assertEquals(kernel.gameResources, guiResources);
		kernel.gameResources.gameStates.loop = false;
	}
}