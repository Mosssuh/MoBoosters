package mv.mossuh.moboosters.EVENT.Applicators.Rival;

import me.rivaldev.pickaxes.api.events.*;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.util.UUID;

public class RivalPickaxesBoost extends EventApplicatorHook {
    public RivalPickaxesBoost() {
        super(ApplicatorType.RIVALPICKAXES);
    }

    @EventHandler
    public void money(PickaxeMoneyReceiveEnchant event) {
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
    public void essence(PickaxeEssenceReceiveEnchantEvent event) {
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
    public void xp(PickaxeXPGainEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        String boosted = "xp";
        double obtained = event.getXP();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + 1;
        event.setXP((withBase * obtained));
    }

    @EventHandler
    public void fortune(PickaxeFortuneBoostEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        String boosted = "fortune";
        double obtained = event.getAmount();

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + 1;
        event.setAmount((withBase * obtained));
    }

    @EventHandler
    public void enchant_proc(PickaxeEnchantProcBoostEvent event) {
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
