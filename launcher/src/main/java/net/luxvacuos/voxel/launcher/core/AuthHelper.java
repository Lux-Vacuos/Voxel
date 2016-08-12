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

package net.luxvacuos.voxel.launcher.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AuthHelper {

	public static boolean login(String user, String pass) throws IOException {
		URL url = new URL(
				LauncherVariables.authHost + "/forum/getdata.php?option=login&user=" + user + "&pass=" + pass);
		URLConnection conn = url.openConnection();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		return bufferedReader.readLine().equals("login: true");
	}
}
