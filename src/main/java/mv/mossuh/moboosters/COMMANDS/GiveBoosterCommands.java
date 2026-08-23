package mv.mossuh.moboosters.COMMANDS;

import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfigs;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItems;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.CONFIGS.Messages;
import mv.mossuh.moboosters.NBT.NBTBooster;

public class GiveBoosterCommands {
    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("give")) {
                // /moboosters give <code> <amount> <player> <args...>
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String itemCode = strings[1];
                        String itemAmount = strings[2];
                        String itemPlayer = strings[3];
                        Player player = Bukkit.getPlayer(itemPlayer);
                        if (!BoosterConfigs.exist(itemCode)) {
                            UtilString.get(Messages.INVALID_CODE).hex().sendMessage(sender);
                            return;
                        }
                        if (!UtilString.get(itemAmount).isNumeric()) {
                            UtilString.get(Messages.INVALID_AMOUNT).hex().sendMessage(sender);
                            return;
                        }
                        if (!UtilMethods.isOnline(player)) {
                            UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }


                        int amount = Integer.parseInt(itemAmount);
                        CommandArgs args = new CommandArgs(strings, 4);
                        String argsAsString = args.getArgsAsString();
                        BoosterItem boosterItem = BoosterItems.create(itemCode, player.getUniqueId(), args, amount);
                        ItemStack itemStack = boosterItem.getItemStack();
                        NBTBooster.setArgs(itemStack, argsAsString);

                        player.getInventory().addItem(itemStack);

                        UtilString.get(Messages.GIVE_BOOSTER_ITEM_SENDER).replaceString("%amount%", itemAmount).setVariables(player).setVariables(boosterItem)
                                .setArgs(args).setPlaceholders(player).hex().sendMessage(sender);
                        UtilString.get(Messages.GIVE_BOOSTER_ITEM_RECEIVER).replaceString("%amount%", itemAmount).setVariables(player).setVariables(boosterItem)
                                .setArgs(args).setPlaceholders(player).hex().sendMessage(player);
                    } else {
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters give <code> <amount> <player> <args...>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("giveall")) {
                // /moboosters giveall <code> <amount>
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    if (strings.length >= 3) {
                        String itemCode = strings[1];
                        String itemAmount = strings[2];
                        if (!BoosterConfigs.exist(itemCode)) {
                            UtilString.get(Messages.INVALID_CODE).hex().sendMessage(sender);
                            return;
                        }
                        if (!UtilString.get(itemAmount).isNumeric()) {
                            UtilString.get(Messages.INVALID_AMOUNT).hex().sendMessage(sender);
                            return;
                        }

                        int amount = Integer.parseInt(itemAmount);
                        CommandArgs args = new CommandArgs(strings, 3);
                        String argsAsString = args.getArgsAsString();

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            BoosterItem boosterItem = BoosterItems.create(itemCode, player.getUniqueId(), args, amount);
                            ItemStack itemStack = boosterItem.getItemStack();
                            NBTBooster.setArgs(itemStack, argsAsString);
                            player.getInventory().addItem(itemStack);

                            UtilString.get(Messages.GIVE_BOOSTER_ITEM_RECEIVER).replaceString("%amount%", itemAmount).setVariables(player).setVariables(boosterItem)
                                    .setArgs(args).setPlaceholders(player).hex().sendMessage(player);
                        }
                        UtilString.get(Messages.GIVE_ALL_BOOSTER_ITEM_SENDER).replaceString("%code%", itemCode).replaceString("%amount%", itemAmount)
                                .setArgs(args).hex().sendMessage(sender);
                    } else {
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters giveall <code> <amount> <args...>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
