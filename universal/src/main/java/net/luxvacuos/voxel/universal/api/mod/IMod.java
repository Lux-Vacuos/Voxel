package net.luxvacuos.voxel.universal.api.mod;

import net.luxvacuos.voxel.universal.api.IMoltenAPI;

public interface IMod {

	public void preInit(IMoltenAPI iMoltenAPI);

	public void init(IMoltenAPI iMoltenAPI);

	public void postInit(IMoltenAPI iMoltenAPI);

	public void dispose(IMoltenAPI iMoltenAPI);

}
