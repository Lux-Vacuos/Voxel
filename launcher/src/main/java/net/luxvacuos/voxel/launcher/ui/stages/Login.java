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

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.luxvacuos.voxel.launcher.core.AuthHelper;
import net.luxvacuos.voxel.launcher.ui.MainUI;

public class Login extends GridPane {

	private Text userText;
	private TextField userField;
	private Text passText;
	private PasswordField passField;
	private Button login;
	private ProgressBar loginProgress;
	private Text message;

	public Login(Stage stage, MainUI ui) {

		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);

		Image voxelLogoI = new Image(getClass().getClassLoader().getResourceAsStream("assets/menu/Voxel-Logo.png"));

		ImageView voxelLogo = new ImageView();
		voxelLogo.setImage(voxelLogoI);
		voxelLogo.setFitWidth(200);
		voxelLogo.setPreserveRatio(true);
		voxelLogo.setSmooth(true);
		voxelLogo.setCache(true);

		add(voxelLogo, 0, 0);

		GridPane loginPane = new GridPane();
		loginPane.setHgap(10);
		loginPane.setVgap(10);
		loginPane.setAlignment(Pos.CENTER);

		userText = new Text("Username:");
		loginPane.add(userText, 0, 0);

		userField = new TextField();
		loginPane.add(userField, 0, 1);

		passText = new Text("Password:");
		loginPane.add(passText, 0, 2);

		passField = new PasswordField();
		loginPane.add(passField, 0, 3);
		add(loginPane, 0, 1);

		GridPane bottom = new GridPane();
		bottom.setHgap(10);
		bottom.setVgap(10);

		login = new Button("Login");
		login.setOnAction((event) -> {
			message.setText("");
			userField.setEditable(false);
			passField.setEditable(false);
			loginProgress.setVisible(true);
			loginProgress.setProgress(-1);
			new Thread(() -> {
				try {
					if (AuthHelper.login(userField.getText(), passField.getText()))
						Platform.runLater(() -> {
							stage.hide();
							stage.setScene(new Scene(ui.getMainStage()));
							stage.centerOnScreen();
							stage.show();
							ui.getMainStage().userName.setText("Welcome, " + userField.getText());
							passField.setText("");
						});
					else
						Platform.runLater(() -> {
							userField.setText("");
							passField.setText("");
							userField.setEditable(true);
							passField.setEditable(true);
							loginProgress.setVisible(false);
							loginProgress.setProgress(0);
							message.setText("Invalid credentials");
						});
				} catch (Exception e) {
					Platform.runLater(() -> {
						userField.setText("");
						passField.setText("");
						userField.setEditable(true);
						passField.setEditable(true);
						loginProgress.setVisible(false);
						loginProgress.setProgress(0);
						message.setText("No Internet connection");
					});
				}
			}).start();
		});
		bottom.add(login, 0, 0);

		Hyperlink link = new Hyperlink("Don't have an account?");
		link.setOnAction((event) -> {
			ui.getHostServices().showDocument("https://luxvacuos.net/forum/register.php");
		});

		bottom.add(link, 1, 0);
		add(bottom, 0, 2);

		loginProgress = new ProgressBar(0);
		loginProgress.setVisible(false);
		loginProgress.setMinWidth(200);

		message = new Text();
		message.setFont(new Font(16));

		add(loginProgress, 0, 3);
		add(message, 0, 4);

	}

}
