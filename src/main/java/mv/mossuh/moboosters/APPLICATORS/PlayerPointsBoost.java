package mv.mossuh.moboosters.APPLICATORS;

import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.black_ixx.playerpoints.event.PlayerPointsChangeEvent;
import org.black_ixx.playerpoints.models.TransactionType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;


public class PlayerPointsBoost implements Listener {

    @EventHandler
    public void boost(PlayerPointsChangeEvent event) {
        UUID uuid = event.getPlayerId();
        double obtained = event.getChange();
        String boosted = "points";
        ApplicatorType applicator = ApplicatorType.MINECRAFT;


        // Only multiplied if are earnings
        if (obtained <= 0) return;

        Map<BoosterType, Double> boosts = ActiveBoosterManager.getTotalBoostCached(uuid, applicator, boosted);
        double personal = boosts.getOrDefault(BoosterType.PERSONAL, 0.0);
        double global = boosts.getOrDefault(BoosterType.GLOBAL, 0.0);
        double superiorSkyblock2 = boosts.getOrDefault(BoosterType.SUPERIORSKYBLOCK2, 0.0);

        double total = personal+global+superiorSkyblock2;
        PlayerApplyBoostEvent playerApplyBoostEvent = new PlayerApplyBoostEvent(uuid, total, applicator, boosted);
        Bukkit.getPluginManager().callEvent(playerApplyBoostEvent);

        if (playerApplyBoostEvent.isCancelled()) {
            return;
        }

        double eventBoost = playerApplyBoostEvent.getBoost();
        if (eventBoost > 0) {
            UtilString.get(Config.PREFIX + " &c(" + applicator.name() + ")&8: &aApplying the &2x" + eventBoost + " &aboost to &2" + boosted)
                    .hex().sendMessageInConsole(DebugType.APPLICATORS);
            double boost = eventBoost + 1;
            event.setChange((int) (obtained * boost));
        }

    }
}
