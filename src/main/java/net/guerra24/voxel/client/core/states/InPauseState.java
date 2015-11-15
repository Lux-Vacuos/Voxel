package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.menu.PauseMenu;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class InPauseState implements State {

	private PauseMenu pauseMenu;
	
	public InPauseState() {
		pauseMenu = new PauseMenu();
	}
	
	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		
		if (pauseMenu.getBackToMain().pressed()) {
			voxel.getWorldsHandler().getActiveWorld().clearDimension(gm);
			gm.getSoundSystem().play("menu1");
			gm.getCamera().setPosition(new Vector3f(0, 0, 1));
			gm.getCamera().setPitch(0);
			gm.getCamera().setYaw(0);
			states.state = GameState.MAINMENU;
			gm.getSoundSystem().setVolume("menu1", 1f);
		}
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		
		gm.getRenderer().prepare();
		gm.getRenderer().begin(gm);
		voxel.getWorldsHandler().getActiveWorld().updateChunksRender(gm);
		gm.getRenderer().end(gm);
		gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm);
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getParticleController().render(gm);
		gm.getGuiRenderer().renderGui(gm.guis4);
	}

}
