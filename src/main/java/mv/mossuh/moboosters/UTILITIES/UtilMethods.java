package mv.mossuh.moboosters.UTILITIES;

import mv.mossuh.moboosters.MANAGER.HooksManager;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.MODEL.Booster.BoosterItem;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.DATA.Config.Messages;

import java.text.NumberFormat;
import java.util.*;

public class UtilMethods {

    public static String formatNumber(double number, int max_decimal) {
        Locale locale = new Locale("en", "US");

        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(max_decimal);
        numberFormat.setGroupingUsed(false);

        return numberFormat.format(number).replace("$", "");
    }

    public static boolean isNumeric(String string) {
        if (string != null) {
            try {
                Double.parseDouble(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public static double transformChance(UUID uuid, String chance) {
        chance = UtilString.get(chance).setTimeFormatter().setPlaceholders(uuid).apply();
        double chanceFormat = 100 * 100;
        if (UtilString.get(chance).isNumeric()) {
            chanceFormat = Double.parseDouble(chance) * 100;
        }
        return 10000 - chanceFormat;
    }

    public static double randomNumber() {
        double lowest = 1.000000000000000F;
        double highest = 10000.000000000000000F;
        return Math.floor(Math.random() * (highest - lowest + 1) + lowest);
    }

    public static double random(double min, double max) {
        try {
            Random rand = new Random();
            return (rand.nextInt((int) ((max - min) + 1)) + min);
        } catch (Exception ignored) {

        }
        return 0;
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(permission)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean isOnline(Player player) {
        if (player != null) {
            return player.isOnline();
        }
        return false;
    }


    public static long showCooldown(long cooldownInSeconds, long startedTimeInMilliSeconds) {
        long currentTimeInMilliSeconds = System.currentTimeMillis();
        long wait = currentTimeInMilliSeconds - startedTimeInMilliSeconds;
        long waitDiv = wait / 1000;
        long seconds = cooldownInSeconds - waitDiv;

        long durationInMilliSeconds = cooldownInSeconds * 1000;

        if (((startedTimeInMilliSeconds + durationInMilliSeconds) > currentTimeInMilliSeconds) && (startedTimeInMilliSeconds != 0)) {
            return seconds;
        } else {
            return 0;
        }
    }


    public static String showCooldownFormatted(long cooldownInSeconds, long startedTimeInMilliSeconds) {
        String time = "";

        String year = UtilString.get(Config.TIME_FORMAT_YEAR).hex().apply();
        String month = UtilString.get(Config.TIME_FORMAT_MONTH).hex().apply();
        String week = UtilString.get(Config.TIME_FORMAT_WEEK).hex().apply();
        String day = UtilString.get(Config.TIME_FORMAT_DAY).hex().apply();
        String hour = UtilString.get(Config.TIME_FORMAT_HOUR).hex().apply();
        String minute = UtilString.get(Config.TIME_FORMAT_MINUTE).hex().apply();
        String second = UtilString.get(Config.TIME_FORMAT_SECOND).hex().apply();

        long millis = System.currentTimeMillis();
        long cooldownmil = Math.round(cooldownInSeconds * 1000);

        long wait = millis - startedTimeInMilliSeconds;
        long waitDiv = wait / 1000;
        long waittotalsec = cooldownInSeconds - waitDiv;

        long waittotalmin = waittotalsec / 60;
        long waittotalhour = waittotalmin / 60;
        long waittotaldays = waittotalhour / 24;
        long waittotalweeks = waittotaldays / 7;
        long waittotalmonths = waittotalweeks / 4;
        long waittotalyears = waittotalmonths / 12;

        if (((startedTimeInMilliSeconds + cooldownmil) > millis) && (startedTimeInMilliSeconds != 0)) {
            if (waittotalsec > 59) {
                waittotalsec = waittotalsec - 60 * waittotalmin;
            }

            if (waittotalsec != 0) {
                time = waittotalsec + second;
            }

            if (waittotalmin > 59) {
                waittotalmin = waittotalmin - 60 * waittotalhour;
            }
            if (waittotalmin > 0) {
                time = waittotalmin + minute + " " + time;
            }

            if (waittotalhour > 23) {
                waittotalhour = waittotalhour - 24 * waittotaldays;
            }

            if (waittotalhour > 0) {
                time = waittotalhour + hour + " " + time;
            }

            if (waittotaldays > 6) {
                waittotaldays = waittotaldays - 7 * waittotalweeks;
            }

            if (waittotaldays > 0) {
                time = waittotaldays + day + " " + time;
            }

            if (waittotalweeks > 3) {
                waittotalweeks = waittotalweeks - 4 * waittotalmonths;
            }

            if (waittotalweeks > 0) {
                time = waittotalweeks + week + " " + time;
            }

            if (waittotalmonths > 11) {
                waittotalmonths = waittotalmonths - 12 * waittotalyears;
            }

            if (waittotalmonths > 0) {
                time = waittotalmonths + month + " " + time;
            }

            if (waittotalyears > 0) {
                time = waittotalyears + year + " " + time;
            }

            return time;
        } else {
            return "0" + second;
        }
    }

    public static boolean isRegisteredBooster(BoosterIdentifier boosterIdentifier, DurationType durationType) {
        if (boosterIdentifier == null) {
            return false;
        }

        if (!boosterIdentifier.isIdentifier()) { return false; }
        if (durationType == null) { return false; }
        if (durationType.equals(DurationType.NONE)) { return false; }

        return true;
    }

    public static String getTempBoosterStartMessage(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_TEMP_BOOSTER_START;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_TEMP_BOOSTER_START;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_TEMP_BOOSTER_START;
        }
        return messageReceiver;
    }

    public static String getTempBoosterEndMessage(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_TEMP_BOOSTER_END;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_TEMP_BOOSTER_END;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_TEMP_BOOSTER_END;
        }
        return messageReceiver;
    }

    public static String getTempBoosterAddTimeMessage(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_TEMP_BOOSTER_ADD_TIME;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_TEMP_BOOSTER_ADD_TIME;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_TEMP_BOOSTER_ADD_TIME;
        }
        return messageReceiver;
    }

    public static String getTempBoosterRemoveTimeMessage(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_TEMP_BOOSTER_REMOVE_TIME;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_TEMP_BOOSTER_REMOVE_TIME;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_TEMP_BOOSTER_REMOVE_TIME;
        }
        return messageReceiver;
    }

    public static String getBoosterSetBoostMessage(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_BOOSTER_SET_BOOST;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_BOOSTER_SET_BOOST;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_BOOSTER_SET_BOOST;
        }
        return messageReceiver;
    }

    public static String getBoosterAddBoostMessage(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_BOOSTER_ADD_BOOST;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_BOOSTER_ADD_BOOST;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_BOOSTER_ADD_BOOST;
        }
        return messageReceiver;
    }

    public static String getBoosterRemoveBoostMessage(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_BOOSTER_REMOVE_BOOST;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_BOOSTER_REMOVE_BOOST;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_BOOSTER_REMOVE_BOOST;
        }
        return messageReceiver;
    }

    public static String getBoosterAlreadyActive(BoosterType boosterType) {
        String messageReceiver = null;
        if (boosterType.equals(BoosterType.PERSONAL)) {
            messageReceiver = Messages.PERSONAL_BOOSTER_ALREADY_ACTIVE;
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            messageReceiver = Messages.GLOBAL_BOOSTER_ALREADY_ACTIVE;
        } else if (boosterType.equals(BoosterType.TEAM)) {
            messageReceiver = Messages.SUPERIORSKYBLOCK2_BOOSTER_ALREADY_ACTIVE;
        }
        return messageReceiver;
    }

    public static boolean getBoolean(String string) {
        return string.equalsIgnoreCase("true");
    }

    public static void sendMessageToReceiver(String messageReceiver, BoosterType boosterType, Player player, List<VariableArg> variables, BoosterItem boosterItem, boolean cancelledMessage) {
        if (!cancelledMessage) {
            UUID uuid = player.getUniqueId();
            if (boosterType.equals(BoosterType.PERSONAL)) {
                UtilString.get(messageReceiver).setVariables(player).setVariables(boosterItem)
                        .setVariables(variables).setPlaceholders(player).hex().sendMessage(player);
            } else if (boosterType.equals(BoosterType.GLOBAL)) {
                UtilString.get(messageReceiver).setVariables(player).setVariables(boosterItem)
                        .setVariables(variables).setPlaceholders(player).hex().sendMessageToOnlinePlayers();
            } else if (boosterType.equals(BoosterType.TEAM) && PluginsChecker.isPluginEnabled(boosterType.getHook())) {
                if (HooksManager.getTeamBoosterHook().hasTeam(uuid)) {
                    UUID teamUUID = HooksManager.getTeamBoosterHook().getTeamByPlayer(uuid);
                    Set<UUID> members = HooksManager.getTeamBoosterHook().getMembers(teamUUID);
                    for (UUID member : members) {
                        UtilString.get(messageReceiver).setVariables(player).setVariables(boosterItem)
                                .setVariables(variables).setPlaceholders(player).hex().sendMessage(member);
                    }
                }
            }
        }
    }
}
