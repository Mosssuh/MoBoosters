package mv.mossuh.moboosters.MENUS.ActiveBoosters;

import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.BOOSTERS.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.BOOSTERS.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.Duration.Boosts;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.mocore.GUI.Holders.PaginatedGUI;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import mv.mossuh.mocore.GUI.Layouts.GUILayout;
import mv.mossuh.mocore.GUI.PaginatedUtilities.PaginatedInventoryConfiguration;
import mv.mossuh.mocore.ITEM.MoItemStack;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerActiveBoostersMenu {

    public static PaginatedGUI openPersonal(UUID uuid) {
        PaginatedInventoryConfiguration configuration = createConfiguration();
        PaginatedGUI gui = new PaginatedGUI("&aActive Boosters", configuration);

        GUILayout layout = new GUILayout(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        );
        gui.setLayout(layout);

        ItemStack itemStack = null;
        if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        } else {
            itemStack = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 0, (byte) 15);
        }

        MoItemStack crystal = new MoItemStack(itemStack);
        crystal.addFlag(ItemFlag.HIDE_ATTRIBUTES);
        crystal.setName("&r");
        GUIItem panel = new GUIItem(crystal);
        gui.setReservedItem(panel,
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 26,
                27, 35,
                36, 44,
                45, 46, 47, 49, 51, 52, 53
        );

        gui.onGUIClick(e -> {
            e.setCancelled(true);
        });

        Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            UtilString.get(Config.PREFIX + " &eError opening inventory to player with UUID: " + uuid).hex().sendMessageInConsole();
            return gui;
        }

        addGUIBoosters(uuid, gui);

        gui.setRefreshInterval(20);
        gui.startAutoRefresh(player, () -> {
            updateGUIBoosters(gui);
        });
        player.openInventory(gui.getInventory());

        return gui;
    }

    private static void updateGUIBoosters(PaginatedGUI gui) {
        List<GUIItem> current = gui.getCurrentPageItems();

        for (GUIItem item : current) {
            Object object = item.getIdentifier();
            if (!(object instanceof ActiveBooster)) continue;
            ActiveBooster ab = (ActiveBooster) object;
            Booster booster = ab.getBooster();

            if (!ab.isActive()) {
                gui.removeItem(item);
            };

            Boosts boosts = ab.getBoosts();

            PermanentBoost perm = boosts.getPermanent();
            TemporaryBoost temp = boosts.getTemporary();

            BoosterIdentifier identifier = booster.getIdentifier();

            MoItemStack boosterItem = new MoItemStack(Material.PAPER, 1);
            boosterItem.setName("&aPersonal's Booster");
            boosterItem.setLore(
                    "&r",
                    "&7Identifier: &a" + identifier.getIdentifier(),
                    "&7Type: &a" + identifier.getBoosterType().name(),
                    "&7Applicator: &a" + identifier.getApplicatorType().name(),
                    "&7Boosted: &a" + identifier.getBoosted(),
                    "&r",
                    "&7Permanent: &ax" + perm.getBoost(),
                    "&7Temporary: &ax" + temp.getBoost()
                            + " (" + temp.getDuration().getRemainingTimeFormatted() + ")",
                    "&r",
                    "&7Total Boost: " + boosts.getTotalBoost()
            );

            item.setItemStack(boosterItem);
        }
    }

    private static void addGUIBoosters(UUID uuid, PaginatedGUI gui) {
        List<ActiveBooster> actives = BoostersAPI.getManager().getBoosters(BoosterType.PERSONAL);
        for (int i = actives.size() - 1; i >= 0; i--) {
            ActiveBooster ab = actives.get(i);
            Booster booster = ab.getBooster();
            if (!booster.getUUID().equals(uuid)) continue;
            Boosts boosts = ab.getBoosts();
            PermanentBoost perm = boosts.getPermanent();
            TemporaryBoost temp = boosts.getTemporary();

            if (!ab.isActive()) continue;

            BoosterIdentifier identifier = booster.getIdentifier();
            MoItemStack boosterItem = new MoItemStack(Material.PAPER, 1);
            boosterItem.setName("&aPersonal's Booster");
            boosterItem.setLore(
                    "&r",
                    "&7Identifier: &a" + identifier.getIdentifier(),
                    "&7Type: &a" + identifier.getBoosterType().name(),
                    "&7Applicator: &a" + identifier.getApplicatorType().name(),
                    "&7Boosted: &a" + identifier.getBoosted(),
                    "&r",
                    "&7Permanent: &ax" + perm.getBoost(),
                    "&7Temporary: &ax" + temp.getBoost() + " (" + temp.getDuration().getRemainingTimeFormatted() + ")",
                    "&r",
                    "&7Total Boost: " + boosts.getTotalBoost()
            );

            GUIItem item = new GUIItem(boosterItem);
            item.setIdentifier(ab);
            gui.addItem(item);
        }
    }


    private static @NotNull PaginatedInventoryConfiguration createConfiguration() {
        PaginatedInventoryConfiguration configuration = new PaginatedInventoryConfiguration();
        MoItemStack backItem = new MoItemStack(Material.ARROW, 1);
        backItem.setName("&cPrevious page");
        MoItemStack nextPage = new MoItemStack(Material.ARROW, 1);
        nextPage.setName("&aNext page");

        configuration.setBackItem(backItem);
        configuration.setNextItem(nextPage);
        configuration.setBackItemSlot(48);
        configuration.setNextItemSlot(50);
        return configuration;
    }
}
