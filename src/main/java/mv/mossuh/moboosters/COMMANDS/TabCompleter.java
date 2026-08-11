package mv.mossuh.moboosters.COMMANDS;

import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.CONFIGS.Configs;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DurationType;

import java.util.*;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("give", "giveall", "activate", "setbooster", "removebooster", "addtime", "removetime", "addboost", "removeboost");
        } else if (args.length > 1) {
            String arg = args[0].toLowerCase();
            if (args.length == 2) {
                switch (arg) {
                    case "give":
                    case "giveall":
                    case "activate":
                        return Configs.getCodes();
                    case "setbooster":
                    case "addtime":
                    case "removetime":
                    case "addboost":
                    case "removeboost":
                        return getPlayers();
                    case "removebooster":
                        return getIdentifiers();
                }
            } else if (args.length == 3) {
                switch (arg) {
                    case "give":
                    case "giveall":
                        return Arrays.asList("1", "5", "50", "150", "500");
                    case "activate":
                        return getPlayers();
                    case "setbooster":
                    case "addtime":
                    case "removetime":
                        return getIdentifiers();
                    case "removebooster":
                        return getBoosterTypes();
                    case "addboost":
                    case "removeboost":
                        return getDurationTypes();
                }
            } else if (args.length == 4) {
                switch (arg) {
                    case "give":
                        return getPlayers();
                    case "activate":
                        return Arrays.asList("true", "false");
                    case "setbooster":
                    case "addtime":
                    case "removetime":
                        return getBoosterTypes();
                    case "addboost":
                    case "removeboost":
                        return getIdentifiers();
                    case "removebooster":
                        List<String> durationApplicatorType = new ArrayList<>();
                        durationApplicatorType.addAll(getDurationTypes());
                        durationApplicatorType.addAll(getApplicatorTypes());
                        return durationApplicatorType;
                }
            } else if (args.length == 5) {
                switch (arg) {
                    case "setbooster":
                    case "addtime":
                    case "removetime":
                        return getApplicatorTypes();
                    case "removebooster":
                        return getDefaultBoosted();
                    case "addboost":
                    case "removeboost":
                        return getBoosterTypes();
                }
            } else if (args.length == 6) {
                switch (arg) {
                    case "setbooster":
                    case "addtime":
                    case "removetime":
                        return getDefaultBoosted();
                    case "removebooster":
                        return getDurationTypes();
                    case "addboost":
                    case "removeboost":
                        return getApplicatorTypes();
                }
            } else if (args.length == 7) {
                switch (arg) {
                    case "setbooster":
                        return Arrays.asList("0.05", "0.5", "5", "15.5", "50");
                    case "addtime":
                    case "removetime":
                        return Arrays.asList("30", "60", "300", "1800");
                    case "addboost":
                    case "removeboost":
                        return getDefaultBoosted();
                    case "removebooster":
                        return getPlayers();
                }
            } else if (args.length == 8) {
                switch (arg) {
                    case "setbooster":
                        return Arrays.asList("30", "60", "300", "1800");
                    case "addboost":
                    case "removeboost":
                        return Arrays.asList("0.05", "0.5", "5", "15.5", "50");
                }
            }
        }
        // en otro caso, no sugerimos nada
        return Collections.emptyList();
    }


    public List<String> getIdentifiers() {
        Set<String> identifiers = new HashSet<>();
        identifiers.add("default");
        identifiers.addAll(Config.IDENTIFIERS.getIdentifiers());
        return new ArrayList<>(identifiers);
    }
    public List<String> getDurationTypes() {
        DurationType[] durationTypes = DurationType.values();
        List<String> types = new ArrayList<>();
        for (DurationType d : durationTypes) {
            types.add(d.name());
        }
        return types;
    }
    public List<String> getBoosterTypes() {
        BoosterType[] boosterType = BoosterType.values();
        List<String> types = new ArrayList<>();
        for (BoosterType b : boosterType) {
            types.add(b.name());
        }
        return types;
    }
    public List<String> getApplicatorTypes() {
        ApplicatorType[] applicatorTypes = ApplicatorType.values();
        List<String> types = new ArrayList<>();
        for (ApplicatorType a : applicatorTypes) {
            types.add(a.name());
        }
        return types;
    }
    public List<String> getDefaultBoosted() {
        return Arrays.asList("experience", "fortune");
    }
    public List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }

        return players;
    }
}
