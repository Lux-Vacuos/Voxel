package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.particle.ParticleMaster;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.WorldsHandler;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * In Pause State
 * 
 * @author danirod
 * @category Kernel
 */
public class InPauseState extends State {

	public InPauseState() {
		super(4);
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		while (Mouse.next()) {
			if (gm.getMenuSystem().pauseMenu.getBackToMain().pressed()) {
				gm.getMenuSystem().mainMenu.load(gm);
				voxel.getWorldsHandler().getActiveWorld().clearDimension(gm);
				if (gm.getRand().nextBoolean())
					gm.getSoundSystem().play("menu1");
				else
					gm.getSoundSystem().play("menu2");
				gm.getCamera().setPosition(new Vector3f(0, 0, 1));
				gm.getCamera().setPitch(0);
				gm.getCamera().setYaw(0);
				states.setState(GameState.MAINMENU);
			}
		}
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		WorldsHandler worlds = voxel.getWorldsHandler();
		worlds.getActiveWorld().lighting();
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		if (VoxelVariables.useShadows) {
			gm.getMasterShadowRenderer().being();
			gm.getRenderer().prepare();
			worlds.getActiveWorld().updateChunksShadow(gm);
			gm.getMasterShadowRenderer().end();
		}
		gm.getDeferredShadingRenderer().getPost_fbo().begin();
		gm.getRenderer().prepare();
		gm.getRenderer().begin(gm);
		worlds.getActiveWorld().updateChunksRender(gm);
		gm.getRenderer().end(gm);
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm);
		ParticleMaster.getInstance().render(gm.getCamera());
		gm.getDeferredShadingRenderer().getPost_fbo().end();
		gm.getRenderer().prepare();
		gm.getDeferredShadingRenderer().render(gm);
		gm.getGuiRenderer().renderGui(gm.guis4);
	}

}
