package mv.mossuh.moboosters.EVENT.Applicators.Rival;

import me.rivaldev.rivalpets.api.events.RivalPetsXPGainEvent;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.util.UUID;

public class RivalPetsBoost extends EventApplicatorHook {
    public RivalPetsBoost() {
        super(ApplicatorType.RIVALPETS);
    }

    @EventHandler
    public void xp(RivalPetsXPGainEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        UUID uuid = player.getUniqueId();

        String boosted = "xp";
        double obtained = event.getBoost();
        double boost = resolveBoost(uuid, null, boosted);

        if (boost <= 0) return;

        double withBase = boost + obtained;
        event.setBoost(withBase);
    }
}
