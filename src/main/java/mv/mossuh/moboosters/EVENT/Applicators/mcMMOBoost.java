package mv.mossuh.moboosters.EVENT.Applicators;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class mcMMOBoost extends EventApplicatorHook {
    public mcMMOBoost() {
        super(ApplicatorType.MCMMO);
    }

    @EventHandler
    public void xp(McMMOPlayerXpGainEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String boosted = "player_xp";
        float obtained = event.getRawXpGained();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + 1;
        event.setRawXpGained((float) (withBase * obtained));
    }
}
