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

public class BoostBoosterCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("addboost")) {
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    if (strings.length == 8) {
                        // /moboosters addboost <player> <duration type> <identifier> <booster type> <applicator> <boosted> <boost>
                        String playerName = strings[1];
                        DurationType durationType = UtilMethods.getDurationType(strings[2]);
                        String identifier = strings[3];
                        BoosterType boosterType = UtilMethods.getBoosterType(strings[4]);
                        ApplicatorType applicatorType = ApplicatorType.convert(strings[5]);
                        String boosted = strings[6];
                        String boostString = strings[7];
                        double boost = 0;

                        Player player = Bukkit.getPlayer(playerName);
                        if (!UtilMethods.isOnline(player)) { UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender); return; }
                        if (durationType.equals(DurationType.NONE)) { UtilString.get(Messages.INVALID_DURATION_TYPE).hex().sendMessage(sender); }
                        if (!Config.IDENTIFIERS.hasIdentifier(identifier)) { UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender); return; }
                        if (boosterType.equals(BoosterType.NONE)) { UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender); return; }
                        if (applicatorType.equals(ApplicatorType.NONE)) { UtilString.get(Messages.INVALID_APPLICATOR_TYPE).hex().sendMessage(sender); return; }
                        if (!UtilMethods.isNumeric(boostString)) { UtilString.get(Messages.INVALID_DURATION).hex().sendMessage(sender); return; }

                        boost = Double.parseDouble(boostString);

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%duration_type%", durationType.name()));
                        variables.add(new VariableArg("%boost%", boost+""));
                        String boostBase = UtilMethods.formatNumber(boost + 1, 10);
                        variables.add(new VariableArg("%boost_with_base%", boostBase));

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

                        ActiveBooster active = BoostersAPI.getManager().addBoost(durationType, booster, boost);
                        if (!active.isValid()) return;

                        String messageReceiver = UtilMethods.getBoosterAddBoostMessage(boosterType);
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
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters addboost <player> <duration type> <identifier> <booster type> <applicator> <boosted> <boost>").hex().sendMessage(sender);
                    }
                }
            } else if (strings[0].equalsIgnoreCase("removeboost")) {
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    if (strings.length == 8) {
                        // /moboosters removeboost <player> <duration type> <identifier> <booster type> <applicator> <boosted> <boost>
                        String playerName = strings[1];
                        DurationType durationType = UtilMethods.getDurationType(strings[2]);
                        String identifier = strings[3];
                        BoosterType boosterType = UtilMethods.getBoosterType(strings[4]);
                        ApplicatorType applicatorType = ApplicatorType.convert(strings[5]);
                        String boosted = strings[6];
                        String boostString = strings[7];
                        double boost = 0;

                        Player player = Bukkit.getPlayer(playerName);
                        if (!UtilMethods.isOnline(player)) { UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender); return; }
                        if (durationType.equals(DurationType.NONE)) { UtilString.get(Messages.INVALID_DURATION_TYPE).hex().sendMessage(sender); }
                        if (!Config.IDENTIFIERS.hasIdentifier(identifier)) { UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender); return; }
                        if (boosterType.equals(BoosterType.NONE)) { UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender); return; }
                        if (applicatorType.equals(ApplicatorType.NONE)) { UtilString.get(Messages.INVALID_APPLICATOR_TYPE).hex().sendMessage(sender); return; }
                        if (!UtilMethods.isNumeric(boostString)) { UtilString.get(Messages.INVALID_DURATION).hex().sendMessage(sender); return; }

                        boost = Double.parseDouble(boostString);

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%duration_type%", durationType.name()));
                        variables.add(new VariableArg("%boost%", boost+""));
                        String boostBase = UtilMethods.formatNumber(boost + 1, 10);
                        variables.add(new VariableArg("%boost_with_base%", boostBase));


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

                        ActiveBooster active = BoostersAPI.getManager().removeBoost(durationType, booster, boost);
                        if (!active.isValid()) return;

                        String messageReceiver = UtilMethods.getBoosterRemoveBoostMessage(boosterType);
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
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters removeboost <player> <duration type> <identifier> <booster type> <applicator> <boosted> <boost>").hex().sendMessage(sender);
                    }
                }
            }
        }
    }
}
