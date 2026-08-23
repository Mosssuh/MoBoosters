package mv.mossuh.moboosters.EVENTS.Applicators;

import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.MODULES.EventApplicatorModule;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.UUID;

public class EssentialsXBoost extends EventApplicatorModule implements Listener {

    public EssentialsXBoost() {
        super(ApplicatorType.ESSENTIALSX);
    }

    @EventHandler
    public void economy(UserBalanceUpdateEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String boosted = "economy";

        BigDecimal oldBalance = event.getOldBalance();
        BigDecimal newBalance = event.getNewBalance();

        BigDecimal obtained = newBalance.subtract(oldBalance);
        if (obtained.compareTo(BigDecimal.ZERO) >= 0) return;
        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        BigDecimal multiplier = new BigDecimal(boost);
        BigDecimal bonus = obtained.multiply(multiplier.subtract(BigDecimal.ONE));

        event.setNewBalance(newBalance.add(bonus));
    }
}
