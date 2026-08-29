package mv.mossuh.moboosters.COMMANDS;

import mv.mossuh.moboosters.DATA.Config.Messages;
import mv.mossuh.moboosters.MENU.ActiveBoosters.PlayerActiveBoostersMenu;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MenusCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            // /moboosters openmenu activeboosters <player>
            if (strings[0].equalsIgnoreCase("openmenu")) {
                if (UtilString.get("moboosters.openmenu.commands").hasPermission(sender)) {
                    if (strings.length > 1) {
                        if (strings[1].equalsIgnoreCase("activeboosters")) {
                            if (UtilString.get("moboosters.openmenu.activeboosters").hasPermission(sender)) {
                                OfflinePlayer player = null;
                                if (strings.length >= 3) {
                                    // If the command contains player's name
                                    player = Bukkit.getPlayer(strings[2]);
                                } else {
                                    // If the command don't contain player's name, shows the active booster of who executed it
                                    if (sender instanceof Player) {
                                        player = (Player) sender;
                                    }
                                }

                                if (player == null) {
                                    UtilString.get(Messages.INVALID_PLAYER).setPlaceholders(sender).sendMessage(sender);
                                    return;
                                }

                                UUID uuid = player.getUniqueId();
                                PlayerActiveBoostersMenu.openPersonal(uuid);
                            } else {
                                UtilString.get(Messages.NO_PERMISSION).setPlaceholders(sender).hex().sendMessage(sender);
                            }
                        }
                    } else {
                        UtilString.get("&r").hex().sendMessage(sender);
                        UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                        UtilString.get("&r").hex().sendMessage(sender);
                        UtilString.get("&b/moboosters openmenu activeboosters").hex().sendMessage(sender);
                        UtilString.get("&r").hex().sendMessage(sender);
                        UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                        UtilString.get("&r").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).setPlaceholders(sender).hex().sendMessage(sender);
                }
            }
        }
    }
}
