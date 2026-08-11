package mv.mossuh.moboosters.APPLICATORS;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class JobsRebornBoost implements Listener {

    @EventHandler
    public void boostMoney(JobsPaymentEvent event) {
        OfflinePlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String boosted = "money";
        ApplicatorType applicator = ApplicatorType.JOBSREBORN;
        CurrencyType currency = CurrencyType.MONEY;
        double obtained = event.get(currency);

        double total = ActiveBoosterManager.getTotalBoostCached(uuid, applicator, boosted);
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
            event.set(currency, (obtained*boost));
        }
    }

    @EventHandler
    public void boostExp(JobsPaymentEvent event) {
        OfflinePlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String boosted = "exp";
        ApplicatorType applicator = ApplicatorType.JOBSREBORN;
        CurrencyType currency = CurrencyType.EXP;
        double obtained = event.get(currency);

        double total = ActiveBoosterManager.getTotalBoostCached(uuid, applicator, boosted);
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
            event.set(currency, (obtained*boost));
        }
    }
}
