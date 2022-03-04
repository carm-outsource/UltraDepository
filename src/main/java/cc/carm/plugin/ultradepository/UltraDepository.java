package cc.carm.plugin.ultradepository;

import cc.carm.plugin.ultradepository.manager.DepositoryManager;
import cc.carm.plugin.ultradepository.manager.EconomyManager;
import cc.carm.plugin.ultradepository.manager.UserManager;
import cc.carm.plugin.ultradepository.storage.DataStorage;

public class UltraDepository {

    public static DataStorage getStorage() {
        return Main.getInstance().getStorage();
    }

    public static Main getInstance() {
        return Main.getInstance();
    }

    public static UserManager getUserManager() {
        return Main.getInstance().getUserManager();
    }

    public static EconomyManager getEconomyManager() {
        return Main.getInstance().getEconomyManager();
    }

    public static DepositoryManager getDepositoryManager() {
        return Main.getInstance().getDepositoryManager();
    }
    
}
