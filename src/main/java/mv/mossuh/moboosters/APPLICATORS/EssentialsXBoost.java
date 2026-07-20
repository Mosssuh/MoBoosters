package mv.mossuh.moboosters.APPLICATORS;

import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class EssentialsXBoost implements Listener {

    @EventHandler
    public void boost(UserBalanceUpdateEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String boosted = "economy";
        ApplicatorType applicator = ApplicatorType.ESSENTIALSX;

        BigDecimal oldBalance = event.getOldBalance();
        BigDecimal newBalance = event.getNewBalance();

        BigDecimal obtained = newBalance.subtract(oldBalance);

        if (obtained.compareTo(BigDecimal.ZERO) > 0) {
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
                BigDecimal multiplier = new BigDecimal(eventBoost);
                BigDecimal bonus = obtained.multiply(multiplier.subtract(BigDecimal.ONE));

                event.setNewBalance(newBalance.add(bonus));
            }
        }
    }
}
