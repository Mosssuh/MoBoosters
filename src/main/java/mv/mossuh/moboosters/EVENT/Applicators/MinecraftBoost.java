package mv.mossuh.moboosters.EVENT.Applicators;

import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MinecraftBoost extends EventApplicatorHook implements Listener {

    public MinecraftBoost() {
        super(ApplicatorType.MINECRAFT);
    }

    @EventHandler
    public void caughtFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (!(event.getCaught() instanceof Item)) return;

        Item caughtItem = (Item) event.getCaught();
        ItemStack stack = caughtItem.getItemStack();

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String boosted = "caught_fish";
        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        int earnings = (int) (stack.getAmount() * boost);
        if (earnings <= 0) return;
        ItemStack clone = stack.clone();
        clone.setAmount(earnings);
        caughtItem.setItemStack(clone);
    }

    @EventHandler
    public void logging(BlockBreakEvent event) {
        Block block = event.getBlock();
        String name = block.getType().name();

        if (!name.endsWith("LOG")) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String boosted = "logging";
        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        for (ItemStack stack : block.getDrops(player.getInventory().getItemInMainHand())) {
            if (stack == null || stack.getType() == Material.AIR) continue;

            int earnings = (int) (stack.getAmount() * boost);
            ItemStack clone = stack.clone();
            clone.setAmount(earnings);
            block.getWorld().dropItemNaturally(block.getLocation(), clone);
        }
    }

    @EventHandler
    public void miningOre(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockData data = block.getBlockData();
        String name = data.getMaterial().name();
        if (!name.endsWith("ORE")) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String boosted = "mining_ore";
        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        for (ItemStack stack : block.getDrops(player.getInventory().getItemInMainHand())) {
            if (stack == null || stack.getData() == null || stack.getData().getItemType().equals(Material.AIR)) continue;

            int earnings = (int) (stack.getAmount() * boost);
            if (earnings <= 0) return;
            ItemStack clone = stack.clone();
            clone.setAmount(earnings);
            block.getWorld().dropItemNaturally(block.getLocation(), clone);
        }
    }

    @EventHandler
    public void miningXP(BlockBreakEvent event) {
        String name = event.getBlock().getType().name();
        if (!name.endsWith("ORE")) return;

        Player player = event.getPlayer();

        String boosted = "mining_xp";
        double boost = resolveBoost(player.getUniqueId(), null, boosted);
        if (boost <= 0) return;

        event.setExpToDrop((int) (event.getExpToDrop() * (boost + 1)));
    }

    @EventHandler
    public void mobDrops(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null || event.getEntity() instanceof Player) return;

        String boosted = "mob_drops";
        double boost = resolveBoost(killer.getUniqueId(), null, boosted);
        if (boost <= 0) return;

        for (ItemStack stack : event.getDrops()) {
            if (stack == null || stack.getType() == Material.AIR) continue;
            int earnings = (int) (stack.getAmount() * boost);
            if (earnings <= 0) continue;
            ItemStack clone = stack.clone();
            clone.setAmount(earnings);
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), clone);
        }
    }

    @EventHandler
    public void mobExperience(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null || event.getEntity() instanceof Player) return;

        String boosted = "mob_experience";
        double boost = resolveBoost(killer.getUniqueId(), null, boosted);
        if (boost <= 0) return;

        event.setDroppedExp((int) (event.getDroppedExp() * (boost + 1)));
    }

    @EventHandler
    public void harvestingDrops(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getBlockData() instanceof Ageable)) return;

        Ageable ageable = (Ageable) block.getBlockData();
        if (ageable.getAge() < ageable.getMaximumAge()) return;

        Player player = event.getPlayer();

        String boosted = "harvesting_drops";
        double boost = resolveBoost(player.getUniqueId(), null, boosted);
        if (boost <= 0) return;

        for (ItemStack stack : block.getDrops(player.getInventory().getItemInMainHand())) {
            if (stack == null || stack.getType() == Material.AIR) continue;
            int earnings = (int) (stack.getAmount() * boost);
            if (earnings <= 0) continue;
            ItemStack clone = stack.clone();
            clone.setAmount(earnings);
            block.getWorld().dropItemNaturally(block.getLocation(), clone);
        }
    }

    @EventHandler
    public void meleeDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();

        String boosted = "melee_damage";
        double boost = resolveBoost(player.getUniqueId(), null, boosted);
        if (boost <= 0) return;

        event.setDamage(event.getDamage() * (boost + 1));
    }

    @EventHandler
    public void projectileDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile)) return;

        Projectile projectile = (Projectile) event.getDamager();
        if (!(projectile.getShooter() instanceof Player)) return;

        Player player = (Player) projectile.getShooter();

        String boosted = "projectile_damage";
        double boost = resolveBoost(player.getUniqueId(), null, boosted);
        if (boost <= 0) return;

        event.setDamage(event.getDamage() * (boost + 1));
    }
}
