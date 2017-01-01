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

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.luxvacuos.voxel.launcher.ui.MainUI;

public class Update extends GridPane {

	private ProgressBar updateBar;

	public Update(Stage stage, MainUI ui) {
		stage.initStyle(StageStyle.UNDECORATED);
		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(20));

		GridPane text = new GridPane();

		Text updateText = new Text("Updating Launcher");
		updateText.setFont(new Font(16));
		text.setAlignment(Pos.CENTER);
		text.add(updateText, 0, 0);
		add(text, 0, 0);
		updateBar = new ProgressBar();
		updateBar.setPrefWidth(200);
		add(updateBar, 0, 1);
		try {
			ui.getUpdateLauncher().downloadAndRun(stage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
