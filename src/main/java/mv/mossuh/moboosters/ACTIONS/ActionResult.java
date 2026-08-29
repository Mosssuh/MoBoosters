package mv.mossuh.moboosters.ACTIONS;

import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.MODEL.Booster.BoosterItem;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {
    private Player player;
    private BoosterItem boosterItem = new BoosterItem();
    private List<VariableArg> variables = new ArrayList<>();
    private List<MoRewards> approvedRewards = new ArrayList<>();
    private boolean hasRewards = false;
    public ActionResult(Player player, BoosterItem boosterItem, List<VariableArg> variables, List<MoRewards> approvedRewards) {
        this.player = player;
        if (boosterItem != null) { this.boosterItem = boosterItem; }
        if (variables != null) { this.variables = variables; }
        if (approvedRewards != null) { this.approvedRewards = approvedRewards; }
        if (approvedRewards != null && !approvedRewards.isEmpty()) { this.hasRewards = true; }
    }
    public ActionResult() {}

    public Player getPlayer() { return player; }
    public BoosterItem getBoosterItem() { return boosterItem; }
    public List<VariableArg> getVariables() { return variables; }
    public List<MoRewards> getApprovedRewards() { return approvedRewards; }
    public boolean hasApprovedRewards() { return hasRewards; }
}
