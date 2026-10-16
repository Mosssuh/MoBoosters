package mv.mossuh.moboosters.MENU;

import mv.mossuh.mocore.ITEM.MoItemStack;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Material;

public class MenuItems {

    public static MoItemStack yellowPane() {
        return ServerVersion.isAtLeast(ServerVersion.MC1_13) ?
                new MoItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1) :
                new MoItemStack(Material.valueOf("STAINED_GLASS_PANE"), (byte) 4, 1);
    }

    public static MoItemStack blackPane() {
        return ServerVersion.isAtLeast(ServerVersion.MC1_13) ?
                new MoItemStack(Material.BLACK_STAINED_GLASS_PANE, 1) :
                new MoItemStack(Material.valueOf("STAINED_GLASS_PANE"), (byte) 15, 1);
    }

    public static MoItemStack sunFlower() {
        return ServerVersion.isAtLeast(ServerVersion.MC1_13) ?
                new MoItemStack(Material.SUNFLOWER, 1) :
                new MoItemStack(Material.valueOf("DOUBLE_PLANT"), (byte) 0, 1);
    }

    public static MoItemStack beacon() {
        return new MoItemStack(Material.BEACON, 1);
    }

    public static MoItemStack book() {
        return new MoItemStack(Material.BOOK, 1);
    }
}
