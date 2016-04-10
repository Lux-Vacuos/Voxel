package net.luxvacuos.voxel.client.ui;

import java.io.PrintStream;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CrashScreen extends Application {

	public static PrintStream ps;
	public static boolean ready = false;

	public static void run() {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Voxel");
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("assets/icons/icon32.png")));
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("assets/icons/icon64.png")));
		Scene main = new Scene(startMain(stage));
		stage.setScene(main);
		stage.setMinWidth(894);
		stage.setMinHeight(680);
		stage.setWidth(894);
		stage.setHeight(680);
		stage.centerOnScreen();
		stage.show();
		ready = true;
	}

	private BorderPane startMain(Stage stage) {
		GridPane gridTop = new GridPane();
		gridTop.setAlignment(Pos.CENTER);
		gridTop.setHgap(10);
		gridTop.setVgap(10);
		gridTop.setPadding(new Insets(10, 10, 10, 10));
		gridTop.autosize();

		Image voxelLogoI = new Image(
				getClass().getClassLoader().getResourceAsStream("assets/textures/menu/Voxel-Logo.png"));

		ImageView voxelLogo = new ImageView();
		voxelLogo.setImage(voxelLogoI);
		voxelLogo.setFitWidth(200);
		voxelLogo.setPreserveRatio(true);
		voxelLogo.setSmooth(true);
		voxelLogo.setCache(true);

		gridTop.getChildren().add(voxelLogo);

		GridPane gridCenter = new GridPane();
		gridCenter.setAlignment(Pos.CENTER);
		gridCenter.setHgap(10);
		gridCenter.setVgap(10);
		gridCenter.setPadding(new Insets(10, 10, 10, 10));
		gridCenter.autosize();

		TextArea tac = new TextArea();
		tac.setMinWidth(854);
		tac.setMinHeight(480);
		tac.setPrefHeight(720);
		tac.setPrefWidth(1280);
		tac.setEditable(false);
		tac.autosize();
		Console console = new Console(tac);
		ps = new PrintStream(console, true);

		gridCenter.getChildren().add(tac);

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(gridTop);
		borderPane.setCenter(gridCenter);
		borderPane.autosize();

		return borderPane;
	}

}
