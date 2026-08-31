package mv.mossuh.moboosters.EVENT.Applicators;

import dev.aurelium.auraskills.api.event.skill.XpGainEvent;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;


import java.util.UUID;

public class AuraSkillsBoost extends EventApplicatorHook {
    public AuraSkillsBoost() {
        super(ApplicatorType.AURASKILLS);
    }

    @EventHandler
    public void xp(XpGainEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String boosted = "xp";
        double obtained = event.getAmount();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + 1;
        event.setAmount((withBase * obtained));
    }
}
