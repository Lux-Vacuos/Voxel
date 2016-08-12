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

package net.luxvacuos.voxel.server.ui;

import static javafx.collections.FXCollections.observableArrayList;

import java.io.PrintStream;

import com.esotericsoftware.kryonet.Connection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.server.core.CoreUtils;
import net.luxvacuos.voxel.server.core.commands.CommandsHandler;
import net.luxvacuos.voxel.server.core.commands.KickCommand;
import net.luxvacuos.voxel.server.core.commands.StopCommand;
import net.luxvacuos.voxel.server.core.commands.TimeCommand;
import net.luxvacuos.voxel.server.network.ConnectionsHandler;
import net.luxvacuos.voxel.server.resources.GameResources;

public class MainUI extends Application {

	static void launchUI(String... args) {
		launch(args);
	}

	private boolean close = false;

	private Text textUps;
	private CoreUtils coreUtils;

	private ObservableList<String> players;

	@Override
	public void start(Stage stage) throws Exception {
		Thread.currentThread().setName("Voxel-Server-UI");
		coreUtils = new CoreUtils();
		stage.setTitle("Voxel Server - UI");
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("assets/icons/icon32.png")));
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("assets/icons/icon64.png")));
		players = observableArrayList();
		Scene main = new Scene(startMain(stage));
		stage.setScene(main);
		stage.centerOnScreen();
		stage.show();
		stage.sizeToScene();
		stage.setMinHeight(780);
		stage.setMinWidth(1094);
		stage.setHeight(780);
		stage.setWidth(1094);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST && !close) {
					event.consume();
					final Stage dialog = new Stage();
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.initOwner(stage);
					GridPane gridEdit = new GridPane();
					gridEdit.setAlignment(Pos.CENTER);
					gridEdit.setHgap(10);
					gridEdit.setVgap(10);
					gridEdit.setPadding(new Insets(10, 10, 10, 10));

					Button btnClose = new Button("Back");
					btnClose.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							dialog.close();
						}
					});
					gridEdit.add(btnClose, 0, 1);
					Text dl = new Text("The server is running, can't close UI. For exit please stop the server");
					gridEdit.add(dl, 0, 0);

					Scene dialogScene = new Scene(gridEdit, 400, 100);
					dialog.setScene(dialogScene);
					dialog.setTitle("Can't Close");
					dialog.setResizable(false);
					dialog.getIcons().addAll(stage.getIcons());
					dialog.show();
				}
			};
		});
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				while (!UserInterface.ready)
					Thread.sleep(100);
				while (!close) {
					textUps.setText("Loaded Chunks: " + GameResources.getInstance().getWorldsHandler().getActiveWorld()
							.getActiveDimension().getLoadedChunks());

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							players.clear();
							players.addAll(GameResources.getInstance().getVoxelServer().getNames());
						}
					});
					coreUtils.sync(1);
				}
				return null;
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();

		UserInterface.started = true;
	}

	private BorderPane startMain(Stage stage) {
		GridPane gridBottom = new GridPane();
		gridBottom.setAlignment(Pos.CENTER);
		gridBottom.setHgap(10);
		gridBottom.setVgap(10);
		gridBottom.setPadding(new Insets(10, 10, 10, 10));
		Button btnStop = new Button("Stop and Exit");
		btnStop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				CommandsHandler.getInstace().addCommand(new StopCommand());
				close = true;
				stage.close();
			}
		});
		btnStop.autosize();
		gridBottom.getChildren().add(btnStop);

		Tab home = new Tab("Home");
		home.setClosable(false);
		GridPane gridHome = new GridPane();
		gridHome.setAlignment(Pos.CENTER);
		gridHome.setHgap(10);
		gridHome.setVgap(10);
		gridHome.setPadding(new Insets(10, 10, 10, 10));
		gridBottom.autosize();
		ListView<String> pl = new ListView<String>(players);
		pl.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				ListCell<String> cell = new ListCell<>();
				ContextMenu contextMenu = new ContextMenu();

				MenuItem editItem = new MenuItem();
				editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
				editItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String item = cell.getItem();
						Connection con = ConnectionsHandler.getInstace().getByName(item);
						final Stage dialog = new Stage();
						dialog.initModality(Modality.APPLICATION_MODAL);
						dialog.initOwner(stage);
						GridPane gridEdit = new GridPane();
						gridEdit.setAlignment(Pos.CENTER);
						gridEdit.setHgap(10);
						gridEdit.setVgap(10);
						gridEdit.setPadding(new Insets(10, 10, 10, 10));

						Button btnClose = new Button("Save and Back");
						btnClose.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								dialog.close();
							}
						});
						gridEdit.getChildren().add(btnClose);

						Scene dialogScene = new Scene(gridEdit, 300, 200);
						dialog.setScene(dialogScene);
						dialog.setTitle("Edit Menu");
						dialog.setResizable(false);
						dialog.getIcons().addAll(stage.getIcons());
						dialog.show();
					}
				});
				MenuItem kickPlayer = new MenuItem();
				kickPlayer.textProperty().bind(Bindings.format("Kick \"%s\"", cell.itemProperty()));
				kickPlayer.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String item = cell.getItem();
						Logger.log(item + " was kicked from this server");
						ConnectionsHandler.getInstace().getByName(item).close();
						param.getItems().remove(item);
						GameResources.getInstance().getVoxelServer().updateNames();
					}
				});
				MenuItem banPlayer = new MenuItem();
				banPlayer.textProperty().bind(Bindings.format("Ban \"%s\"", cell.itemProperty()));
				banPlayer.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String item = cell.getItem();
						Logger.log(item + " was banned from this server");
						ConnectionsHandler.getInstace().getByName(item).close();
						param.getItems().remove(item);
					}
				});
				contextMenu.getItems().addAll(editItem, kickPlayer, banPlayer);
				cell.textProperty().bind(cell.itemProperty());
				cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
					if (isNowEmpty) {
						cell.setContextMenu(null);
					} else {
						cell.setContextMenu(contextMenu);
					}
				});
				return cell;
			}
		});
		pl.autosize();
		gridHome.add(pl, 0, 0);

		TextArea tac = new TextArea();
		tac.setMinWidth(800);
		tac.setMinHeight(600);
		tac.setWrapText(true);
		tac.setEditable(false);
		Console console = new Console(tac);
		PrintStream ps = new PrintStream(console, true);
		System.setOut(ps);
		System.setErr(ps);
		tac.autosize();
		gridHome.add(tac, 1, 0);
		home.setContent(gridHome);

		TextArea tar = new TextArea();
		tar.setPrefWidth(800);
		tar.setPrefHeight(14);
		tar.setWrapText(true);
		tar.setEditable(true);
		tar.setPrefRowCount(1);
		tar.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					String text = tar.getText();
					Logger.log(text);
					if (text.equals("/stop")) {
						CommandsHandler.getInstace().addCommand(new StopCommand());
						close = true;
						btnStop.setText("Exit");
					} else if (text.contains("/time set")) {
						CommandsHandler.getInstace().addCommand(new TimeCommand(Float.parseFloat(text.substring(10))));
					} else if (text.contains("/kick")) {
						if (text.length() > 6)
							CommandsHandler.getInstace().addCommand(new KickCommand(text.substring(6)));
						else
							Logger.log("Syntax: /kick <username>");
					} else {
						Logger.log("Command not found: " + text);
					}
					tar.clear();
					event.consume();
				}
			}
		});
		tar.autosize();
		gridHome.add(tar, 1, 1);
		home.setContent(gridHome);

		Tab status = new Tab("Status");
		status.setClosable(false);
		GridPane gridStatus = new GridPane();
		gridStatus.setHgap(10);
		gridStatus.setVgap(10);
		gridStatus.setPadding(new Insets(10, 10, 10, 10));
		textUps = new Text("Loaded Chunks: ");
		gridStatus.getChildren().add(textUps);
		status.setContent(gridStatus);

		TabPane tabPane = new TabPane(home, status);
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(tabPane);
		borderPane.setBottom(gridBottom);
		borderPane.autosize();
		return borderPane;
	}

}
