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
import javafx.scene.control.TextAreaBuilder;
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
import net.luxvacuos.voxel.server.core.CoreInfo;
import net.luxvacuos.voxel.server.core.CoreUtils;
import net.luxvacuos.voxel.server.core.Voxel;
import net.luxvacuos.voxel.server.network.ConnectionsHandler;
import net.luxvacuos.voxel.server.util.Logger;

public class MainUI extends Application {

	private static Voxel voxel;

	static void launchUI(String... args) {
		launch(args);
	}

	static void setVoxel(Voxel voxel) {
		MainUI.voxel = voxel;
	}

	private boolean close = false;

	private Text textUps;
	private CoreUtils coreUtils;

	private ObservableList<String> players;

	@Override
	public void start(Stage stage) throws Exception {
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
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST && !close) {
					event.consume();
				}
			};
		});

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				while (!close) {
					textUps.setText("Updates Per Second: " + CoreInfo.ups);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							players.clear();
							players.addAll(voxel.getGameResources().getVoxelServer().getNames());
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
				voxel.getGameResources().getGlobalStates().loop = false;
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
						voxel.getGameResources().getVoxelServer().updateNames();
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

		TextArea tac = TextAreaBuilder.create().prefWidth(800).prefHeight(600).wrapText(true).build();
		tac.setEditable(false);
		Console console = new Console(tac);
		PrintStream ps = new PrintStream(console, true);
		System.setOut(ps);
		System.setErr(ps);
		Logger.readyToConsole = true;
		tac.autosize();
		gridHome.add(tac, 1, 0);
		home.setContent(gridHome);

		TextArea tar = TextAreaBuilder.create().prefWidth(800).prefHeight(14).wrapText(true).build();
		tar.setEditable(true);
		tar.setPrefRowCount(1);
		tar.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					String text = tar.getText();
					Logger.log("[Server] " + text);
					if (text.equals("/stop")) {
						voxel.getGameResources().getGlobalStates().loop = false;
						btnStop.setText("Exit");
					} else if (text.contains("/time set")) {
						voxel.time = Float.parseFloat(text.substring(10));
						Logger.log("Time set to: " + voxel.time);
					} else if (text.contains("/kick")) {
						try {
							ConnectionsHandler.getInstace().getByName(text.substring(6)).close();
						} catch (NullPointerException e) {
							Logger.log("[Server] User not found: " + text.substring(6));
						}
						voxel.getGameResources().getVoxelServer().updateNames();
					} else {
						Logger.log("[Server] Command not found: " + text);
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
		gridStatus.setAlignment(Pos.CENTER);
		gridStatus.setHgap(10);
		gridStatus.setVgap(10);
		gridStatus.setPadding(new Insets(10, 10, 10, 10));
		textUps = new Text();
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
