package net.guerra24.voxel.test;

import static org.junit.Assert.*;

import org.junit.Test;

import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.input.KeyCodes;

import static net.guerra24.voxel.client.input.Keyboard.*;

public class BasicTests {

	@Test
	public void testVariables() {
		assertEquals(0.1f, VoxelVariables.NEAR_PLANE, 0.0001);
		assertEquals(1000f, VoxelVariables.FAR_PLANE, 0.0001);
		assertEquals("assets/game/settings.conf", VoxelVariables.settings);
		assertEquals("http://guerra24.github.io/", VoxelVariables.web);
	}

	@Test
	public void testKeyCodesToGLFW() {
		assertEquals(256, KeyCodes.toGlfwKey(KEY_ESCAPE));
		assertEquals(259, KeyCodes.toGlfwKey(KEY_BACK));
		assertEquals(258, KeyCodes.toGlfwKey(KEY_TAB));
		assertEquals(257, KeyCodes.toGlfwKey(KEY_RETURN));
		assertEquals(32, KeyCodes.toGlfwKey(KEY_SPACE));

		assertEquals(341, KeyCodes.toGlfwKey(KEY_LCONTROL));
		assertEquals(340, KeyCodes.toGlfwKey(KEY_LSHIFT));
		assertEquals(342, KeyCodes.toGlfwKey(KEY_LMENU));
		assertEquals(343, KeyCodes.toGlfwKey(KEY_LMETA));

		assertEquals(345, KeyCodes.toGlfwKey(KEY_RCONTROL));
		assertEquals(344, KeyCodes.toGlfwKey(KEY_RSHIFT));
		assertEquals(346, KeyCodes.toGlfwKey(KEY_RMENU));
		assertEquals(347, KeyCodes.toGlfwKey(KEY_RMETA));

		assertEquals(49, KeyCodes.toGlfwKey(KEY_1));
		assertEquals(50, KeyCodes.toGlfwKey(KEY_2));
		assertEquals(51, KeyCodes.toGlfwKey(KEY_3));
		assertEquals(52, KeyCodes.toGlfwKey(KEY_4));
		assertEquals(53, KeyCodes.toGlfwKey(KEY_5));
		assertEquals(54, KeyCodes.toGlfwKey(KEY_6));
		assertEquals(55, KeyCodes.toGlfwKey(KEY_7));
		assertEquals(56, KeyCodes.toGlfwKey(KEY_8));
		assertEquals(57, KeyCodes.toGlfwKey(KEY_9));
		assertEquals(48, KeyCodes.toGlfwKey(KEY_0));

		assertEquals(65, KeyCodes.toGlfwKey(KEY_A));
		assertEquals(66, KeyCodes.toGlfwKey(KEY_B));
		assertEquals(67, KeyCodes.toGlfwKey(KEY_C));
		assertEquals(68, KeyCodes.toGlfwKey(KEY_D));
		assertEquals(69, KeyCodes.toGlfwKey(KEY_E));
		assertEquals(70, KeyCodes.toGlfwKey(KEY_F));
		assertEquals(71, KeyCodes.toGlfwKey(KEY_G));
		assertEquals(72, KeyCodes.toGlfwKey(KEY_H));
		assertEquals(73, KeyCodes.toGlfwKey(KEY_I));
		assertEquals(74, KeyCodes.toGlfwKey(KEY_J));
		assertEquals(75, KeyCodes.toGlfwKey(KEY_K));
		assertEquals(76, KeyCodes.toGlfwKey(KEY_L));
		assertEquals(77, KeyCodes.toGlfwKey(KEY_M));
		assertEquals(78, KeyCodes.toGlfwKey(KEY_N));
		assertEquals(79, KeyCodes.toGlfwKey(KEY_O));
		assertEquals(80, KeyCodes.toGlfwKey(KEY_P));
		assertEquals(81, KeyCodes.toGlfwKey(KEY_Q));
		assertEquals(82, KeyCodes.toGlfwKey(KEY_R));
		assertEquals(83, KeyCodes.toGlfwKey(KEY_S));
		assertEquals(84, KeyCodes.toGlfwKey(KEY_T));
		assertEquals(85, KeyCodes.toGlfwKey(KEY_U));
		assertEquals(86, KeyCodes.toGlfwKey(KEY_V));
		assertEquals(87, KeyCodes.toGlfwKey(KEY_W));
		assertEquals(88, KeyCodes.toGlfwKey(KEY_X));
		assertEquals(89, KeyCodes.toGlfwKey(KEY_Y));
		assertEquals(90, KeyCodes.toGlfwKey(KEY_Z));

		assertEquals(265, KeyCodes.toGlfwKey(KEY_UP));
		assertEquals(264, KeyCodes.toGlfwKey(KEY_DOWN));
		assertEquals(263, KeyCodes.toGlfwKey(KEY_LEFT));
		assertEquals(262, KeyCodes.toGlfwKey(KEY_RIGHT));

		assertEquals(260, KeyCodes.toGlfwKey(KEY_INSERT));
		assertEquals(261, KeyCodes.toGlfwKey(KEY_DELETE));
		assertEquals(268, KeyCodes.toGlfwKey(KEY_HOME));
		assertEquals(269, KeyCodes.toGlfwKey(KEY_END));
		assertEquals(266, KeyCodes.toGlfwKey(KEY_PRIOR));
		assertEquals(267, KeyCodes.toGlfwKey(KEY_NEXT));

		assertEquals(290, KeyCodes.toGlfwKey(KEY_F1));
		assertEquals(291, KeyCodes.toGlfwKey(KEY_F2));
		assertEquals(292, KeyCodes.toGlfwKey(KEY_F3));
		assertEquals(293, KeyCodes.toGlfwKey(KEY_F4));
		assertEquals(294, KeyCodes.toGlfwKey(KEY_F5));
		assertEquals(295, KeyCodes.toGlfwKey(KEY_F6));
		assertEquals(296, KeyCodes.toGlfwKey(KEY_F7));
		assertEquals(297, KeyCodes.toGlfwKey(KEY_F8));
		assertEquals(298, KeyCodes.toGlfwKey(KEY_F9));
		assertEquals(299, KeyCodes.toGlfwKey(KEY_F10));
		assertEquals(300, KeyCodes.toGlfwKey(KEY_F11));
		assertEquals(301, KeyCodes.toGlfwKey(KEY_F12));
		assertEquals(302, KeyCodes.toGlfwKey(KEY_F13));
		assertEquals(303, KeyCodes.toGlfwKey(KEY_F14));
		assertEquals(304, KeyCodes.toGlfwKey(KEY_F15));
		assertEquals(305, KeyCodes.toGlfwKey(KEY_F16));
		assertEquals(306, KeyCodes.toGlfwKey(KEY_F17));
		assertEquals(307, KeyCodes.toGlfwKey(KEY_F18));
		assertEquals(308, KeyCodes.toGlfwKey(KEY_F19));

		assertEquals(321, KeyCodes.toGlfwKey(KEY_NUMPAD1));
		assertEquals(322, KeyCodes.toGlfwKey(KEY_NUMPAD2));
		assertEquals(323, KeyCodes.toGlfwKey(KEY_NUMPAD3));
		assertEquals(324, KeyCodes.toGlfwKey(KEY_NUMPAD4));
		assertEquals(325, KeyCodes.toGlfwKey(KEY_NUMPAD5));
		assertEquals(326, KeyCodes.toGlfwKey(KEY_NUMPAD6));
		assertEquals(327, KeyCodes.toGlfwKey(KEY_NUMPAD7));
		assertEquals(328, KeyCodes.toGlfwKey(KEY_NUMPAD8));
		assertEquals(329, KeyCodes.toGlfwKey(KEY_NUMPAD9));
		assertEquals(320, KeyCodes.toGlfwKey(KEY_NUMPAD0));

		assertEquals(334, KeyCodes.toGlfwKey(KEY_ADD));
		assertEquals(333, KeyCodes.toGlfwKey(KEY_SUBTRACT));
		assertEquals(332, KeyCodes.toGlfwKey(KEY_MULTIPLY));
		assertEquals(331, KeyCodes.toGlfwKey(KEY_DIVIDE));
		assertEquals(330, KeyCodes.toGlfwKey(KEY_DECIMAL));
		assertEquals(336, KeyCodes.toGlfwKey(KEY_NUMPADEQUALS));
		assertEquals(335, KeyCodes.toGlfwKey(KEY_NUMPADENTER));
		assertEquals(282, KeyCodes.toGlfwKey(KEY_NUMLOCK));

		assertEquals(59, KeyCodes.toGlfwKey(KEY_SEMICOLON));
		assertEquals(92, KeyCodes.toGlfwKey(KEY_BACKSLASH));
		assertEquals(44, KeyCodes.toGlfwKey(KEY_COMMA));
		assertEquals(46, KeyCodes.toGlfwKey(KEY_PERIOD));
		assertEquals(47, KeyCodes.toGlfwKey(KEY_SLASH));
		assertEquals(96, KeyCodes.toGlfwKey(KEY_GRAVE));

		assertEquals(280, KeyCodes.toGlfwKey(KEY_CAPITAL));
		assertEquals(281, KeyCodes.toGlfwKey(KEY_SCROLL));

		assertEquals(284, KeyCodes.toGlfwKey(KEY_PAUSE));
		assertEquals(161, KeyCodes.toGlfwKey(KEY_CIRCUMFLEX));

		assertEquals(45, KeyCodes.toGlfwKey(KEY_MINUS));
		assertEquals(61, KeyCodes.toGlfwKey(KEY_EQUALS));
		assertEquals(91, KeyCodes.toGlfwKey(KEY_LBRACKET));
		assertEquals(93, KeyCodes.toGlfwKey(KEY_RBRACKET));
		assertEquals(39, KeyCodes.toGlfwKey(KEY_APOSTROPHE));
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
