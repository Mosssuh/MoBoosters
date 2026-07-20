package mv.mossuh.moboosters.ACTIONS;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.UTILITIES.DefaultVariables;
import mv.mossuh.moboosters.UTILITIES.UtilString;

import java.util.*;

public class ExecuteAction {
    private MoAction moAction = new MoAction(null, null, null, null, null, null, null);
    private Player player;
    private BoosterItem boosterItem = new BoosterItem(null, null, null, null, null, null, null);
    private List<VariableArg> variables = new ArrayList<>();
    private ActionResult actionResult = new ActionResult(null, null, null, null);
    private boolean cancelMessage = false;
    private boolean cancelBooster = false;
    private boolean cancelClaim = false;
    public ExecuteAction(MoAction moAction, Player player, BoosterItem boosterItem, List<VariableArg> variables) {
        if (moAction != null) { this.moAction = moAction; }
        this.player = player;
        if (boosterItem != null) { this.boosterItem = boosterItem; }
        if (variables != null) { this.variables = new ArrayList<>(variables); }
    }
    public boolean cancelMessage() { return cancelMessage; }
    public boolean cancelBooster() { return  cancelBooster; }
    public boolean cancelClaim() { return cancelClaim; }

    public ExecuteAction addVariables(VariableArg... variables) {
        this.variables.addAll(Arrays.asList(variables));
        return this;
    }

    public ExecuteAction addVariables(List<VariableArg> variables) {
        this.variables.addAll(variables);
        return this;
    }

    public ExecuteAction addAllDefaultVariables() {
        addDefaultPlayerVariables();
        addDefaultBoosterVariables();
        return this;
    }

    public ExecuteAction addDefaultPlayerVariables() {
        this.variables.addAll(DefaultVariables.player(player));
        return this;
    }

    public ExecuteAction addDefaultBoosterVariables() {
        this.variables.addAll(DefaultVariables.boosterItem(boosterItem));
        return this;
    }
    public ExecuteAction check() {

        UUID uuid = player.getUniqueId();

        MoRequirements requirements = moAction.getRequirements();
        List<MoRequirement> requirementList = requirements.getRequirements();
        int requirementsAmount = requirementList.size();
        int requirementsAccepted = 0;

        for (MoRequirement vRequirement : requirementList) {
            if (vRequirement.isRequirement(RequirementType.EVAL)) {
                RequirementEval requirement = (RequirementEval) vRequirement.getRequirement();
                for (String eval : requirement.getRequirements()) {
                    boolean condition = UtilString.get(eval).hex().setVariables(variables)
                            .setDefaultNumberRandomVariable().setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                            .setTimeFormatter().evaluateString();
                    if (condition) {
                        requirementsAccepted = requirementsAccepted + 1;
                        break;
                    }
                }
            }
        }

        if (requirementsAccepted == requirementsAmount) {
            MoRewards moRewards = moAction.getRewards();
            actionResult = new ActionResult(player, boosterItem, new ArrayList<>(variables), new ArrayList<>(Collections.singletonList(moRewards)));
            Rewards rewards = new Rewards(actionResult).executeDefault().executeCancellations().check();
            if (rewards.cancelMessage()) { this.cancelMessage = true; }
            if (rewards.cancelBooster()) { this.cancelBooster = true; }
            if (rewards.cancelClaim()) { this.cancelClaim = true; }
        } else {
            MoRewards moRewards = moAction.getElseRewards();
            actionResult = new ActionResult(player, boosterItem, new ArrayList<>(variables), new ArrayList<>(Collections.singletonList(moRewards)));
            Rewards rewards = new Rewards(actionResult).executeDefault().executeCancellations().check();
            if (rewards.cancelMessage()) { this.cancelMessage = true; }
            if (rewards.cancelBooster()) { this.cancelBooster = true; }
            if (rewards.cancelClaim()) { this.cancelClaim = true; }
        }
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
