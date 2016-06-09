/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.luxvacuos.voxel.launcher.ui.MainUI;

public class Main extends BorderPane {

	private Button playButton;
	private ProgressBar download;
	Text userName;

	public Main(Stage stage, MainUI ui) {
		userName = new Text();

		Tab home = new Tab("Home");
		home.setClosable(false);

		WebView browser = new WebView();
		browser.getEngine().load("https://guerra24.github.io/launcher");
		browser.minWidth(342);
		browser.minHeight(370);
		home.setContent(browser);

		Tab status = new Tab("Settings");
		status.setClosable(false);

		GridPane gridStatus = new GridPane();
		gridStatus.setAlignment(Pos.CENTER);
		gridStatus.setPadding(new Insets(10, 10, 10, 10));

		status.setContent(gridStatus);

		BorderPane bottom = new BorderPane();

		playButton = new Button("Play");
		playButton.setOnAction((event) -> {
			
			new Thread(() -> {
				try {
					ui.getUpdater().downloadAndRun(ui.getUpdater().getVersionsHandler().getVersions().get(0),
							userName.getText());
					Platform.runLater(() -> playButton.setText("Launching..."));
				} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
					Platform.runLater(() -> {
						playButton.setText("Error downloading, try again");
						playButton.setDisable(false);
					});
					e.printStackTrace();
					return;
				}
			}).start();
			
			while (!ui.getUpdater().isDownloading() && !ui.getUpdater().isDownloaded()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			new Thread(() -> {
				while (ui.getUpdater().isDownloading()) {
					Platform.runLater(
							() -> download.setProgress(ui.getUpdater().getDownloadingVersion().getDownloadProgress()));
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
					while (!ui.getUpdater().isLaunched()) {
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

		TabPane tabPane = new TabPane(home, status);

		setCenter(tabPane);
		setBottom(bottom);
		autosize();
	}

}
