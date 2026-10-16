package mv.mossuh.moboosters.MENU.BoosterManage;

import mv.mossuh.moboosters.MENU.MenuItems;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import mv.mossuh.mocore.GUI.Layouts.GUILayout;
import org.bukkit.entity.Player;

public class BoosterMainMenu {

    public void open(Player player) {
        SingleGUI gui = new SingleGUI(3, "&aBooster Manage");
        GUILayout layout = new GUILayout(
                11, 15
        );
        gui.setLayout(layout);

        gui.setReservedItem(new GUIItem(MenuItems.blackPane()),
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26
        );
        gui.setReservedItem(new GUIItem(MenuItems.yellowPane()),
                10, 12, 13, 14, 16
        );

        GUIItem activeBooster = new GUIItem(
                MenuItems.sunFlower().setName("&aActive Booster")
                        .setLore(
                                "&r",
                                "&7Click in this option to create",
                                "&7a new active booster."
                        )
        );
        GUIItem manageBoosters = new GUIItem(
                MenuItems.beacon().setName("&aManage Boosters")
                        .setLore(
                                "&r",
                                "&7Click to see all the active",
                                "&7boosters and manage them"
                        )
        );

        gui.setItem(activeBooster, 11);
        gui.setItem(manageBoosters, 15);

        player.openInventory(gui.getInventory());
    }
}
