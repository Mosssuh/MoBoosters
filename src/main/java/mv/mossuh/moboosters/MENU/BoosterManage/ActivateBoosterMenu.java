package mv.mossuh.moboosters.MENU.BoosterManage;

import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.MANAGER.HooksManager;
import mv.mossuh.moboosters.MENU.MenuItems;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.mocore.ENUMS.ResponseType;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import mv.mossuh.mocore.GUI.Layouts.GUILayout;
import mv.mossuh.mocore.ITEM.MoItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class ActivateBoosterMenu {

    public void open(Player player) {
        SingleGUI gui = new SingleGUI(3, "&aActivate Booster");
        gui.onGUIClick(l -> {
            l.setCancelled(true);
        });
        GUILayout layout = new GUILayout(
                19, 21, 23, 25,
                29, 33
        );
        gui.setLayout(layout);

        gui.setReservedItem(new GUIItem(MenuItems.blackPane()),
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                45, 46, 47, 48, 50, 51, 52, 53
        );
        gui.setReservedItem(new GUIItem(MenuItems.yellowPane()),
                9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 20, 22, 24, 26,
                27, 28, 30, 31, 32, 34, 35,
                36, 37, 38, 39, 40, 41, 42, 43, 44,
                49
        );

        gui.setItem(obtainIdentifier(player, gui), 19);

        player.openInventory(gui.getInventory());
    }


    private GUIItem obtainIdentifier(Player player, SingleGUI gui) {
        List<String> identifiers = Config.IDENTIFIERS.getIdentifiers();

        String identifier = identifiers.stream().findFirst().orElse(Config.DEFAULT_IDENTIFIER);

        GUIItem item = new GUIItem(MenuItems.book()
                .setName("&dIdentifier")
                .setLore(
                        "&r",
                        "&7Allows multiple boosters to use",
                        "&7the same applicator and boosted",
                        "&7type.",
                        "&r",
                        "&7Selected: &a" + identifier,
                        "&r",
                        "&eLeft-click to switch!",
                        "&eRight-click to type it!"
                )
        );
        item.setIdentifier(identifier);

        // Left click
        item.onItemClick(l -> {
            int index = identifiers.indexOf((String) item.getIdentifier());
            String next = identifiers.get((index + 1) % identifiers.size());

            item.setIdentifier(next);

            MoItemStack editor = item.getItemStackEditor();
            editor.setLore(
                    "&r",
                    "&7Allows multiple boosters to use",
                    "&7the same applicator and boosted",
                    "&7type.",
                    "&r",
                    "&7Selected: &a" + next,
                    "&r",
                    "&eLeft-click to switch!",
                    "&eRight-click to type it!"
            );

            gui.refreshGUI(player);
        });

        item.onConfirm((p, click, result) -> {
            String obtainedIdentifier = (String) result;

            if (!click.equals(ClickType.RIGHT))  {
                p.openInventory(gui.getInventory());
                return;
            }
            if (!Config.IDENTIFIERS.hasIdentifier(obtainedIdentifier)) {
                p.openInventory(gui.getInventory());
                return;
            }

            item.setIdentifier(obtainedIdentifier);

            MoItemStack editor = item.getItemStackEditor();
            editor.setLore(
                    "&r",
                    "&7Allows multiple boosters to use",
                    "&7the same applicator and boosted",
                    "&7type.",
                    "&r",
                    "&7Selected: &a" + obtainedIdentifier,
                    "&r",
                    "&eLeft-click to switch!",
                    "&eRight-click to type it!"
            );

            p.openInventory(gui.getInventory());
            gui.refreshGUI(player);
        }, ResponseType.TEXT);

        return item;
    }


    private GUIItem obtainBoosterType(Player player, SingleGUI gui) {
        List<BoosterType> boosters = BoosterType.list();

        BoosterType selectedType = boosters.stream().findFirst().orElse(BoosterType.PERSONAL);
        // If it is applied to a player or team
        String appliedTo = "";

        List<Object> data = new ArrayList<>();
        data.add(selectedType);
        data.add(appliedTo);

        GUIItem item = new GUIItem(MenuItems.book()
                .setName("&dIdentifier")
                .setLore(
                        "&r",
                        "&7Select the audience to which",
                        "&7the booster will be applied.",
                        "&r",
                        "&7Selected: &a" + selectedType.name(),
                        selectedType.equals(BoosterType.PERSONAL) ? "&7Player: &cNo selected!" : selectedType.equals(BoosterType.TEAM) ? "&7Team: &cNo selected" : null,
                        "&r",
                        "&eLeft-click to switch!",
                        !selectedType.equals(BoosterType.GLOBAL) ? "&r" : null,
                        selectedType.equals(BoosterType.PERSONAL) ? "Shift Left-click to type player name!" : selectedType.equals(BoosterType.TEAM) ? "&7Shift Left-click to type team name!" : null
                )
        );
        item.setIdentifier(data);

        item.onItemClick(l -> {
            ClickType type = l.getClickType();

            if (type.equals(ClickType.LEFT)) {
                if (!((item.getIdentifier()) instanceof List<?>)) return;
                List<Object> savedData = new ArrayList<>((List<?>) item.getItemListener());
                BoosterType savedType = (BoosterType) savedData.get(0);

                int index = boosters.indexOf(savedType);
                BoosterType next = boosters.get((index + 1) % boosters.size());

                MoItemStack editor = item.getItemStackEditor();
                editor.setLore(
                        "&r",
                        "&7Select the audience to which",
                        "&7the booster will be applied.",
                        "&r",
                        "&7Selected: &a" + next.name(),
                        next.equals(BoosterType.PERSONAL) ? "&7Player: &cNo selected!" : next.equals(BoosterType.TEAM) ? "&7Team: &cNo selected" : null,
                        "&r",
                        "&eLeft-click to switch!",
                        !next.equals(BoosterType.GLOBAL) ? "&r" : null,
                        next.equals(BoosterType.PERSONAL) ? "Shift Left-click to type player name!" : next.equals(BoosterType.TEAM) ? "&7Shift Left-click to type team name!" : null
                );

                gui.refreshGUI(player);
            } else if (type.equals(ClickType.SHIFT_LEFT)) {
                if (!((item.getIdentifier()) instanceof List<?>)) return;
                List<Object> savedData = new ArrayList<>((List<?>) item.getItemListener());
                BoosterType savedType = (BoosterType) savedData.get(0);

                if (savedType.equals(BoosterType.GLOBAL)) return;

                item.onConfirm((p, click, result) -> {
                    String name = (String) result;
                    if (savedType.equals(BoosterType.PERSONAL)) {
                        if (name == null) {
                            UtilString.get(Config.PREFIX + " &cThe player name isn't valid!").hex().sendMessage(p);
                            player.openInventory(gui.getInventory());
                            return;
                        };

                        Player toApplied = Bukkit.getPlayer(name);
                        if (toApplied == null || !toApplied.isOnline()) {
                            UtilString.get(Config.PREFIX + " &cThe player isn't valid or is offline!").hex().sendMessage(p);
                            player.openInventory(gui.getInventory());
                            return;
                        }

                        String playerName = toApplied.getName();
                        savedData.set(1, playerName);
                        MoItemStack editor = item.getItemStackEditor();
                        editor.setLore(
                                "&r",
                                "&7Select the audience to which",
                                "&7the booster will be applied.",
                                "&r",
                                "&7Selected: &a" + savedType,
                                "&7Player: &a" + playerName,
                                "&r",
                                "&eLeft-click to switch!",
                                "&r",
                                "Shift Left-click to type player name!"
                        );

                    } else if (savedType.equals(BoosterType.TEAM)) {
                        if (name == null) {
                            UtilString.get(Config.PREFIX + " &cThe team name isn't valid!").hex().sendMessage(p);
                            player.openInventory(gui.getInventory());
                            return;
                        };

                        if (!HooksManager.getTeamBoosterHook().isTeam(name)) {
                            UtilString.get(Config.PREFIX + " &cThe team don't exist!").hex().sendMessage(p);
                            player.openInventory(gui.getInventory());
                            return;
                        }

                        savedData.set(1, name);
                        MoItemStack editor = item.getItemStackEditor();
                        editor.setLore(
                                "&r",
                                "&7Select the audience to which",
                                "&7the booster will be applied.",
                                "&r",
                                "&7Selected: &a" + savedType,
                                "&7Team: &a" + name,
                                "&r",
                                "&eLeft-click to switch!",
                                "&r",
                                "Shift Left-click to type team name!"
                        );

                    }

                }, ResponseType.TEXT);
            }
        });
        return item;
    }
}
