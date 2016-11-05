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

package net.luxvacuos.voxel.client.core.exception;

import net.luxvacuos.igl.Logger;

public class LoadTextureException extends RuntimeException {

	private static final long serialVersionUID = 960750507253812538L;

	public LoadTextureException(Exception cause) {
		super(cause);
	}

	public LoadTextureException(String texture, Exception cause) {
		super(cause);
		Logger.error("Couldn' load texture file " + texture);
	}

}
