package mv.mossuh.moboosters.UTILITIES;

import mv.mossuh.moboosters.BOOSTERS.BoostTypes.Boost;
import mv.mossuh.moboosters.BOOSTERS.BoostTypes.TemporaryBoost;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.ENUMS.DurationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DefaultVariables {
    
    public static List<VariableArg> player(Player player) {
        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%player%", player.getName()));
        return variables;
    }

    public static List<VariableArg> boosterConfig(UUID uuid, BoosterConfig boosterConfig) {
        List<VariableArg> variables = new ArrayList<>();
        if (boosterConfig.isBooster()) {
            String code = boosterConfig.getCode();
            String boost = UtilString.get(boosterConfig.getBoost()).setPlaceholders(uuid).setTimeFormatter().apply();
            String durationType = boosterConfig.getDurationType().name();
            String duration = UtilString.get(boosterConfig.getDuration()).setPlaceholders(uuid).setTimeFormatter().apply();
            BoosterIdentifier boosterIdentifier = boosterConfig.getIdentifier();
            String identifier = UtilString.get(boosterIdentifier.getIdentifier()).setPlaceholders(uuid).setTimeFormatter().apply();
            String boosterType = boosterIdentifier.getBoosterType().name();
            String applicatorType = boosterIdentifier.getApplicatorType().name();
            String boosted = UtilString.get(boosterIdentifier.getBoosted()).setPlaceholders(uuid).setTimeFormatter().apply();;

            variables.add(new VariableArg("%code%", code));
            variables.add(new VariableArg("%boost%", boost));
            variables.add(new VariableArg("%duration_type%", durationType));
            variables.add(new VariableArg("%duration%", duration));
            if (!Objects.equals(duration, "PERM")) {
                variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(Long.parseLong(duration), System.currentTimeMillis())));
            } else {
                variables.add(new VariableArg("%duration_formatted%", "PERM"));
            }
            variables.add(new VariableArg("%identifier%", identifier));
            variables.add(new VariableArg("%booster_type%", boosterType));
            variables.add(new VariableArg("%applicator_type%", applicatorType));
            variables.add(new VariableArg("%boosted%", boosted));

        }
        return variables;
    }

    public static List<VariableArg> boosterConfig(BoosterConfig boosterConfig) {
        List<VariableArg> variables = new ArrayList<>();
        if (boosterConfig.isBooster()) {
            String code = boosterConfig.getCode();
            String boost = UtilMethods.formatNumber(Double.parseDouble(boosterConfig.getBoost()), 10);
            String boostWithBase = UtilMethods.formatNumber(Double.parseDouble(boosterConfig.getBoost()) + 1, 10);
            String duration = boosterConfig.getDuration();
            String durationType = boosterConfig.getDurationType().name();
            BoosterIdentifier boosterIdentifier = boosterConfig.getIdentifier();
            String identifier = boosterIdentifier.getIdentifier();
            String boosterType = boosterIdentifier.getBoosterType().name();
            String applicatorType = boosterIdentifier.getApplicatorType().name();
            String boosted = boosterIdentifier.getBoosted();

            variables.add(new VariableArg("%code%", code));
            variables.add(new VariableArg("%boost%", boost));
            variables.add(new VariableArg("%boost_with_base%", boostWithBase));
            variables.add(new VariableArg("%duration_type%", durationType));
            variables.add(new VariableArg("%duration%", duration));
            if (!duration.equals("PERM")) {
                variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(Long.parseLong(duration), System.currentTimeMillis())));
            } else {
                variables.add(new VariableArg("%duration_formatted%", "PERM"));
            }
            variables.add(new VariableArg("%identifier%", identifier));
            variables.add(new VariableArg("%booster_type%", boosterType));
            variables.add(new VariableArg("%applicator_type%", applicatorType));
            variables.add(new VariableArg("%boosted%", boosted));
        }
        return variables;
    }


    public static List<VariableArg> boosterItem(BoosterItem boosterItem) {
        List<VariableArg> variables = new ArrayList<>();
        if (boosterItem.isBoosterItem()) {
            String code = boosterItem.getCode();
            String boost = boosterItem.getBoost();
            String durationType = boosterItem.getDurationType().name();
            String duration = boosterItem.getDuration();
            BoosterIdentifier boosterIdentifier = boosterItem.getBoosterConfig().getIdentifier();
            String identifier = boosterIdentifier.getIdentifier();
            String boosterType = boosterIdentifier.getBoosterType().name();
            String applicatorType = boosterIdentifier.getApplicatorType().name();
            String boosted = boosterIdentifier.getBoosted();

            variables.add(new VariableArg("%code%", code));
            variables.add(new VariableArg("%boost%", boost));
            String boostBase = UtilMethods.formatNumber(Double.parseDouble(boost) + 1, 10);
            variables.add(new VariableArg("%boost_with_base%", boostBase));
            variables.add(new VariableArg("%duration_type%", durationType));
            variables.add(new VariableArg("%duration%", duration));
            if (!Objects.equals(duration, "PERM")) {
                variables.add(new VariableArg("%duration_formatted%", UtilMethods.showCooldownFormatted(Long.parseLong(duration), System.currentTimeMillis())));
            } else {
                variables.add(new VariableArg("%duration_formatted%", "PERM"));
            }
            variables.add(new VariableArg("%identifier%", identifier));
            variables.add(new VariableArg("%booster_type%", boosterType));
            variables.add(new VariableArg("%applicator_type%", applicatorType));
            variables.add(new VariableArg("%boosted%", boosted));

        }
        return variables;
    }

    public static List<VariableArg> boosterIdentifier(BoosterIdentifier boosterIdentifier) {
        List<VariableArg> variables = new ArrayList<>();
        if (boosterIdentifier.isIdentifier()) {
            String identifier = boosterIdentifier.getIdentifier();
            String boosterType = boosterIdentifier.getBoosterType().name();
            String applicatorType = boosterIdentifier.getApplicatorType().name();
            String boosted = boosterIdentifier.getBoosted();

            variables.add(new VariableArg("%identifier%", identifier));
            variables.add(new VariableArg("%booster_type%", boosterType));
            variables.add(new VariableArg("%applicator_type%", applicatorType));
            variables.add(new VariableArg("%boosted%", boosted));
        }
        return variables;
    }

    public static List<VariableArg> boost(Boost boost) {
        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%boost%", UtilMethods.formatNumber(boost.getBoost(),10)));
        variables.add(new VariableArg("%boost_with_base%", UtilMethods.formatNumber(boost.getBoost() + 1, 10)));
        if (boost.getDurationType().equals(DurationType.TEMP)) {
            TemporaryBoost temporaryBoost = (TemporaryBoost) boost;
            variables.add(new VariableArg("%duration_type%", boost.getDurationType().name()));
            variables.add(new VariableArg("%duration%", temporaryBoost.getDuration().getRemainingTime()+""));
            variables.add(new VariableArg("%duration_formatted%", temporaryBoost.getDuration().getRemainingTimeFormatted()));
        }
        return variables;
    }
}
