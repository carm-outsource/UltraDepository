package cc.carm.plugin.ultrabackpack.api;

import cc.carm.plugin.ultrabackpack.api.manager.UBUserManager;

public class UltraBackpackAPI {

	private static UBUserManager userManager;

	public static void initialize(UBUserManager userManager) {
		UltraBackpackAPI.userManager = userManager;
	}

	public static UBUserManager getUserManager() {
		return userManager;
	}
	
}
