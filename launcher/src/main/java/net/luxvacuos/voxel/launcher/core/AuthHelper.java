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

package net.luxvacuos.voxel.launcher.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;

import net.luxvacuos.voxel.launcher.remote.StatusBoolean;

public class AuthHelper {

	private static Gson gson = new Gson();

	public static boolean login(String user, String pass) throws IOException {
		if (user == "" || user.equals(""))
			return false;
		URL url = new URL(LauncherVariables.API + "/auth/login/" + user + "/" + pass);
		URLConnection conn = url.openConnection();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StatusBoolean res = gson.fromJson(bufferedReader, StatusBoolean.class);
		return res.isStatus();
	}
}
