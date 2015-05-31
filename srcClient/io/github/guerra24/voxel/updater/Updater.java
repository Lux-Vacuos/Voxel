package io.github.guerra24.voxel.updater;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Updater extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private ProgressBar progressBar;

	private Text text;

	private String[] prompts = new String[] { "Fetching Update...",
			"Downloading Update... Please Wait. (FILE)", "Launching Voxel Game" };

	private String updateServerUrl = "http://guerra24.github.io/", latest;

	private int state = 0;

	private double progress = 0;

	private boolean running = false;

	private Properties properties = new Properties();

	private boolean startedDownload = false;

	private boolean updated = false;

	private JarInputStream bufferedInput = null;

	private JarOutputStream fileOutput = null;

	private int blocks = 0, inputState;

	private JarEntry entry;

	float block = 1;

	byte[] bytes = new byte[1024];

	@Override
	public void start(final Stage stage) throws Exception {
		properties.load(new FileInputStream("version.properties"));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.setTitle("Updater");
		stage.setMinHeight(100);
		stage.setMaxHeight(100);
		stage.setMinWidth(450);
		stage.setMaxWidth(450);
		VBox group = new VBox(10);
		progressBar = new ProgressBar(0.2);
		progressBar.setMinWidth(300);
		progressBar.setMaxWidth(300);
		progressBar.setMinHeight(30);
		progressBar.setMaxHeight(30);
		progressBar.getStyleClass().add("green-bar");
		group.getChildren().add(progressBar);
		text = new Text("Fetching Update...");
		group.getChildren().add(text);
		group.setAlignment(Pos.CENTER);
		Scene scene = new Scene(group, 500, 100, Color.LIGHTGRAY);
		scene.getStylesheets().add(
				getClass().getResource("progress.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
		stage.requestFocus();
		running = true;
		schedule(stage);
		Thread updater = new Thread() {
			long start, end;

			public void run() {
				while (running) {
					if (state == 0) {
						try {
							start = System.currentTimeMillis();
							if (updateServerUrl == null) {
								Logger.error("Invalid URL");
								System.exit(-1);
							}
							URL url = new URL(updateServerUrl + "version.html");
							URLConnection conn = url.openConnection();
							BufferedReader bufferedReader = new BufferedReader(
									new InputStreamReader(conn.getInputStream()));
							latest = bufferedReader.readLine();
							if (!latest.equalsIgnoreCase(properties
									.getProperty("version"))) {
								state = 1;
								progress = 0;
							} else {
								state = 2;
							}
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						progress = 1;
					} else if (state == 1) {
						if (!startedDownload) {
							String fileUrl = updateServerUrl + "Voxel-"
									+ latest + ".jar";
							try {
								updated = true;
								bufferedInput = new JarInputStream(new URL(
										fileUrl).openStream());
								fileOutput = new JarOutputStream(
										new FileOutputStream("Voxel.jar"),
										bufferedInput.getManifest());
								blocks = -1;
								startedDownload = true;
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							if (block == blocks || inputState == -1) {
								try {
									bufferedInput.close();
									fileOutput.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								progress = 1;
								state++;
								end = System.currentTimeMillis();
							} else {
								try {
									entry = bufferedInput.getNextJarEntry();
									if (entry != null) {
										fileOutput.putNextEntry(entry);
										int len = bufferedInput.read(bytes, 0,
												bytes.length);
										while (len != -1) {
											fileOutput.write(bytes, 0, len);
											len = bufferedInput.read(bytes, 0,
													bytes.length);
										}
										Logger.log("Writing Entry: " + entry);
										progress++;
										block++;
									} else {
										block = -1;
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					} else if (state == 2) {
						properties.setProperty("version", latest);
						try {
							properties.store(new FileOutputStream(
									"version.properties", false), "");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						if (updated) {
							Logger.log("The game was successfully updated in "
									+ (end - start) + "ms");
						}
						Platform.runLater(new Runnable() {
							public void run() {
								stage.close();
								System.exit(0);
							}
						});
						launchJar();
						running = false;
					}
					Platform.runLater(new Runnable() {
						public void run() {
							progressBar.setProgress(progress / 109);
							text.setText(prompts[state].replaceAll("FILE",
									String.valueOf(entry)));
						}
					});
				}
			}
		};
		updater.start();
	}

	public void launchJar() {
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "Voxel.jar");
		try {
			Logger.log("Launch Voxel Game");
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void schedule(Stage stage) {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				stop();
			}
		});
	}

	public void stop() {
		running = false;
	}

}