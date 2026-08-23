package mv.mossuh.moboosters.UTILITIES;

import me.clip.placeholderapi.PlaceholderAPI;
import mv.mossuh.moboosters.BOOSTERS.BoostTypes.Boost;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.DEBUG.Debugs;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.UTILITIES.UsefulString;
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

public class UtilString extends UsefulString<UtilString> {
    protected UtilString(String string) {
        super(string);
    }

    public static UtilString get(String string) {
        return new UtilString(string);
    }

    public UtilString setVariables(Player player) {
        if (isString()) {
            if (player != null && player.isOnline()) {
                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%player%", player.getName()));
                setVariables(variables);
            }
        }
        return this;
    }

    public UtilString setVariables(UUID uuid) {
        if (isString()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%player%", player.getName()));
                setVariables(variables);
            }
        }
        return this;
    }


    public UtilString setTimeFormatter() {
        if (isString()) {
            setTimeFormatter(Config.TIME_FORMAT);
        }
        return this;
    }

    public UtilString setVariables(Boost boost) {
        if (isString()) {
            List<VariableArg> variables = DefaultVariables.boost(boost);
            setVariables(variables);
        }
        return this;
    }

    public UtilString setVariables(BoosterIdentifier boosterIdentifier) {
        if (isString()) {
            if (boosterIdentifier.isIdentifier()) {
                List<VariableArg> variables = DefaultVariables.boosterIdentifier(boosterIdentifier);
                setVariables(variables);
            }
        }
        return this;
    }
    public UtilString setVariables(BoosterConfig boosterConfig) {
        if (isString()) {
            if (boosterConfig.isBooster()) {
                List<VariableArg> variables = DefaultVariables.boosterConfig(boosterConfig);
                setVariables(variables);
            }
        }
        return this;
    }

    public UtilString setVariables(BoosterItem boosterItem) {
        if (isString()) {
            if (boosterItem != null && boosterItem.isBoosterItem()) {
                List<VariableArg> variables = DefaultVariables.boosterItem(boosterItem);
                setVariables(variables);
            }
        }
        return this;
    }


    public void sendMessageInConsole(DebugType debugType) {
        if (isString()) {
            if (Debugs.isActive(debugType)) {
                Bukkit.getConsoleSender().sendMessage(apply());
            }
        }
    }
}
