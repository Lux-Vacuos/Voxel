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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.luxvacuos.voxel.launcher.ui.MainUI;

public class Login extends GridPane {

	private Text userText;
	private TextField userField;
	private Text passText;
	private TextField passField;
	private Button login;
	private ProgressBar loginProgress;

	public Login(Stage stage, MainUI ui) {

		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(20, 100, 20, 100));

		Image voxelLogoI = new Image(getClass().getClassLoader().getResourceAsStream("assets/menu/Voxel-Logo.png"));

		ImageView voxelLogo = new ImageView();
		voxelLogo.setImage(voxelLogoI);
		voxelLogo.setFitWidth(200);
		voxelLogo.setPreserveRatio(true);
		voxelLogo.setSmooth(true);
		voxelLogo.setCache(true);

		add(voxelLogo, 0, 0);

		userText = new Text("Username:");
		add(userText, 0, 1);

		userField = new TextField();
		add(userField, 0, 2);

		passText = new Text("Password:");
		add(passText, 0, 3);

		passField = new TextField();
		add(passField, 0, 4);

		login = new Button("Continue");
		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				userField.setEditable(false);
				passField.setEditable(false);
				loginProgress.setVisible(true);
				loginProgress.setProgress(-1);
				stage.hide();
				stage.setScene(new Scene(ui.getMainStage()));
				stage.centerOnScreen();
				stage.show();

			};
		});
		add(login, 0, 5);

		Text loginDis = new Text("ALPHA: Login Disabled, Click continue");
		loginDis.setFont(new Font(16));
		add(loginDis, 0, 6);
		loginProgress = new ProgressBar(0);
		loginProgress.setVisible(false);
		loginProgress.setMinWidth(200);
		add(loginProgress, 0, 6);

	}

}
