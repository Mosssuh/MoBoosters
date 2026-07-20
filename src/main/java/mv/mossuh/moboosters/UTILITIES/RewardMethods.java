package mv.mossuh.moboosters.UTILITIES;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DurationType;
import mv.mossuh.moboosters.CONFIGS.Messages;

import java.util.*;

public class RewardMethods {

    public static void consoleCommand(String command) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
    }

    public static void playerCommand(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.performCommand(command);
        }
    }

    public static void playerCommandAsOP(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isOp()) {
                player.performCommand(command);
            } else {
                player.setOp(true);
                player.performCommand(command);
                player.setOp(false);
            }
        }
    }

    public static void playerMessage(LivingEntity entity, String message) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.sendMessage(message);
        }
    }

    public static void playerTitle(LivingEntity entity, String titleSubtitle) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = titleSubtitle.split("::");
            String title = space[0];
            String subtitle = space[1];
            player.sendTitle(title, subtitle);
        }
    }

    public static void playerSound(LivingEntity entity, String soundString) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = soundString.split("::", 3);
            String sound = space[0];
            float volume = Float.parseFloat(space[1]);
            float pitch = Float.parseFloat(space[2].replace(" ", ""));
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    public static void broadcastTitle(String titleSubtitle) {
        String title = "";
        String subtitle = "";
        String[] space;
        space = titleSubtitle.split("::");
        title = space[0];
        subtitle = space[1];

        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            playerOnline.sendTitle(title, subtitle);
        }
    }

    public static void json(LivingEntity entity, String json) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.dispatchCommand(console, ("tellraw " + player.getName() + " " + json));
        }
    }

    public static void jsonBroadcast(String json) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            Bukkit.dispatchCommand(console, ("tellraw " + playerOnline.getName() + " " + json));
        }
    }

    public static void effect(LivingEntity entity, String effectString) {
        if (entity != null) {
            String[] effectSeparate = effectString.replace(" ", "").split("::", 3);
            String effect = effectSeparate[0];
            int duration = Integer.parseInt(effectSeparate[1]) * 20;
            int amplifier = Integer.parseInt(effectSeparate[2]);
            PotionEffect poison = new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(effect)), duration, amplifier);
            entity.addPotionEffect(poison, true);
        }
    }

    public static void addBoost(LivingEntity entity, String information, boolean cancelMessage) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();

            String[] informationSplit = information.split("::", 6);
            if (informationSplit.length == 6) {
                // ADD_BOOST -> <duration type>::<identifier>::<booster type>::<applicator type>::<boosted>::<boost>
                DurationType durationType = UtilMethods.getDurationType(informationSplit[0]);
                String identifier = informationSplit[1];
                BoosterType boosterType = UtilMethods.getBoosterType(informationSplit[2]);
                ApplicatorType applicatorType = UtilMethods.getApplicatorType(informationSplit[3]);
                String boosted = informationSplit[4];
                if (!UtilMethods.isNumeric(informationSplit[5])) {
                    return;
                }
                double boost = Double.parseDouble(informationSplit[5]);

                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%duration_type%" ,durationType.name()));
                variables.add(new VariableArg("%boost%", boost+""));
                String boostBase = UtilMethods.formatNumber(boost + 1, 10);
                variables.add(new VariableArg("%boost_with_base%", boostBase));

                if (boosterType.equals(BoosterType.PERSONAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.PERSONAL_BOOSTER_ADD_BOOST).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessage(player);
                    }
                    BoostersAPI.getManager().addBoost(durationType, new PersonalBooster(playerUUID, boosterIdentifier), boost);
                } else if (boosterType.equals(BoosterType.GLOBAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.GLOBAL_BOOSTER_ADD_BOOST).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessageToOnlinePlayers();
                    }
                    BoostersAPI.getManager().addBoost(durationType, new GlobalBooster(boosterIdentifier), boost);
                } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                    SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                    if (!superiorPlayer.hasIsland()) {
                        return;
                    }
                    UUID islandUUID = superiorPlayer.getIsland().getUniqueId();
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);

                    if (!cancelMessage) {
                        List<SuperiorPlayer> islandPlayers = superiorPlayer.getIsland().getIslandMembers(true);
                        for (SuperiorPlayer sPlayer : islandPlayers) {
                            UtilString.get(Messages.SUPERIORSKYBLOCK2_BOOSTER_ADD_BOOST).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                    .setVariables(variables).sendMessage(sPlayer.getUniqueId());
                        }
                    }

                    BoostersAPI.getManager().addBoost(durationType, new SuperiorSkyblock2Booster(islandUUID, boosterIdentifier), boost);
                }
            }
        }
    }

    public static void removeBoost(LivingEntity entity, String information, boolean cancelMessage) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();

            String[] informationSplit = information.split("::", 5);
            if (informationSplit.length == 6) {
                // REMOVE_BOOST -> <duration type>::<identifier>::<booster type>::<applicator type>::<boosted>::<boost>
                DurationType durationType = UtilMethods.getDurationType(informationSplit[0]);
                String identifier = informationSplit[1];
                BoosterType boosterType = UtilMethods.getBoosterType(informationSplit[2]);
                ApplicatorType applicatorType = UtilMethods.getApplicatorType(informationSplit[3]);
                String boosted = informationSplit[4];
                if (!UtilMethods.isNumeric(informationSplit[5])) {
                    return;
                }
                double boost = Double.parseDouble(informationSplit[5]);

                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%duration_type%" ,durationType.name()));
                variables.add(new VariableArg("%boost%", boost+""));
                String boostBase = UtilMethods.formatNumber(boost + 1, 10);
                variables.add(new VariableArg("%boost_with_base%", boostBase));

                if (boosterType.equals(BoosterType.PERSONAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.PERSONAL_BOOSTER_REMOVE_BOOST).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessage(player);
                    }
                    BoostersAPI.getManager().removeBoost(durationType, new PersonalBooster(playerUUID, boosterIdentifier), boost);
                } else if (boosterType.equals(BoosterType.GLOBAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.GLOBAL_BOOSTER_REMOVE_BOOST).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessageToOnlinePlayers();
                    }
                    BoostersAPI.getManager().removeBoost(durationType, new GlobalBooster(boosterIdentifier), boost);
                } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                    SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                    if (!superiorPlayer.hasIsland()) {
                        return;
                    }
                    UUID islandUUID = superiorPlayer.getIsland().getUniqueId();
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);

                    if (!cancelMessage) {
                        List<SuperiorPlayer> islandPlayers = superiorPlayer.getIsland().getIslandMembers(true);
                        for (SuperiorPlayer sPlayer : islandPlayers) {
                            UtilString.get(Messages.SUPERIORSKYBLOCK2_BOOSTER_REMOVE_BOOST).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                    .setVariables(variables).sendMessage(sPlayer.getUniqueId());
                        }
                    }

                    BoostersAPI.getManager().removeBoost(durationType, new SuperiorSkyblock2Booster(islandUUID, boosterIdentifier), boost);
                }
            }
        }
    }
    public static void addTime(LivingEntity entity, String information, boolean cancelMessage) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();

            String[] informationSplit = information.split("::", 5);
            if (informationSplit.length == 5) {
                // ADD_TIME -> <identifier>::<booster type>::<applicator type>::<boosted>::<duration in seconds>
                DurationType durationType = DurationType.TEMP;
                String identifier = informationSplit[0];
                BoosterType boosterType = UtilMethods.getBoosterType(informationSplit[1]);
                ApplicatorType applicatorType = UtilMethods.getApplicatorType(informationSplit[2]);
                String boosted = informationSplit[3];
                if (!UtilMethods.isNumeric(informationSplit[4])) {
                    return;
                }
                long duration = Long.parseLong(informationSplit[4]);

                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%duration_type%" , durationType.name()));
                variables.add(new VariableArg("%duration%", duration+""));
                variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(duration, System.currentTimeMillis())));

                if (boosterType.equals(BoosterType.PERSONAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.PERSONAL_TEMP_BOOSTER_ADD_TIME).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessage(player);
                    }
                    BoostersAPI.getManager().addTime(new PersonalBooster(playerUUID, boosterIdentifier), duration);
                } else if (boosterType.equals(BoosterType.GLOBAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.GLOBAL_TEMP_BOOSTER_ADD_TIME).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessageToOnlinePlayers();
                    }
                    BoostersAPI.getManager().addTime(new GlobalBooster(boosterIdentifier), duration);
                } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                    SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                    if (!superiorPlayer.hasIsland()) {
                        return;
                    }
                    UUID islandUUID = superiorPlayer.getIsland().getUniqueId();
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        List<SuperiorPlayer> islandPlayers = superiorPlayer.getIsland().getIslandMembers(true);
                        for (SuperiorPlayer sPlayer : islandPlayers) {
                            UtilString.get(Messages.SUPERIORSKYBLOCK2_TEMP_BOOSTER_ADD_TIME).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                    .setVariables(variables).sendMessage(sPlayer.getUniqueId());
                        }
                    }
                    BoostersAPI.getManager().addTime(new SuperiorSkyblock2Booster(islandUUID, boosterIdentifier), duration);
                }
            }
        }
    }

    public static boolean accumulateBooster(LivingEntity entity, String information, boolean cancelMessage) {
        // ACCUMULATE_BOOSTER -> <identifier>::<booster type>::<applicator type>::<boosted>::<boost>::<duration>/PERM
        if (entity instanceof Player){
            Player player = (Player) entity;
            String[] split = information.split("::", 6);
            if  (split.length < 5) return true;
            String identifier = split[0];
            BoosterType boosterType = UtilMethods.getBoosterType(split[1]);
            ApplicatorType applicatorType =  UtilMethods.getApplicatorType(split[2]);
            String boosted = split[3];
            double boost;
            try {
                boost = Double.parseDouble(split[4]);
            } catch (NumberFormatException e) {
                return true;
            }

            long duration = 0L;
            DurationType durationType = DurationType.PERM;
            if (split.length == 6) {
                if (!split[5].equalsIgnoreCase("perm")) {
                    try {
                        duration = Long.parseLong(split[5]);
                        durationType = DurationType.TEMP;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            BoosterIdentifier id = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
            Booster booster = UtilMethods.getBooster(id, player);
            if (!booster.isValid()) return true;

            List<VariableArg> variables = new ArrayList<>(DefaultVariables.boosterIdentifier(id));
            variables.add(new VariableArg("%duration_type%" , durationType.name()));
            variables.add(new VariableArg("%duration%", duration+""));
            variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(duration, System.currentTimeMillis())));
            variables.add(new VariableArg("%boost%", boost+""));
            String boostBase = UtilMethods.formatNumber(boost + 1, 10);
            variables.add(new VariableArg("%boost_with_base%", boostBase));

            ActiveBooster active = BoostersAPI.getManager().getBooster(booster);

            switch (durationType) {
                case TEMP:
                    if (active.isActive(durationType)) {
                        if (active.getBoosts().getTemporary().getBoost() == boost) {
                            String message = UtilMethods.getTempBoosterAddTimeMessage(boosterType);
                            UtilMethods.sendMessageToReceiver(message, boosterType, player, variables, null, cancelMessage);
                            BoostersAPI.getManager().addTime(booster, duration);
                            return false;
                        } else {
                            String message = UtilMethods.getBoosterAlreadyActive(boosterType);
                            UtilMethods.sendMessageToReceiver(message, boosterType, player, variables, null, cancelMessage);
                            return true;
                        }
                    } else {
                        String message = UtilMethods.getTempBoosterStartMessage(boosterType);
                        UtilMethods.sendMessageToReceiver(message, boosterType, player, variables, null, cancelMessage);
                        BoostersAPI.getManager().setTempBoost(booster, boost, duration);
                        return false;
                    }
                case PERM:
                    String message = UtilMethods.getBoosterAddBoostMessage(boosterType);
                    UtilMethods.sendMessageToReceiver(message, boosterType, player, variables, null, cancelMessage);
                    BoostersAPI.getManager().addBoost(durationType, booster, boost);
                    return false;
            }
        }
        return true;
    }

    public static void setBooster(LivingEntity entity, String information, boolean cancelMessage)  {
        // SET_BOOSTER -> <identifier>::<booster type>::<applicator type>::<boosted>::<boost>::<duration>/PERM
        if (entity instanceof Player){
            Player player = (Player) entity;
            String[] split = information.split("::", 6);
            if  (split.length < 5) return;
            String identifier = split[0];
            BoosterType boosterType = UtilMethods.getBoosterType(split[1]);
            ApplicatorType applicatorType =  UtilMethods.getApplicatorType(split[2]);
            String boosted = split[3];
            double boost;
            try {
                boost = Double.parseDouble(split[4]);
            } catch (NumberFormatException e) {
                return;
            }

            long duration = 0L;
            DurationType durationType = DurationType.PERM;
            if (split.length == 6) {
                if (!split[5].equalsIgnoreCase("perm")) {
                    try {
                        duration = Long.parseLong(split[5]);
                        durationType = DurationType.TEMP;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            BoosterIdentifier id = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
            Booster booster = UtilMethods.getBooster(id, player);
            if (!booster.isValid()) return;

            List<VariableArg> variables = new ArrayList<>(DefaultVariables.boosterIdentifier(id));
            variables.add(new VariableArg("%duration_type%" , durationType.name()));
            variables.add(new VariableArg("%duration%", duration+""));
            variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(duration, System.currentTimeMillis())));
            variables.add(new VariableArg("%boost%", boost+""));
            String boostBase = UtilMethods.formatNumber(boost + 1, 10);
            variables.add(new VariableArg("%boost_with_base%", boostBase));

            switch (durationType) {
                case PERM:
                    UtilMethods.sendMessageToReceiver(UtilMethods.getBoosterSetBoostMessage(boosterType), boosterType, player, variables, null, cancelMessage);
                    BoostersAPI.getManager().setPermBoost(booster, boost);
                    break;
                case TEMP:
                    UtilMethods.sendMessageToReceiver(UtilMethods.getTempBoosterStartMessage(boosterType), boosterType, player, variables, null, cancelMessage);
                    BoostersAPI.getManager().setTempBoost(booster, boost, duration);
                    break;
            }

        }
    }

    public static void removeTime(LivingEntity entity, String information, boolean cancelMessage) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();

            String[] informationSplit = information.split("::", 5);
            if (informationSplit.length == 5) {
                // REMOVE_TIME -> <identifier>::<booster type>::<applicator type>::<boosted>::<duration in seconds>
                DurationType durationType = DurationType.TEMP;
                String identifier = informationSplit[0];
                BoosterType boosterType = UtilMethods.getBoosterType(informationSplit[1]);
                ApplicatorType applicatorType = UtilMethods.getApplicatorType(informationSplit[2]);
                String boosted = informationSplit[3];
                if (!UtilMethods.isNumeric(informationSplit[4])) {
                    return;
                }
                long duration = Long.parseLong(informationSplit[4]);

                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%duration_type%" , durationType.name()));
                variables.add(new VariableArg("%duration%", duration+""));
                variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(duration, System.currentTimeMillis())));

                if (boosterType.equals(BoosterType.PERSONAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.PERSONAL_TEMP_BOOSTER_REMOVE_TIME).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessage(player);
                    }
                    BoostersAPI.getManager().removeTime(new PersonalBooster(playerUUID, boosterIdentifier), duration);
                } else if (boosterType.equals(BoosterType.GLOBAL)) {
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        UtilString.get(Messages.GLOBAL_TEMP_BOOSTER_REMOVE_TIME).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                .setVariables(variables).sendMessageToOnlinePlayers();
                    }
                    BoostersAPI.getManager().addTime(new GlobalBooster(boosterIdentifier), duration);
                } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                    SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                    if (!superiorPlayer.hasIsland()) {
                        return;
                    }
                    UUID islandUUID = superiorPlayer.getIsland().getUniqueId();
                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    if (!cancelMessage) {
                        List<SuperiorPlayer> islandPlayers = superiorPlayer.getIsland().getIslandMembers(true);
                        for (SuperiorPlayer sPlayer : islandPlayers) {
                            UtilString.get(Messages.SUPERIORSKYBLOCK2_TEMP_BOOSTER_REMOVE_TIME).hex().setDefaultPlayerVariables(player).setDefaultBoosterVariables(boosterIdentifier)
                                    .setVariables(variables).sendMessage(sPlayer.getUniqueId());
                        }
                    }
                    BoostersAPI.getManager().removeTime(new SuperiorSkyblock2Booster(islandUUID, boosterIdentifier), duration);
                }
            }
        }
    }
}
