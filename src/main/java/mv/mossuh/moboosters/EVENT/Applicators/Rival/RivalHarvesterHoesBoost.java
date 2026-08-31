package mv.mossuh.moboosters.EVENT.Applicators.Rival;

import me.rivaldev.harvesterhoes.api.events.*;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class RivalHarvesterHoesBoost extends EventApplicatorHook {
    public RivalHarvesterHoesBoost() {
        super(ApplicatorType.RIVALHARVESTERHOES);
    }

    @EventHandler
    public void money(HoeMoneyReceiveEnchant event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        String boosted = "money";
        double obtained = event.getBoost();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + obtained;
        event.setBoost(withBase);
    }

    @EventHandler
    public void essence(HoeEssenceReceiveEnchantEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        String boosted = "essence";
        double obtained = event.getBoost();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + obtained;
        event.setBoost(withBase);
    }

    @EventHandler
    public void crop(HoeCropBoostEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        String boosted = "crop";
        double obtained = event.getBoost();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + obtained;
        event.setBoost(withBase);
    }

    @EventHandler
    public void xp(HoeXPGainEvent event) {
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

    @EventHandler
    public void enchant_proc(HoeEnchantProcBoostEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        String boosted = "enchant_proc";
        double obtained = event.getBoost();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + obtained;
        event.setBoost(withBase);
    }


}
