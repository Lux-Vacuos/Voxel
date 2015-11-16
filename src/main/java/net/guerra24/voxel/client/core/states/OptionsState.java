package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Options Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class OptionsState implements State {

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		if (gm.getMenuSystem().optionsMenu.getExitButton().pressed()) {
			voxel.getGameResources().getCamera().setPosition(new Vector3f(0, 0, 1));
			states.state = GameState.MAINMENU;
		}

		if (gm.getMenuSystem().optionsMenu.getExitButton().insideButton())
			gm.getMenuSystem().mainMenu.getList().get(3).changeScale(0.074f);
		else
			gm.getMenuSystem().mainMenu.getList().get(3).changeScale(0.07f);
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		gm.getRenderer().prepare();
		gm.getRenderer().renderGui(gm.getMenuSystem().mainMenu.getList(), gm);
		gm.getGuiRenderer().renderGui(gm.guis2);
	}

}
