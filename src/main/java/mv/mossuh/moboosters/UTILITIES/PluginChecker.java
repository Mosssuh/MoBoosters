package mv.mossuh.moboosters.UTILITIES;

import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.EVENT.Applicators.*;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.EVENT.Applicators.Rival.RivalHarvesterHoesBoost;
import mv.mossuh.moboosters.EVENT.Applicators.Rival.RivalPetsBoost;
import mv.mossuh.moboosters.EVENT.Applicators.Rival.RivalPickaxesBoost;
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

    private static final MoBoosters instance = MoBoosters.getInstance();

    @EventHandler
    public void onPluginChecker(PluginCheckerEvent event) {
        PluginType pluginType = event.getPluginType();

        String softDependMessage = Config.PREFIX+" &aDetected "+pluginType.name()+", used as soft-depend.";

        switch (pluginType) {
            case PlaceholderAPI:
                new PAPI().register();
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case SuperiorSkyblock2:
                instance.getServer().getPluginManager().registerEvents(new IslandBoosterListener(instance.getCentralBoosterListener()), instance);
                instance.getServer().getPluginManager().registerEvents(new IslandDisband(), instance);
                BoosterDataManager.registerDatabaseInMaps(BoosterType.SUPERIORSKYBLOCK2);
                BoosterCooldown.initializeActiveBoosters(BoosterType.SUPERIORSKYBLOCK2);
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case MoPets:
                BoostersAPI.registerApplicator(instance, new MoPetsBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case MoArmors:
                BoostersAPI.registerApplicator(instance, new MoArmorsBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case JobsReborn:
                BoostersAPI.registerApplicator(instance, new JobsRebornBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case PlayerPoints:
                BoostersAPI.registerApplicator(instance, new PlayerPointsBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case RivalHarvesterHoes:
                BoostersAPI.registerApplicator(instance, new RivalHarvesterHoesBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case RivalPets:
                BoostersAPI.registerApplicator(instance, new RivalPetsBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case RivalPickaxes:
                BoostersAPI.registerApplicator(instance, new RivalPickaxesBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case mcMMO:
                BoostersAPI.registerApplicator(instance, new mcMMOBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case AuraSkills:
                BoostersAPI.registerApplicator(instance, new AuraSkillsBoost());
                UtilString.get(softDependMessage).hex().sendMessageInConsole();
                break;
            case NextGens:

        }
    }
}
