package mv.mossuh.moboosters.UTILITIES;

import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.EVENT.Applicators.JobsRebornBoost;
import mv.mossuh.moboosters.EVENT.Applicators.MoArmorsBoost;
import mv.mossuh.moboosters.EVENT.Applicators.MoPetsBoost;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.EVENT.Applicators.PlayerPointsBoost;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.EVENTS.PluginCheckerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.EVENT.Hooks.IslandBoosterListener;
import mv.mossuh.moboosters.MANAGER.BoosterDataManager;
import mv.mossuh.moboosters.EVENT.IslandDisband;
import mv.mossuh.moboosters.UTILITIES.Cooldown.BoosterCooldown;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.MoBoosters;

public class PluginChecker implements Listener {

    private static MoBoosters instance = MoBoosters.getInstance();

    @EventHandler
    public void onPluginChecker(PluginCheckerEvent event) {
        PluginType pluginType = event.getPluginType();

        switch (pluginType) {
            case PlaceholderAPI:
                new PAPI().register();
                UtilString.get(Config.PREFIX+" &aDetected "+pluginType.name()+", used as soft-depend.").hex().sendMessageInConsole();
                break;
            case SuperiorSkyblock2:
                instance.getServer().getPluginManager().registerEvents(new IslandBoosterListener(instance.getCentralBoosterListener()), instance);
                instance.getServer().getPluginManager().registerEvents(new IslandDisband(), instance);
                BoosterDataManager.registerDatabaseInMaps(BoosterType.SUPERIORSKYBLOCK2);
                BoosterCooldown.initializeActiveBoosters(BoosterType.SUPERIORSKYBLOCK2);
                UtilString.get(Config.PREFIX+" &aDetected "+pluginType.name()+", used as soft-depend. Enabling classes.").hex().sendMessageInConsole();
                break;
            case MoPets:
                BoostersAPI.registerApplicator(instance, new MoPetsBoost());
                UtilString.get(Config.PREFIX+" &aDetected "+pluginType.name()+", used as soft-depend.").hex().sendMessageInConsole();
                break;
            case MoArmors:
                BoostersAPI.registerApplicator(instance, new MoArmorsBoost());
                UtilString.get(Config.PREFIX+" &aDetected "+pluginType.name()+", used as soft-depend.").hex().sendMessageInConsole();
                break;
            case JobsReborn:
                BoostersAPI.registerApplicator(instance, new JobsRebornBoost());
                UtilString.get(Config.PREFIX+" &aDetected "+pluginType.name()+", used as soft-depend.").hex().sendMessageInConsole();
                break;
            case PlayerPoints:
                BoostersAPI.registerApplicator(instance, new PlayerPointsBoost());
                UtilString.get(Config.PREFIX+" &aDetected "+pluginType.name()+", used as soft-depend.").hex().sendMessageInConsole();
                break;
        }
    }
}
