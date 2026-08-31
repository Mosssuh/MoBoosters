package mv.mossuh.moboosters.API;

import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.moboosters.MANAGER.BoosterManager;
import mv.mossuh.moboosters.MANAGER.HooksManager;
import mv.mossuh.moboosters.HOOK.ApplicatorHook;
import mv.mossuh.moboosters.HOOK.StateApplicatorHook;
import org.bukkit.plugin.java.JavaPlugin;

public class BoostersAPI {
    private static final BoosterManager boosterManager = new BoosterManager();
    public static BoosterManager getManager() {
        return boosterManager;
    }

    public static void registerApplicator(JavaPlugin plugin, ApplicatorHook... hooks) {
        for (ApplicatorHook h : hooks) {
            HooksManager.registerApplicator(plugin, h);
        }
    }

    public static boolean isStateApplicator(ApplicatorType applicator, String boosted) {
        StateApplicatorHook hook = HooksManager.getStateApplicator(applicator, boosted);
        return hook != null;
    }
    public static boolean isEventApplicator(ApplicatorType applicator) {
        EventApplicatorHook hook = HooksManager.getEventApplicator(applicator);
        return hook != null;
    }


}
