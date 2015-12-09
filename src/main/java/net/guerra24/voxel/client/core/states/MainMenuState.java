package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Main Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class MainMenuState implements State {

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		gm.getRenderer().prepare();
		gm.getRenderer().renderGui(gm.getMenuSystem().mainMenu.getList(), gm);
		gm.getGuiRenderer().renderGui(gm.guis2);
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		if (gm.getMenuSystem().mainMenu.getPlayButton().insideButton())
			gm.getMenuSystem().mainMenu.getList().get(0).changeScale(0.075f);
		else
			gm.getMenuSystem().mainMenu.getList().get(0).changeScale(0.07f);

		if (gm.getMenuSystem().mainMenu.getExitButton().insideButton())
			gm.getMenuSystem().mainMenu.getList().get(2).changeScale(0.075f);
		else
			gm.getMenuSystem().mainMenu.getList().get(2).changeScale(0.07f);

		if (gm.getMenuSystem().mainMenu.getOptionsButton().insideButton())
			gm.getMenuSystem().mainMenu.getList().get(1).changeScale(0.075f);
		else
			gm.getMenuSystem().mainMenu.getList().get(1).changeScale(0.07f);
		if (gm.getMenuSystem().mainMenu.getPlayButton().pressed()) {
			gm.getMenuSystem().gameSP.load(gm);
			states.state = GameState.LOADING_WORLD;
		} else if (gm.getMenuSystem().mainMenu.getExitButton().pressed()) {
			states.loop = false;
		} else if (gm.getMenuSystem().mainMenu.getOptionsButton().pressed()) {
			gm.getMenuSystem().optionsMenu.load(gm);
			gm.getCamera().setPosition(new Vector3f(-1.4f, -3.4f, 1.4f));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			states.state = GameState.OPTIONS;
		}
	}

}
