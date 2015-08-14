package io.github.guerra24.voxel.client.kernel.api;

/**
 * Mod
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.1 Build-54
 * @since 0.0.1 Build-54
 * @category API
 */
public abstract class Mod {
	/**
	 * Basic Mod Info
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract void preInit();

	/**
	 * Mod Textures
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract void init();

	/**
	 * Final Mod Load
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract void postInit();

	/**
	 * Mod Name
	 * 
	 * @return Name
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract String getName();

	/**
	 * Mod ID
	 * 
	 * @return ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public abstract int getID();
}
