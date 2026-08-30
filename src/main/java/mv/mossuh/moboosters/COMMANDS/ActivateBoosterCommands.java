package mv.mossuh.moboosters.COMMANDS;

import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.ACTIONS.ActionResult;
import mv.mossuh.moboosters.ACTIONS.Requirements;
import mv.mossuh.moboosters.ACTIONS.Rewards;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.Config.BoosterConfig;
import mv.mossuh.moboosters.DATA.Config.Booster.BoosterConfigs;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.MODEL.Booster.BoosterItem;
import mv.mossuh.moboosters.MANAGER.BoosterCreator;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.DATA.Config.Messages;

import java.util.UUID;

public class ActivateBoosterCommands {
    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("activate")) {
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        // /moboosters activate <code> <player> <actions (true/false)> <args...>
                        String boosterCode = strings[1];
                        String playerName = strings[2];
                        Player player = Bukkit.getPlayer(playerName);
                        boolean actions = UtilMethods.getBoolean(strings[3]);
                        if (!BoosterConfigs.exist(boosterCode)) {
                            UtilString.get(Messages.INVALID_CODE).hex().sendMessage(sender);
                            return;
                        }
                        if (!UtilMethods.isOnline(player)) {
                            UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }

                        UUID uuid = player.getUniqueId();

                        CommandArgs args = new CommandArgs(strings, 4);

                        BoosterConfig boosterConfig = BoosterConfigs.getBoosterConfig(boosterCode);
                        BoosterItem boosterItem = BoosterCreator.create(boosterConfig, uuid, args, 1);

                        DurationType durationType = boosterConfig.getDurationType();
                        BoosterIdentifier boosterIdentifier = boosterItem.getIdentifier();
                        BoosterType boosterType = boosterIdentifier.getBoosterType();


                        boolean cancelledMessage = false;
                        boolean cancelledBooster = false;

                        if (actions) {
                            Requirements requirements = new Requirements(player, boosterItem).addAllDefaultVariables()
                                    .addArgsVariables(args).check();
                            ActionResult result = requirements.getActionResult();
                            if (result.hasApprovedRewards()) {
                                Rewards rewards = new Rewards(result).executeDefault().check();
                                cancelledMessage = rewards.cancelMessage();
                                cancelledBooster = rewards.cancelBooster();
                            }
                        }

                        if (cancelledBooster) {
                            return;
                        }


                        Booster booster = Booster.getBooster(boosterIdentifier, uuid);

                        if (booster.isValid() && boosterItem.isBoost()) {
                            double boost = Double.parseDouble(boosterItem.getBoost());
                            if (durationType.equals(DurationType.TEMP)) {
                                if (boosterItem.hasDuration()) {
                                    long duration = Math.round((Double.parseDouble(boosterItem.getDuration())));
                                    BoostersAPI.getManager().setTempBoost(booster, boost, duration);
                                }
                            } else if (durationType.equals(DurationType.PERM)) {
                                BoostersAPI.getManager().setPermBoost(booster, boost);
                            }
                        }

                        if (!cancelledMessage) {
                            String messageReceiver = null;
                            if (durationType.equals(DurationType.PERM)) {
                                messageReceiver = UtilMethods.getBoosterAddBoostMessage(boosterType);
                            } else if (durationType.equals(DurationType.TEMP)) {
                                messageReceiver = UtilMethods.getTempBoosterStartMessage(boosterType);
                            }
                            UtilString.get(messageReceiver).setVariables(player).setVariables(boosterItem)
                                    .setArgs(args).setPlaceholders(player).hex().sendMessage(player);
                        }

                        UtilString.get(Messages.ACTIVATE_BOOSTER_SENDER).setVariables(player).setVariables(boosterItem).hex().sendMessage(sender);
                    } else {
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters activate <code> <player> <actions (true/false)> <args...>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
