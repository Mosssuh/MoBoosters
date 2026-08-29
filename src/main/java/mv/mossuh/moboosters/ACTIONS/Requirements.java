package mv.mossuh.moboosters.ACTIONS;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.ArgType;
import mv.mossuh.mocore.UTILITIES.ARGS.Args;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.MODEL.Booster.Config.BoosterConfig;
import mv.mossuh.moboosters.MODEL.Booster.BoosterItem;
import mv.mossuh.moboosters.UTILITIES.DefaultVariables;
import mv.mossuh.moboosters.UTILITIES.UtilString;

import java.util.*;

public class Requirements {
    private List<MoRewards> approvedRewards = new ArrayList<>();
    private Player player;
    private BoosterItem boosterItem = new BoosterItem();
    private BoosterConfig boosterConfig = new BoosterConfig();
    private List<VariableArg> variables = new ArrayList<>();
    private ActionResult actionResult = new ActionResult();

    public Requirements(Player player, BoosterItem boosterItem) {
        this.player = player;
        if (boosterItem != null) {
            this.boosterItem = boosterItem;
            this.boosterConfig = boosterItem.getBoosterConfig();
        }
    }

    public Requirements addVariables(VariableArg... variables) {
        this.variables.addAll(Arrays.asList(variables));
        return this;
    }

    public Requirements addVariables(List<VariableArg> variables) {
        this.variables.addAll(variables);
        return this;
    }

    public Requirements addAllDefaultVariables() {
        addDefaultPlayerVariables();
        addDefaultBoosterVariables();
        return this;
    }

    public Requirements addDefaultPlayerVariables() {
        this.variables.addAll(DefaultVariables.player(player));
        return this;
    }

    public Requirements addDefaultBoosterVariables() {
        this.variables.addAll(DefaultVariables.boosterItem(boosterItem));
        return this;
    }

    public Requirements addArgsVariables(Args args) {
        if (args.getArgType().equals(ArgType.COMMAND) && args.hasArgs()) {
            CommandArgs commandArgs = (CommandArgs) args;
            int size = commandArgs.getArgs().size();
            for (int i = 0; i < size; i++) {
                this.variables.add(new VariableArg("%args_" + (i+1)  + "%", commandArgs.getArg(i)));
            }
        }
        return this;
    }

    public Requirements check() {
        List<MoAction> actionList = boosterConfig.getActions().getActions();

        UUID uuid = player.getUniqueId();

        if (!actionList.isEmpty()) {
            for (MoAction action : actionList) {
                if (!action.isCancelled()) {
                    MoRequirements requirements = action.getRequirements();
                    List<MoRequirement> requirementList = requirements.getRequirements();
                    int requirementsAmount = requirementList.size();
                    int requirementsAccepted = 0;

                    if (!requirementList.isEmpty()) {
                        for (MoRequirement vRequirement : requirementList) {
                            if (vRequirement.isRequirement(RequirementType.EVAL)) {
                                RequirementEval requirement = (RequirementEval) vRequirement.getRequirement();
                                for (String eval : requirement.getRequirements()) {
                                    boolean condition = UtilString.get(eval).setVariables(variables)
                                            .setPlaceholders(uuid).setTimeFormatter().hex().evaluateString();
                                    if (condition) {
                                        requirementsAccepted = requirementsAccepted + 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (requirementsAccepted == requirementsAmount) {
                        this.approvedRewards.add(action.getRewards());
                    } else {
                        this.approvedRewards.add(action.getElseRewards());
                    }
                }
            }
        }
        actionResult = new ActionResult(player, boosterItem, new ArrayList<>(variables), approvedRewards);
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
