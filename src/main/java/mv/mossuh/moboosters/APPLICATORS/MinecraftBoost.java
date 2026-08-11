package mv.mossuh.moboosters.APPLICATORS;

import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MinecraftBoost implements Listener {

    public void boost(PlayerFishEvent event) {

    }

    public void boost(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockData data = block.getBlockData();
        String name = data.getMaterial().name();
        if (!name.endsWith("_ORE")) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String boosted = "mining_ore";
        ApplicatorType applicator = ApplicatorType.MINECRAFT;
        double total = ActiveBoosterManager.getTotalBoostCached(uuid, applicator, boosted);
        PlayerApplyBoostEvent playerApplyBoostEvent = new PlayerApplyBoostEvent(uuid, total, applicator, boosted);
        Bukkit.getPluginManager().callEvent(playerApplyBoostEvent);

        if (playerApplyBoostEvent.isCancelled()) {
            return;
        }

        double eventBoost = playerApplyBoostEvent.getBoost();

        if (eventBoost > 0) {
            for (ItemStack stack : block.getDrops(player.getInventory().getItemInMainHand())) {
                if (stack == null || stack.getData() == null || stack.getData().getItemType().equals(Material.AIR)) continue;

                int earnings = (int) (stack.getAmount() * eventBoost);
                ItemStack clone = stack.clone();
                clone.setAmount(earnings);
                block.getWorld().dropItemNaturally(block.getLocation(), clone);
            }
        }
    }


    @EventHandler
    public void boost(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        double obtained = event.getAmount();
        String boosted = "experience";
        ApplicatorType applicator = ApplicatorType.MINECRAFT;
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
            event.setAmount((int) (obtained * boost));
        }
    }

}
