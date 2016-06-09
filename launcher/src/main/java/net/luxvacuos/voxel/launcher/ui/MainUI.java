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

package net.luxvacuos.voxel.launcher.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.luxvacuos.voxel.launcher.core.LauncherVariables;
import net.luxvacuos.voxel.launcher.core.Updater;
import net.luxvacuos.voxel.launcher.ui.stages.Login;
import net.luxvacuos.voxel.launcher.ui.stages.Main;
import net.luxvacuos.voxel.launcher.util.Logger;

public class MainUI extends Application {

	private Login loginStage;
	private Main mainStage;
	private Updater updater;

	public static void main(String[] args) {
		Logger.log("Version: " + LauncherVariables.version);
		Logger.log("Dist Server: " + LauncherVariables.host);
		Logger.log("Auth Server: " + LauncherVariables.authHost);
		Thread.currentThread().setName("Voxel-Launcher");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		updater = new Updater();
		updater.getRemoteVersions();
		loginStage = new Login(stage, this);
		mainStage = new Main(stage, this);
		stage.setTitle("Voxel Launcher");
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("assets/icons/icon32.png")));
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("assets/icons/icon64.png")));
		stage.setScene(new Scene(loginStage));
		stage.setWidth(854);
		stage.setHeight(520);
		stage.setMinWidth(342);
		stage.setMinHeight(520);
		stage.centerOnScreen();
		stage.show();
	}

	public Login getLoginStage() {
		return loginStage;
	}

	public Main getMainStage() {
		return mainStage;
	}

	public Updater getUpdater() {
		return updater;
	}

}
