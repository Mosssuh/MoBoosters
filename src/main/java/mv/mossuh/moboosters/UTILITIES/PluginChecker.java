package mv.mossuh.moboosters.UTILITIES;

import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.EVENTS.PluginCheckerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.EVENTS.IslandBoosterModuleEvent;
import mv.mossuh.moboosters.DATA.BoosterDataManager;
import mv.mossuh.moboosters.EVENTS.IslandDisband;
import mv.mossuh.moboosters.COOLDOWN.BoosterCooldown;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.PAPI;
import mv.mossuh.moboosters.MoBoosters;

public class PluginChecker implements Listener {

    private static MoBoosters instance = MoBoosters.getInstance();

    @EventHandler
    public void onPluginChecker(PluginCheckerEvent event) {
        PluginType pluginType = event.getPluginType();

        switch (pluginType) {
            case PlaceholderAPI:
                new PAPI().register();
                UtilString.get(Config.PREFIX+" &aDetected PlaceholderAPI, used as soft-depend.").hex().sendMessageInConsole();
                break;
            case SuperiorSkyblock2:
                instance.getServer().getPluginManager().registerEvents(new IslandBoosterModuleEvent(), instance);
                instance.getServer().getPluginManager().registerEvents(new IslandDisband(), instance);
                BoosterDataManager.registerDatabaseInMaps(BoosterType.SUPERIORSKYBLOCK2);
                BoosterCooldown.initializeActiveBoosters(BoosterType.SUPERIORSKYBLOCK2);
                UtilString.get(Config.PREFIX+" &aDetected SuperiorSkyblock2, used as soft-depend. Enabling classes.").hex().sendMessageInConsole();
                break;
        }
    }
}
