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

package net.luxvacuos.voxel.universal.util;

public class SimpleResource implements IResource {
	
	protected String domain = "";
	protected String path = "";
	
	public SimpleResource(String domain, String path) {
		if(domain == null) {
			throw new IllegalStateException("Resource cannot have null domain!");
		} else if(path == null) {
			throw new IllegalStateException("Resource cannot have null path!");
		}
		
		this.domain = domain;
		this.path = path;
	}
	
	public SimpleResource(String fullResource) {
		String[] split = fullResource.split(":");
		if(split.length != 2) {
			throw new IllegalStateException("Generated invalid Resource from fullResource string "+fullResource+"! Should only use one ':' to split the domain and path!");
		}
		
		this.domain = split[0];
		this.path = split[1];
	}

	@Override
	public String getResourceDomain() {
		return this.domain;
	}

	@Override
	public String getResourcePath() {
		return this.path;
	}
	
	@Override
	public String toString() {
		return this.domain + ":" + this.path;
	}

	@Override
	public int hashCode() {
		final int prime = 191;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleResource))
			return false;
		SimpleResource other = (SimpleResource) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	

}
