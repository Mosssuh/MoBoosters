package mv.mossuh.moboosters.COMMANDS;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.CONFIGS.Messages;

import java.util.ArrayList;
import java.util.List;

public class SetBoosterCommands {
    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("setbooster")) {
                if (UtilString.get("moboosters.admin").hasPermission(sender)) {
                    if (strings.length == 8) {
                        // Temporary Booster
                        // /moboosters setbooster <player> <identifier> <booster type> <applicator type> <boosted> <boost> <duration in seconds>
                        String playerName = strings[1];
                        String identifier = strings[2];
                        DurationType durationType = DurationType.TEMP;
                        BoosterType boosterType = UtilMethods.getBoosterType(strings[3]);
                        ApplicatorType applicatorType = UtilMethods.getApplicatorType(strings[4]);
                        String boosted = strings[5];
                        String boostAsString = strings[6];
                        String durationAsString = strings[7];

                        Player player = Bukkit.getPlayer(playerName);
                        if (!UtilMethods.isOnline(player)) { UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender); return; }
                        if (!Config.IDENTIFIERS.hasIdentifier(identifier)) { UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender); return; }
                        if (boosterType.equals(BoosterType.NONE)) { UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender); return; }
                        if (applicatorType.equals(ApplicatorType.NONE)) { UtilString.get(Messages.INVALID_APPLICATOR_TYPE).hex().sendMessage(sender); return; }
                        if (!UtilString.get(boostAsString).isNumeric()) { UtilString.get(Messages.INVALID_BOOST).hex().sendMessage(sender); return; }
                        if (!UtilString.get(durationAsString).isNumeric()) { UtilString.get(Messages.INVALID_DURATION).hex().sendMessage(sender); return; }

                        double boost = Double.parseDouble(boostAsString);
                        long duration = Math.round(Double.parseDouble(durationAsString));

                        //TemporaryBoost boosterBoost = new TemporaryBoost(boost, new BoosterDuration(duration));
                        BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                        Booster booster = UtilMethods.getBooster(boosterIdentifier, player);

                        //ActiveBooster oldBooster = BoostersAPI.getManager().getBooster(booster);
                        //TemporaryBoost oldBoost = oldBooster.getBoosts().getTemporary();

                        /*
                        ActivateTemporaryBoosterEvent boosterEvent = new ActivateTemporaryBoosterEvent(booster, boosterBoost, oldBoost);
                        Bukkit.getPluginManager().callEvent(boosterEvent);

                        if (boosterEvent.isCancelled()) { return; }

                        double newBoost = boosterEvent.getBoost().getBoost();
                        long newDuration = boosterEvent.getBoost().getDuration().getDuration();

                         */
                        ActiveBooster active = BoostersAPI.getManager().setTempBoost(booster, boost, duration);
                        if (!active.isValid()) { return; }

                        TemporaryBoost temp = active.getBoosts().getTemporary();
                        double newBoost = temp.getBoost();
                        long newDuration = temp.getDuration().getDuration();

                        String messageReceiver = UtilMethods.getTempBoosterStartMessage(boosterType);
                        String messageSender = Messages.SET_TEMP_BOOSTER_SENDER;

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%duration_type%", durationType.name()));
                        variables.add(new VariableArg("%boost%", newBoost+""));
                        String boostBase = UtilMethods.formatNumber(newBoost + 1, 10);
                        variables.add(new VariableArg("%boost_with_base%", boostBase));
                        variables.add(new VariableArg("%duration%", newDuration+""));
                        variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(newDuration, System.currentTimeMillis())));

                        if (identifier.equalsIgnoreCase("default")) {
                            UtilString.get(messageReceiver).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier).setVariables(variables).setPlaceholders(sender).sendMessage(player);
                        }
                        UtilString.get(messageSender).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier).setVariables(variables).setPlaceholders(sender).sendMessage(sender);
                    } else if (strings.length == 7) {
                        // Permanent Booster
                        // /moboosters setbooster <player> <identifier> <booster type> <applicator> <boosted> <boost>
                        String playerName = strings[1];
                        String identifier = strings[2];
                        DurationType durationType = DurationType.PERM;
                        BoosterType boosterType = UtilMethods.getBoosterType(strings[3]);
                        ApplicatorType applicatorType = UtilMethods.getApplicatorType(strings[4]);
                        String boosted = strings[5];
                        String boostAsString = strings[6];

                        Player player = Bukkit.getPlayer(playerName);
                        if (!UtilMethods.isOnline(player)) { UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender); return; }
                        if (!Config.IDENTIFIERS.hasIdentifier(identifier)) { UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender); return; }
                        if (boosterType.equals(BoosterType.NONE)) { UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender); return; }
                        if (applicatorType.equals(ApplicatorType.NONE)) { UtilString.get(Messages.INVALID_APPLICATOR_TYPE).hex().sendMessage(sender); return; }
                        if (!UtilString.get(boostAsString).isNumeric()) { UtilString.get(Messages.INVALID_BOOST).hex().sendMessage(sender); return; }

                        double boost = Double.parseDouble(boostAsString);

                        //PermanentBoost boosterBoost = new PermanentBoost(boost);
                        BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                        Booster booster = UtilMethods.getBooster(boosterIdentifier, player);

                        //ActiveBooster oldBooster = BoostersAPI.getManager().getBooster(booster);
                        //PermanentBoost oldBoost = oldBooster.getBoosts().getPermanent();

                        /*
                        ActivatePermanentBoosterEvent boosterEvent = new ActivatePermanentBoosterEvent(booster, boosterBoost, oldBoost);
                        Bukkit.getPluginManager().callEvent(boosterEvent);

                        if (boosterEvent.isCancelled()) { return; }

                        double newBoost = boosterEvent.getBoost().getBoost();
                        */
                        ActiveBooster active = BoostersAPI.getManager().setPermBoost(booster, boost);

                        if (!active.isValid()) { return; }

                        PermanentBoost perm = active.getBoosts().getPermanent();
                        double newBoost = perm.getBoost();

                        String messageReceiver = UtilMethods.getBoosterSetBoostMessage(boosterType);
                        String messageSender = Messages.SET_PERM_BOOSTER_SENDER;

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%duration_type%", durationType.name()));
                        variables.add(new VariableArg("%boost%", newBoost+""));
                        String boostBase = UtilMethods.formatNumber(newBoost + 1, 10);
                        variables.add(new VariableArg("%boost_with_base%", boostBase));

                        if (identifier.equalsIgnoreCase("default")) {
                            UtilString.get(messageReceiver).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier).setVariables(variables).setPlaceholders(sender).sendMessage(player);
                        }
                        UtilString.get(messageSender).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier).setVariables(variables).setPlaceholders(sender).sendMessage(sender);
                    } else {
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters setbooster <player> <identifier> <booster type> <applicator> <boosted> <boost> <duration in seconds>").hex().sendMessage(sender);
                        UtilString.get(Config.PREFIX+" &cUse: /moboosters setbooster <player> <identifier> <booster type> <applicator> <boosted> <boost>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
