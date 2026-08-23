package mv.mossuh.moboosters.API;

import mv.mossuh.moboosters.MANAGERS.BoosterManager;
import mv.mossuh.moboosters.MANAGERS.ModulesManager;
import mv.mossuh.moboosters.MODULES.ApplicatorModule;
import org.bukkit.plugin.java.JavaPlugin;

public class BoostersAPI {
    private static final BoosterManager boosterManager = new BoosterManager();
    public static BoosterManager getManager() {
        return boosterManager;
    }

    public static void registerApplicator(JavaPlugin plugin, ApplicatorModule... module) {
        for (ApplicatorModule m : module) {
            ModulesManager.registerApplicator(plugin, m);
        }
    }
}
