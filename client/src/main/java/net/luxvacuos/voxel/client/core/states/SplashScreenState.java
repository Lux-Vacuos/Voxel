package net.luxvacuos.voxel.client.core.states;

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Image;
import net.luxvacuos.voxel.client.ui.Panel;

public class SplashScreenState extends State {

	private Panel panel;
	private Image luxVacuosLogo;
	private float wait = 0;

	public SplashScreenState() {
		panel = new Panel(GameResources.getInstance().getDisplay().getDisplayWidth() / 2,
				GameResources.getInstance().getDisplay().getDisplayHeight() / 2, 0, 0);
		panel.setBorderColor(0, 0, 0, 0);
		panel.setFillColor(0, 0, 0, 0);
		panel.setGradientColor(0, 0, 0, 0);

		luxVacuosLogo = new Image(-256, 256, 512, 512,
				GameResources.getInstance().getLoader().loadNVGTexture("LuxVacuos-Logo"));
		/*
		 * luxVacuosLogo.setOnUpdate(new OnAction() { private float speed = 0;
		 * 
		 * @Override public void onAction(Component component, float delta) {
		 * Image img = (Image) component; if (img.getY() < 200 + 100 && speed <=
		 * 1) speed += 1 * delta; else if (speed > 0) speed -= 1 * delta; else
		 * speed = 0; speed = Maths.clamp(speed, 0, 1); img.addPosition(0,
		 * speed); } });
		 */
		panel.addChildren(luxVacuosLogo);
	}

	@Override
	public void start() {
		panel.setFadeAlpha(0);
	}

	@Override
	public void end() {
		panel.setFadeAlpha(1);
	}

	@Override
	public void render(Voxel voxel, float alpha) {
		GameResources gm = voxel.getGameResources();
		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getDisplay().beingNVGFrame();
		panel.render();
		gm.getDisplay().endNVGFrame();
	}

	@Override
	public void update(Voxel voxel, float delta) throws Exception {
		wait += 1 * delta;
		if (wait > 2)
			panel.update(delta);
		if (!switching)
			panel.fadeIn(4, delta);
		if (switching)
			if (panel.fadeOut(4, delta)) {
				readyForSwitch = true;
			}
		if (wait > 3)
			switchTo(GameState.MAINMENU);
	}

}
