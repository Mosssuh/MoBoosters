package mv.mossuh.moboosters.COMMANDS;

import mv.mossuh.moboosters.MANAGER.HooksManager;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.InvalidBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.TeamBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.DATA.Config.Messages;
import mv.mossuh.moboosters.MANAGER.BoosterManager;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;

import java.util.UUID;

public class RemoveBoosterCommands {
    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0 && strings[0].equalsIgnoreCase("removebooster")) {
            if (!UtilString.get("moboosters.admin").hasPermission(sender)) {
                UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                return;
            }

            if (strings.length >= 6) {
                // /moboosters removebooster <identifier> <booster type> <applicator> <boosted> <duration type> [<player>/<teamName>]
                String identifier = strings[1];
                BoosterType boosterType = BoosterType.getBoosterType(strings[2]);
                ApplicatorType applicatorType = ApplicatorType.convert(strings[3]);
                String boosted = strings[4];
                DurationType durationType = DurationType.getDurationType(strings[5]);
                String forWho = strings[6];

                if (!Config.IDENTIFIERS.hasIdentifier(identifier)) {
                    UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender);
                    return;
                }
                if (boosterType.equals(BoosterType.NONE)) {
                    UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender);
                    return;
                }
                if (applicatorType.equals(ApplicatorType.NONE)) {
                    UtilString.get(Messages.INVALID_APPLICATOR_TYPE).hex().sendMessage(sender);
                    return;
                }


                BoosterIdentifier bi = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                Booster booster = new InvalidBooster();

                if (boosterType.equals(BoosterType.PERSONAL)) {
                    Player player = Bukkit.getPlayer(forWho);
                    if (!UtilMethods.isOnline(player)) {
                        UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                        return;
                    }
                    booster = new PersonalBooster(player.getUniqueId(), bi);
                } else if (boosterType.equals(BoosterType.TEAM)) {
                    UUID id = HooksManager.getTeamBoosterHook().getTeamByName(forWho);
                    if (id == null) {
                        UtilString.get(Messages.INVALID_ISLAND_NAME).hex().sendMessage(sender);
                        return;
                    }
                    booster = new TeamBooster(id, bi);
                } else if (boosterType.equals(BoosterType.GLOBAL)){
                    booster = new GlobalBooster(bi);
                }

                if (!booster.isValid()) return;
                switch (durationType) {
                    case NONE:
                        BoostersAPI.getManager().removeBooster(booster);
                        break;
                    case TEMP:
                    case PERM:
                        BoostersAPI.getManager().removeBooster(durationType, booster);
                        break;
                }
            } else if (strings.length == 4 || strings.length == 5) {
                // /moboosters removebooster <identifier> <booster type> <duration type> [<player>/<teamName>]
                String identifier = strings[1];
                BoosterType boosterType = BoosterType.getBoosterType(strings[2]);
                DurationType durationType = DurationType.getDurationType(strings[3]);
                String forWho = strings[4];

                if (!Config.IDENTIFIERS.hasIdentifier(identifier)) {
                    UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(sender);
                    return;
                }
                if (boosterType.equals(BoosterType.NONE)) {
                    UtilString.get(Messages.INVALID_BOOSTER_TYPE).hex().sendMessage(sender);
                    return;
                }

                UUID uuid = null;
                if (boosterType.equals(BoosterType.PERSONAL)) {
                    Player player = Bukkit.getPlayer(forWho);
                    if (!UtilMethods.isOnline(player)) {
                        UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                        return;
                    }
                    uuid = player.getUniqueId();
                } else if (boosterType.equals(BoosterType.TEAM)) {
                    UUID teamUUID = HooksManager.getTeamBoosterHook().getTeamByName(forWho);
                    if (teamUUID == null) {
                        UtilString.get(Messages.INVALID_ISLAND_NAME).hex().sendMessage(sender);
                        return;
                    }
                    uuid = teamUUID;
                }

                switch (durationType) {
                    case NONE:
                        BoosterManager.removeBooster(identifier, boosterType, uuid);
                        break;
                    case TEMP:
                    case PERM:
                        BoosterManager.removeBooster(identifier, boosterType, durationType, uuid);
                        break;
                }
            } else {
                UtilString.get(Config.PREFIX+" &cUse: /moboosters removebooster <identifier> <booster type> <applicator> <boosted> <duration type> &4[<player>/<teamName]").hex().sendMessage(sender);
                UtilString.get(Config.PREFIX+" &cUse: /moboosters removebooster <identifier> <booster type> <duration type> &4[<player>/<teamName]").hex().sendMessage(sender);
            }
        }
    }

}
