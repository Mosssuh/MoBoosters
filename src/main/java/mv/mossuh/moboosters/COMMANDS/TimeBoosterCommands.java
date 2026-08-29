package mv.mossuh.moboosters.COMMANDS;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.InvalidBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.DATA.Config.Messages;

import java.util.ArrayList;
import java.util.List;

public class TimeBoosterCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("addtime")) {
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    // /moboosters addtime <player> <identifier> <booster type> <applicator> <boosted> <duration in seconds>
                    if (strings.length == 7) {
                        String playerName = strings[1];
                        String identifier = strings[2];
                        DurationType durationType = DurationType.TEMP;
                        BoosterType boosterType = UtilMethods.getBoosterType(strings[3]);
                        ApplicatorType applicatorType = ApplicatorType.convert(strings[4]);
                        String boosted = strings[5];
                        String durationString = strings[6];
                        long duration = 0;

                        Player player = Bukkit.getPlayer(playerName);
                        if (!UtilMethods.isOnline(player)) { UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender); return; }
                        if (!Config.IDENTIFIERS.hasIdentifier(identifier)) { UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender); return; }
                        if (boosterType.equals(BoosterType.NONE)) { UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender); return; }
                        if (applicatorType.equals(ApplicatorType.NONE)) { UtilString.get(Messages.INVALID_APPLICATOR_TYPE).hex().sendMessage(sender); return; }
                        if (!UtilMethods.isNumeric(durationString)) { UtilString.get(Messages.INVALID_DURATION).hex().sendMessage(sender); return; }

                        duration = Math.round(Double.parseDouble(durationString));

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%duration_type%", durationType.name()));
                        variables.add(new VariableArg("%duration%", duration+""));
                        variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(duration, System.currentTimeMillis())));

                        BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                        Booster booster = new InvalidBooster();
                        if (boosterType.equals(BoosterType.PERSONAL)) {
                            booster = new PersonalBooster(player.getUniqueId(), boosterIdentifier);
                        } else if (boosterType.equals(BoosterType.GLOBAL)) {
                            booster = new GlobalBooster(boosterIdentifier);
                        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2) && PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
                            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                            if (superiorPlayer.hasIsland()) {
                                booster = new SuperiorSkyblock2Booster(superiorPlayer.getIsland().getUniqueId(), boosterIdentifier);
                            }
                        }

                        ActiveBooster active = BoostersAPI.getManager().addTime(booster, duration);
                        if (!active.isValid()) return;

                        String messageReceiver = UtilMethods.getTempBoosterAddTimeMessage(boosterType);
                        if (boosterType.equals(BoosterType.PERSONAL)) {
                            UtilString.get(messageReceiver).setVariables(player).setVariables(boosterIdentifier)
                                    .setVariables(variables).setPlaceholders(player).hex().sendMessage(player);
                        } else if (boosterType.equals(BoosterType.GLOBAL)) {
                            UtilString.get(messageReceiver).setVariables(player).setVariables(boosterIdentifier)
                                    .setVariables(variables).setPlaceholders(player).hex().sendMessageToOnlinePlayers();
                        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2) && PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
                            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                            if (superiorPlayer.hasIsland()) {
                                List<SuperiorPlayer> islandPlayers = superiorPlayer.getIsland().getIslandMembers(true);
                                for (SuperiorPlayer sPlayer : islandPlayers) {
                                    if (sPlayer.isOnline()) {
                                        UtilString.get(messageReceiver).setVariables(player).setVariables(boosterIdentifier)
                                                .setVariables(variables).setPlaceholders(player).hex().sendMessage(sPlayer.getUniqueId());
                                    }
                                }
                            }
                        }

                    } else {
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters addtime <player> <identifier> <booster type> <applicator> <boosted> <duration in seconds>").hex().sendMessage(sender);
                    }
                }
            } else if (strings[0].equalsIgnoreCase("removetime")) {
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    // /moboosters removetime <player> <identifier> <booster type> <applicator> <boosted> <duration in seconds>
                    if (strings.length == 7) {
                        String playerName = strings[1];
                        String identifier = strings[2];
                        DurationType durationType = DurationType.TEMP;
                        BoosterType boosterType = UtilMethods.getBoosterType(strings[3]);
                        ApplicatorType applicatorType = ApplicatorType.convert(strings[4]);
                        String boosted = strings[5];
                        String durationString = strings[6];
                        long duration = 0;

                        Player player = Bukkit.getPlayer(playerName);
                        if (!UtilMethods.isOnline(player)) { UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender); return; }
                        if (!Config.IDENTIFIERS.hasIdentifier(identifier)) { UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender); return; }
                        if (boosterType.equals(BoosterType.NONE)) { UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender); return; }
                        if (applicatorType.equals(ApplicatorType.NONE)) { UtilString.get(Messages.INVALID_APPLICATOR_TYPE).hex().sendMessage(sender); return; }
                        if (!UtilMethods.isNumeric(durationString)) { UtilString.get(Messages.INVALID_DURATION).hex().sendMessage(sender); return; }

                        duration = Math.round(Double.parseDouble(durationString));

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%duration_type%", durationType.name()));
                        variables.add(new VariableArg("%duration%", duration+""));
                        variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(duration, System.currentTimeMillis())));

                        BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                        Booster booster = new InvalidBooster();
                        if (boosterType.equals(BoosterType.PERSONAL)) {
                            booster = new PersonalBooster(player.getUniqueId(), boosterIdentifier);
                        } else if (boosterType.equals(BoosterType.GLOBAL)) {
                            booster = new GlobalBooster(boosterIdentifier);
                        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2) && PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
                            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                            if (superiorPlayer.hasIsland()) {
                                booster = new SuperiorSkyblock2Booster(superiorPlayer.getIsland().getUniqueId(), boosterIdentifier);
                            }
                        }

                        ActiveBooster active = BoostersAPI.getManager().removeTime(booster, duration);
                        if (!active.isValid()) return;

                        String messageReceiver = UtilMethods.getTempBoosterRemoveTimeMessage(boosterType);
                        if (boosterType.equals(BoosterType.PERSONAL)) {
                            UtilString.get(messageReceiver).setVariables(player).setVariables(boosterIdentifier)
                                    .setVariables(variables).setPlaceholders(player).hex().sendMessage(player);
                        } else if (boosterType.equals(BoosterType.GLOBAL)) {
                            UtilString.get(messageReceiver).setVariables(player).setVariables(boosterIdentifier)
                                    .setVariables(variables).setPlaceholders(player).hex().sendMessageToOnlinePlayers();
                        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2) && PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
                            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                            if (superiorPlayer.hasIsland()) {
                                List<SuperiorPlayer> islandPlayers = superiorPlayer.getIsland().getIslandMembers(true);
                                for (SuperiorPlayer sPlayer : islandPlayers) {
                                    if (sPlayer.isOnline()) {
                                        UtilString.get(messageReceiver).setVariables(player).setVariables(boosterIdentifier)
                                                .setVariables(variables).setPlaceholders(player).hex().sendMessage(sPlayer.getUniqueId());
                                    }
                                }
                            }
                        }
                    } else {
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters removetime <player> <identifier> <booster type> <applicator> <boosted> <duration in seconds>").hex().sendMessage(sender);
                    }
                }
            }
        }
    }
}
