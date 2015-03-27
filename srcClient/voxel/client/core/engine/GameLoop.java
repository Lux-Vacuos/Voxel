package voxel.client.core.engine;


public class GameLoop {

	private Screen currentScreen;

	private double frameCap = 60.0;
	private boolean running = false, debugMode = false;;
	private int frames = 0;
	private static int currentFPS;

	public void setScreen(Screen screen) {
		if (currentScreen != null)
			currentScreen.dispose();
		currentScreen = screen;
		currentScreen.initGL();
		currentScreen.init();
	}

	public void start(int frameCap) {
		if (running)
			return;
		running = true;
		this.frameCap = frameCap;
		run();
	}

	private void run() {
		frames = 0;
		int frameCounter = 0;

		final double frameTime = 1.0 / frameCap;

		long lastTime = Time.getTime();
		double unprocessed = 0;

		while (running && !Window.isWindowCloseRequested()) {
			boolean render = false;
			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessed += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;

			while (unprocessed > frameTime) {
				render = true;
				unprocessed -= frameTime;

				if (Window.isWindowCloseRequested())
					stop();

				Time.setDelta(frameTime);
				update();

				if (frameCounter >= Time.SECOND) {
					if (debugMode) {
						System.out.println("FPS: " + frames);
					}
					currentFPS = frames;
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		stop();
	}

	private void update() {
		Window.update();
		currentScreen.update();
	}

	private void render() {
		currentScreen.render();
	}

	public void stop() {
		if (!running)
			return;
		running = false;
		currentScreen.dispose();
		Window.dispose();
	}

	public double getFrameCap() {
		return frameCap;
	}

	public void setFrameCap(double frameCap) {
		this.frameCap = frameCap;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public boolean isRunning() {
		return running;
	}

	public static int getFPS() {
		return currentFPS;
	}
}
