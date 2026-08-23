package mv.mossuh.moboosters.EVENTS.Applicators;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.MODULES.EventApplicatorModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class JobsRebornBoost extends EventApplicatorModule implements Listener {

    public JobsRebornBoost() {
        super(ApplicatorType.JOBSREBORN);
    }

    @EventHandler
    public void money(JobsPaymentEvent event) {
        OfflinePlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String boosted = "money";
        CurrencyType currency = CurrencyType.MONEY;
        double obtained = event.get(currency);

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + 1;
        event.set(currency, (obtained*withBase));
    }

    @EventHandler
    public void exp(JobsPaymentEvent event) {
        OfflinePlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String boosted = "exp";
        CurrencyType currency = CurrencyType.EXP;
        double obtained = event.get(currency);

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + 1;
        event.set(currency, (obtained*withBase));
    }
}
