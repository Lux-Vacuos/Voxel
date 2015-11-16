package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.api.API;
import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.GuiResources;
import net.guerra24.voxel.client.world.WorldsHandler;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;

public class GameSPState implements State {

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		GuiResources gi = voxel.getGuiResources();
		WorldsHandler worlds = voxel.getWorldsHandler();
		API api = voxel.getApi();
		Display display = voxel.getDisplay();
		
		worlds.getActiveWorld().updateChunksGeneration(gm, api);
		gm.getPhysics().getMobManager().update(delta, gm, gi, worlds.getActiveWorld(), api);
		gm.getParticleController().update(delta, gm, gi, worlds.getActiveWorld());
		gm.getRenderer().getWaterRenderer().update(delta);
		gm.update(gm.getSkyboxRenderer().update(delta));
		gm.getParticleController().update(delta, gm, gi, worlds.getActiveWorld());
		
		if (!display.isDisplayFocused() && !VoxelVariables.debug) {
			gm.getCamera().unlockMouse();
			states.state = GameState.IN_PAUSE;
		}
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		WorldsHandler worlds = voxel.getWorldsHandler();
		API api = voxel.getApi();
		
		worlds.getActiveWorld().lighting();

		gm.getWaterFBO().begin(128, 128);
		gm.getCamera().invertPitch();
		gm.getRenderer().prepare();
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getWaterFBO().end();
		gm.getCamera().invertPitch();

		gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
		if (VoxelVariables.useShadows) {
			gm.getMasterShadowRenderer().being();
			gm.getRenderer().prepare();
			worlds.getActiveWorld().updateChunksShadow(gm);
			gm.getMasterShadowRenderer().end();
		}

		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());

		gm.getPostProcessing().getPost_fbo().begin(Display.getWidth(), Display.getHeight());
		gm.getRenderer().prepare();
		gm.getRenderer().begin(gm);
		worlds.getActiveWorld().updateChunksRender(gm);
		gm.getRenderer().end(gm);
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm);
		gm.getParticleController().render(gm);
		gm.getCamera().update(delta, gm, voxel.getGuiResources(), worlds.getActiveWorld(), api, voxel.getClient());
		gm.getPhysics().getMobManager().getPlayer().update(delta, gm, voxel.getGuiResources(), worlds.getActiveWorld(),
				api);
		gm.getPostProcessing().getPost_fbo().end();

		gm.getRenderer().prepare();
		gm.getPostProcessing().render(gm);
		gm.getGuiRenderer().renderGui(gm.guis);
	}

}
