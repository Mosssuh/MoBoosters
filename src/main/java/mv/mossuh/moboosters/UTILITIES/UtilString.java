package mv.mossuh.moboosters.UTILITIES;

import me.clip.placeholderapi.PlaceholderAPI;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.Boost;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.DEBUG.Debugs;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.VERSION.ServerVersion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString {
    private void replacePattern(Pattern pattern, Function<Matcher, String> replacementFunction) {
        Matcher matcher = pattern.matcher(builder);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String replacement = replacementFunction.apply(matcher);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        builder = new StringBuilder(result.toString());
    }


    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");


    private StringBuilder builder;
    private boolean isString = false;
    private UtilString(String string) {
        if (string != null) { this.isString = true; }
        this.builder = new StringBuilder(string != null ? string : "");
    }

    public static UtilString get(String string) {
        return new UtilString(string);
    }

    public UtilString hex() {
        if (ServerVersion.isAtLeast(ServerVersion.MC1_16)) {
            replacePattern(HEX_PATTERN, matcher -> {
                String hex = matcher.group().substring(1); // Remove #
                StringBuilder hexColor = new StringBuilder("§x");
                for (char c : hex.toCharArray()) {
                    hexColor.append('§').append(c);
                }
                return hexColor.toString();
            });
        }
        builder = new StringBuilder(ChatColor.translateAlternateColorCodes('&', builder.toString()));
        return this;
    }


    public UtilString replaceString(String target, String replacement) {
        UsefulMethods.replacePlaceholder(builder, target, replacement);
        return this;
    }

    public UtilString setPlaceholders(Player player) {
        if (isString) {
            if (player != null && player.isOnline()) {
                String string = builder.toString();
                builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
            }
        }
        return this;
    }

    public UtilString setPlaceholders(CommandSender sender) {
        if (isString) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String string = builder.toString();
                builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
            }
        }
        return this;
    }

    public UtilString setPlaceholders(OfflinePlayer player) {
        if (isString) {
            if (player != null) {
                String string = builder.toString();
                builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
            }
        }
        return this;
    }

    public UtilString setPlaceholders(UUID uuid) {
        if (isString) {
            if (uuid != null) {
                if (Bukkit.getPlayer(uuid) != null) {
                    Player player = Bukkit.getPlayer(uuid);
                    String string = builder.toString();
                    builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
                } else {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    String string = builder.toString();
                    builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
                }
            }
        }
        return this;
    }

    public UtilString removeColors() {
        String string = builder.toString();
        builder = new StringBuilder(ChatColor.stripColor(string));
        return this;
    }

    public boolean evaluateString() {
        return UsefulMethods.evaluateString(builder.toString());
    }

    public boolean isNumeric() {
        return UsefulMethods.isNumeric(builder.toString());
    }

    public boolean hasPermission(CommandSender sender) {
        if (isString) {
            if (sender != null) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    return player.hasPermission(builder.toString());
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermission(Player player) {
        if (isString) {
            if (player != null) {
                return player.hasPermission(builder.toString());
            }
        }
        return false;
    }

    public boolean hasPermission(UUID uuid) {
        if (isString) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                return player.hasPermission(builder.toString());
            }
        }
        return false;
    }


    public UtilString setVariables(List<VariableArg> variables) {
        if (variables != null && !variables.isEmpty()) {
            for (VariableArg variable : variables) {
                if (variable.isVariable() && variable.isValue()) {
                    UsefulMethods.replacePlaceholder(builder, variable.getVariable(), variable.getValue());
                }
            }
        }
        return this;
    }

    public UtilString setDefaultPlayerVariables(Player player) {
        if (isString) {
            if (player != null && player.isOnline()) {
                List<VariableArg> variableList = new ArrayList<>();
                variableList.add(new VariableArg("%player%", player.getName()));
                setVariables(variableList);
            }
        }
        return this;
    }

    public UtilString setDefaultPlayerVariables(UUID uuid) {
        if (isString) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                List<VariableArg> variableList = new ArrayList<>();
                variableList.add(new VariableArg("%player%", player.getName()));
                setVariables(variableList);
            }
        }
        return this;
    }


    public UtilString setDefaultNumberRandomVariable() {
        String string = builder.toString();

        if (string.contains("{random_")) {
            Pattern pattern = Pattern.compile("\\{random_(\\d+)-(\\d+)}");
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                double min = Integer.parseInt(matcher.group(1));
                double max = Integer.parseInt(matcher.group(2));
                string = string.replace(matcher.group(0), UsefulMethods.random(min, max) + "");
                builder = new StringBuilder(string);
            }
        }
        return this;
    }

    public UtilString setMathPlaceholder() {
        if (isString) {
            String string = builder.toString();
            builder = new StringBuilder(UsefulMethods.setMathPlaceholder(string));
        }
        return this;
    }

    public UtilString setChangeOutputPlaceholder() {
        if (isString) {
            String string = builder.toString();
            builder = new StringBuilder(UsefulMethods.setChangeOutputPlaceholder(string));
        }
        return this;
    }

    public UtilString setTimeFormatter() {
        if (isString) {
            String string = builder.toString();
            builder = new StringBuilder(UsefulMethods.setTimeFormatter(string, Config.TIME_FORMAT));
        }
        return this;
    }

    public UtilString setDefaultBoostVariables(Boost boost) {
        if (isString) {
            List<VariableArg> variables = DefaultVariables.boost(boost);
            setVariables(variables);
        }
        return this;
    }

    public UtilString setDefaultBoosterVariables(BoosterIdentifier boosterIdentifier) {
        if (isString) {
            if (boosterIdentifier.isIdentifier()) {
                List<VariableArg> variables = DefaultVariables.boosterIdentifier(boosterIdentifier);
                setVariables(variables);
            }
        }
        return this;
    }
    public UtilString setDefaultBoosterVariables(BoosterConfig boosterConfig) {
        if (isString) {
            if (boosterConfig.isBooster()) {
                List<VariableArg> variables = DefaultVariables.boosterConfig(boosterConfig);
                setVariables(variables);
            }
        }
        return this;
    }

    public UtilString setDefaultBoosterVariables(BoosterItem boosterItem) {
        if (isString) {
            if (boosterItem != null && boosterItem.isBoosterItem()) {
                List<VariableArg> variables = DefaultVariables.boosterItem(boosterItem);
                setVariables(variables);
            }
        }
        return this;
    }

    public UtilString setArgs(CommandArgs args) {
        if (args != null && args.hasArgs()) {
            List<String> argList = args.getArgs();
            for (int i = 0; i < argList.size(); i++) {
                UsefulMethods.replacePlaceholder(builder, "%args_" + (i + 1) + "%", argList.get(i));
            }
        }
        return this;
    }


    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public String apply() {
        return builder.toString();
    }

    public void sendMessage(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage(builder.toString());
        } else {
            Bukkit.getConsoleSender().sendMessage(builder.toString());
        }
    }

    public void sendMessage(Player player) {
        if (isString) {
            if (player != null && player.isOnline()) {
                player.sendMessage(builder.toString());
            }
        }
    }

    public void sendMessage(UUID uuid) {
        if (isString) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.sendMessage(builder.toString());
            }
        }
    }

    public void sendMessageInConsole() {
        Bukkit.getConsoleSender().sendMessage(builder.toString());
    }

    public void sendMessageToOnlinePlayers() {
        if (isString) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(builder.toString());
            }
        }
    }

    public void sendMessageInConsole(DebugType debugType) {
        if (isString) {
            if (Debugs.isActive(debugType)) {
                Bukkit.getConsoleSender().sendMessage(builder.toString());
            }
        }
    }
}
