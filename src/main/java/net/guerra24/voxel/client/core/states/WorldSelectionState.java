package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.graphics.MenuRendering;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.resources.GameResources;

public class WorldSelectionState implements State {

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		if (gm.getMenuSystem().worldSelectionMenu.getExitButton().pressed())
			gm.getGlobalStates().setState(GameState.MAINMENU);
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float alpha) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		Display.beingNVGFrame();
		gm.getMenuSystem().worldSelectionMenu.render();
		MenuRendering.renderMouse();
		Display.endNVGFrame();
	}

}
