package mv.mossuh.moboosters.UTILITIES;

import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoosterItem;
import mv.mossuh.moboosters.MODEL.Booster.Config.BoosterConfig;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.MANAGER.DebugManager;
import mv.mossuh.moboosters.UTILITIES.Enums.DebugType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            if (DebugManager.isActive(debugType)) {
                Bukkit.getConsoleSender().sendMessage(apply());
            }
        }
    }
}
