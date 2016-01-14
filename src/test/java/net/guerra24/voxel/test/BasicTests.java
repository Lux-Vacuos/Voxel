package net.guerra24.voxel.test;

import static org.junit.Assert.*;

import org.junit.Test;

import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;

public class BasicTests {

	@Test
	public void testVariables() {
		assertEquals(0.1f, VoxelVariables.NEAR_PLANE, 0.0001);
		assertEquals(1000f, VoxelVariables.FAR_PLANE, 0.0001);
		assertEquals("assets/game/settings.conf", VoxelVariables.settings);
		assertEquals("http://guerra24.github.io/", VoxelVariables.web);
	}

	@Test
	public void testRun() {

		Voxel voxel = new Voxel("test");

		assertNotNull(voxel);
		/*
		 * voxel.preInit(); voxel.init(); voxel.postInit();
		 * assertFalse(voxel.getGameResources().getCamera().isUnderWater());
		 * assertFalse(voxel.getGameResources().getCamera().isMoved);
		 * assertNotNull(voxel.getGameResources().getRand());
		 * assertNotNull(voxel.getGameResources().getLoader());
		 * assertNotNull(voxel.getGameResources().getCamera());
		 * assertNotNull(voxel.getGameResources().getSun_Camera());
		 * assertNotNull(voxel.getGameResources().getRenderer());
		 * assertNotNull(voxel.getGameResources().getSkyboxRenderer());
		 * assertNotNull(voxel.getGameResources().getGlobalStates());
		 * assertNotNull(voxel.getGameResources().getDeferredShadingRenderer());
		 * assertNotNull(voxel.getGameResources().getMasterShadowRenderer());
		 * assertNotNull(voxel.getGameResources().getOcclusionRenderer());
		 * assertNotNull(voxel.getGameResources().getPhysicsEngine());
		 * assertNotNull(voxel.getGameResources().getSoundSystem());
		 * assertNotNull(voxel.getGameResources().getFrustum());
		 * assertNotNull(voxel.getGameResources().getKryo());
		 * assertNotNull(voxel.getGameResources().getMenuSystem());
		 * assertNotNull(voxel.getGameResources().getGameSettings());
		 * 
		 * voxel.dispose();
		 */
	}
}
