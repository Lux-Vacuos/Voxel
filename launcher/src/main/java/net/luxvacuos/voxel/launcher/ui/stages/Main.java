/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.launcher.ui.stages;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.luxvacuos.voxel.launcher.core.LauncherVariables;
import net.luxvacuos.voxel.launcher.core.Updater;
import net.luxvacuos.voxel.launcher.ui.MainUI;

public class Main extends BorderPane {

	private Button playButton;
	private ProgressBar download;
	Text userName;

	private TextField wf;
	private TextField hf;

	public Main(Stage stage, MainUI ui) {

		userName = new Text();

		Tab home = new Tab("Home");
		home.setClosable(false);

		WebView browser = new WebView();
		browser.getEngine().load("https://luxvacuos.net/");
		browser.minWidth(342);
		browser.minHeight(370);
		home.setContent(browser);

		BorderPane bottom = new BorderPane();

		playButton = new Button("Play");
		playButton.setOnAction((event) -> {

			new Thread(() -> {

				LauncherVariables.userArgs.add("-width");
				LauncherVariables.userArgs.add(wf.getText());
				LauncherVariables.userArgs.add("-height");
				LauncherVariables.userArgs.add(hf.getText());

				try {
					Updater.getUpdater().downloadAndRun();
					Platform.runLater(() -> playButton.setText("Launching..."));
				} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
					Platform.runLater(() -> {
						playButton.setText("Error downloading, try again");
						playButton.setDisable(false);
					});
					return;
				}
			}).start();

			while (!Updater.getUpdater().isDownloading() && !Updater.getUpdater().isDownloaded()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			new Thread(() -> {
				while (Updater.getUpdater().isDownloading()) {
					Platform.runLater(() -> download
							.setProgress(Updater.getUpdater().getDownloadingVersion().getDownloadProgress()));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Platform.runLater(() -> download.setProgress(1f));
			}).start();

			playButton.setText("Downloading... Please Wait");
			playButton.setDisable(true);

			new Thread(() -> {
				try {
					while (!Updater.getUpdater().isLaunched()) {
						Thread.sleep(100);
					}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(() -> stage.close());
			}).start();
		});
		playButton.setMinSize(160, 60);
		playButton.setFont(new Font(16));
		bottom.setCenter(playButton);

		download = new ProgressBar(0);
		download.setMinWidth(300);
		download.setPrefWidth(4000);
		bottom.setBottom(download);

		GridPane left = new GridPane();
		left.setAlignment(Pos.CENTER);
		left.setHgap(10);
		left.setVgap(10);
		left.setPadding(new Insets(20, 20, 20, 20));

		Tab settings = new Tab("Settings");
		settings.setClosable(false);

		GridPane gridSettings = new GridPane();
		gridSettings.setPadding(new Insets(10, 10, 10, 10));

		GridPane resGrid = new GridPane();
		resGrid.setVgap(10);
		resGrid.setAlignment(Pos.CENTER);

		Text resolutionText = new Text("Resolution");
		Font font = new Font(16);
		resolutionText.setFont(font);
		resGrid.add(resolutionText, 0, 0);

		GridPane resOpts = new GridPane();
		resOpts.setHgap(10);
		Text w = new Text("Width");
		resOpts.add(w, 0, 0);

		wf = new TextField("1280");
		resOpts.add(wf, 1, 0);

		Text h = new Text("Height");
		resOpts.add(h, 0, 1);

		hf = new TextField("720");
		resOpts.add(hf, 1, 1);

		resGrid.add(resOpts, 0, 1);

		gridSettings.add(resGrid, 0, 0);

		settings.setContent(gridSettings);

		TabPane tabPane = new TabPane(home, settings);

		setCenter(tabPane);
		setBottom(bottom);
		autosize();
	}

}
