package mv.mossuh.moboosters.COMMANDS;

import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.MoBoosters;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItems;
import mv.mossuh.moboosters.NBT.NBTBooster;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.CONFIGS.Messages;

import java.util.List;

public class Commands implements CommandExecutor {
    /*
    /moboosters give <code> <amount> <player> <args...>
    /moboosters giveall <code> <amount> <args...>

    /moboosters activate <code> <player> <actions (true/false)> <args...>

    /moboosters setbooster <player> <identifier> <booster type> <applicator> <boosted> <boost> <duration in seconds> #Temporal booster
    /moboosters setbooster <player> <identifier> <booster type> <applicator> <boosted> <boost> #Permanent Booster

    /moboosters removebooster <identifier> <booster type> <applicator> <boosted> <duration type> [<player>/<islandName]
    /moboosters removebooster <identifier> <booster type> <duration type> [<player>/<islandName]

    /moboosters addtime <player> <identifier> <booster type> <applicator> <boosted> <duration in seconds>
    /moboosters removetime <player> <identifier> <booster type> <applicator> <boosted> <duration in seconds>

    /moboosters addboost <player> <duration type> <identifier> <booster type> <applicator> <boosted> <boost>
    /moboosters removeboost <player> <duration type> <identifier> <booster type> <applicator> <boosted> <boost>
     */


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length > 0) {
            switch (strings[0].toLowerCase()) {
                case "debug":
                    DebugCommands.onCommand(sender, strings);
                case "give":
                case "giveall":
                    GiveBoosterCommands.onCommand(sender, strings);
                    break;
                case "setbooster":
                    SetBoosterCommands.onCommand(sender, strings);
                    break;
                case "removebooster":
                    RemoveBoosterCommands.onCommand(sender, strings);
                    break;
                case "activate":
                    ActivateBoosterCommands.onCommand(sender, strings);
                    break;
                case "addtime":
                case "removetime":
                    TimeBoosterCommands.onCommand(sender, strings);
                    break;
                case "addboost":
                case "removeboost":
                    BoostBoosterCommands.onCommand(sender, strings);
                    break;
                case "reload":
                    MoBoosters.getInstance().getConfigs().reload();
                    UtilString.get(Config.PREFIX+" &aConfiguration reloaded!").hex().sendMessage(sender);
                    break;
                case "boosters":
                    List<String> boosters = Messages.BOOSTERS;
                    if (boosters != null && !boosters.isEmpty()) {
                        for (String line : boosters) {
                            UtilString.get(line).hex().setPlaceholders(sender).setDefaultNumberRandomVariable().setTimeFormatter()
                                    .setChangeOutputPlaceholder().setMathPlaceholder().sendMessage(sender);
                        }
                    }
                    break;
                case "check":
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoosterItem boosterItem = BoosterItems.get(player.getItemInHand());
                        ItemStack itemStack = boosterItem.getItemStack();
                        UtilString.get("&r").hex().sendMessage(sender);
                        UtilString.get("&8--------------------------------------").hex().sendMessage(sender);
                        UtilString.get("&r").hex().sendMessage(sender);
                        UtilString.get("&bMaterial: &7" + itemStack.getType().name() + ":" + itemStack.getData().getData()).hex().sendMessage(player);
                        if (boosterItem.isBoosterItem()) {
                            CommandArgs args = boosterItem.getArgs();
                            UtilString.get("&bCode: &7" + NBTBooster.getCode(itemStack)).hex().sendMessage(player);
                            UtilString.get("&bIdentifier: &7" + NBTBooster.getIdentifier(itemStack)).hex().sendMessage(player);
                            UtilString.get("&bBooster Type: &7" + NBTBooster.getBoosterType(itemStack).name()).hex().sendMessage(player);
                            UtilString.get("&bApplicator: &7" + NBTBooster.getApplicator(itemStack).name()).hex().sendMessage(player);
                            UtilString.get("&bBoosted: &7" + NBTBooster.getBoosted(itemStack)).hex().sendMessage(player);
                            UtilString.get("&bBoost: &7" + NBTBooster.getBoost(itemStack)).hex().sendMessage(player);
                            UtilString.get("&bDuration: &7" + NBTBooster.getDuration(itemStack)).hex().sendMessage(player);
                            if (args.hasArgs()) {
                                UtilString.get("&bArgs: &7" + String.join("&f, &7", args.getArgs())).hex().sendMessage(player);
                            }
                        }
                        UtilString.get("&r").hex().sendMessage(player);
                        UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                        UtilString.get("&r").hex().sendMessage(player);
                    }
            }
        } else {
            if (UtilMethods.hasPermission(sender, "moboosters.admin")) {
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/moboosters reload").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/moboosters give").hex().sendMessage(sender);
                UtilString.get("&b/moboosters giveall").hex().sendMessage(sender);
                UtilString.get("&b/moboosters activate").hex().sendMessage(sender);
                UtilString.get("&b/moboosters setbooster").hex().sendMessage(sender);
                UtilString.get("&b/moboosters removebooster").hex().sendMessage(sender);
                UtilString.get("&b/moboosters addboost").hex().sendMessage(sender);
                UtilString.get("&b/moboosters removeboost").hex().sendMessage(sender);
                UtilString.get("&b/moboosters addtime").hex().sendMessage(sender);
                UtilString.get("&b/moboosters removetime").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
            }
        }
        return false;
    }
}
