package mv.mossuh.moboosters.COMMANDS;

import org.bukkit.command.CommandSender;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.DATA.Config.Messages;
import mv.mossuh.moboosters.UTILITIES.Enums.DebugType;
import mv.mossuh.moboosters.MANAGER.DebugManager;
import mv.mossuh.moboosters.UTILITIES.UtilString;

public class DebugCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("debug")) {
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    if (strings.length >= 2) {
                        String key = strings[1].toLowerCase();
                        DebugType type = DebugType.NONE;

                        switch (key){
                            case "applicators":
                            case "applicator":
                                // /moboosters debug applicators
                                type = DebugType.APPLICATORS;
                                break;
                            case "claim_booster":
                            case "claim_boosters":
                            case "claimbooster":
                            case "claimboosters":
                                // /moboosters debug claimbooster
                                type = DebugType.CLAIM_BOOSTER;
                                break;
                        }

                        if (type != DebugType.NONE) {
                            boolean status = DebugManager.changeStatus(type);
                            UtilString.get(Config.PREFIX + "&b" + type.name() + " &bDebug: &a" + status).hex().sendMessage(sender);
                        }
                    } else {
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters debug <debug type>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
