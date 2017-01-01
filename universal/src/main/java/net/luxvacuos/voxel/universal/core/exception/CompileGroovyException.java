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

package net.luxvacuos.voxel.universal.core.exception;

public class CompileGroovyException extends RuntimeException {

	private static final long serialVersionUID = 4761096455473153214L;

	public CompileGroovyException() {
		super();
	}

	public CompileGroovyException(String error) {
		super(error);
	}

	public CompileGroovyException(Exception e) {
		super(e);
	}

	public CompileGroovyException(Throwable cause) {
		super(cause);
	}

	public CompileGroovyException(String message, Throwable cause) {
		super(message, cause);
	}

}
