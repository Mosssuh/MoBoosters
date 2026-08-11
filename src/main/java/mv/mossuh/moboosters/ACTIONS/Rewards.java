package mv.mossuh.moboosters.ACTIONS;

import mv.mossuh.moboosters.UTILITIES.RewardMethods;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoActions;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.ENUMS.RewardReceiverType;
import mv.mossuh.mocore.ENUMS.RewardType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.REWARDS.RewardReceiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.MoBoosters;

import java.util.List;
import java.util.UUID;

public class Rewards {
    private final MoBoosters instance = MoBoosters.getInstance();
    private boolean executeDefaultRewards = false;
    private boolean executeCancellationRewards = false;
    private boolean cancelMessage = false;
    private boolean cancelBooster = false;
    private boolean cancelClaim = false;

    private ActionResult actionResult = new ActionResult(null, null, null, null);
    private int times = 1;


    public Rewards(ActionResult actionResult) {
        if (actionResult != null) { this.actionResult = actionResult; }
    }

    public Rewards(ActionResult actionResult, Integer times) {
        if (actionResult != null) { this.actionResult = actionResult; }
        if (times != null) { this.times = times; }
    }
    public boolean cancelMessage() { return cancelMessage; }
    public boolean cancelBooster() { return cancelBooster; }
    public boolean cancelClaim() { return cancelClaim; }

    public Rewards executeDefault() {
        this.executeDefaultRewards = true;
        return this;
    }
    public Rewards executeCancellations() {
        this.executeCancellationRewards = true;
        return this;
    }

    public Rewards check() {
        if(!Bukkit.isPrimaryThread()){
            new BukkitRunnable(){
                @Override
                public void run() {
                    for (int i = 0; i < times; i++) {
                        rewards();
                    }
                }
            }.runTask(instance);
        } else{
            for (int i = 0; i < times; i++) {
                rewards();
            }
        }
        return this;
    }

    private void rewards() {
        boolean accumulateApplied = false;
        boolean accumulateFounded = false;

        Player player = actionResult.getPlayer();
        UUID uuid = player.getUniqueId();
        List<VariableArg> variables = actionResult.getVariables();
        BoosterItem boosterItem = actionResult.getBoosterItem();
        BoosterConfig boosterConfig = boosterItem.getBoosterConfig();
        MoActions actions = boosterConfig.getActions();

        List<MoRewards> approvedRewards = actionResult.getApprovedRewards();
        if (!actionResult.hasApprovedRewards()) {
            return;
        }

        for (MoRewards moRewards : approvedRewards) {
            for (MoReward moReward : moRewards.getRewards()) {

                double chance = UtilMethods.transformChance(uuid, moReward.getChance());
                double random = UtilMethods.randomNumber();
                if (random < chance) {
                    continue;
                }

                for (AvailableRewards available : moReward.getAvailableRewards()) {
                    if (!available.hasReward()) continue;

                    SelectedReward selected = available.getRandomReward();
                    RewardReceiver rewardReceiver = selected.getRewardReceiver();
                    LivingEntity entityReceiver = getEntityReceiver(player, variables, rewardReceiver);

                    String reward = UtilString.get(selected.getReward())
                            .hex().setDefaultNumberRandomVariable()
                            .setVariables(variables).setPlaceholders(entityReceiver)
                            .setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply();

                    if (reward == null) continue;

                    RewardType rewardType = selected.getRewardType();
                    EntityType entityReceiverType = (entityReceiver != null ? entityReceiver.getType() : EntityType.UNKNOWN);

                    if (executeDefaultRewards) {
                        if (rewardType.equals(RewardType.CONSOLE_COMMAND)) {
                            RewardMethods.consoleCommand(reward);
                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND)) {
                            RewardMethods.playerCommand(entityReceiver, reward);
                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND_AS_OP)) {
                            RewardMethods.playerCommandAsOP(entityReceiver, reward);
                        } else if (rewardType.equals(RewardType.MESSAGE)) {
                            RewardMethods.playerMessage(entityReceiver, reward);
                        } else if (rewardType.equals(RewardType.TITLE)) {
                            RewardMethods.playerTitle(entityReceiver, reward);
                        } else if (rewardType.equals(RewardType.SOUND)) {
                            RewardMethods.playerSound(entityReceiver, reward);
                        } else if (rewardType.equals(RewardType.BROADCAST_MESSAGE)) {
                            RewardMethods.broadcastMessage(reward);
                        } else if (rewardType.equals(RewardType.BROADCAST_TITLE)) {
                            RewardMethods.broadcastTitle(reward);
                        } else if (rewardType.equals(RewardType.JSON)) {
                            RewardMethods.json(entityReceiver, reward);
                        } else if (rewardType.equals(RewardType.JSON_BROADCAST)) {
                            RewardMethods.jsonBroadcast(reward);
                        } else if (rewardType.equals(RewardType.EFFECT)) {
                            RewardMethods.effect(entityReceiver, reward);
                        } else if (rewardType.equals(RewardType.EXECUTE_ACTION)) {
                            if (entityReceiverType == EntityType.PLAYER) {
                                MoAction rewardAction = actions.getAction(reward);
                                ExecuteAction exec = new ExecuteAction(rewardAction, (Player) entityReceiver, boosterItem, variables)
                                        .addAllDefaultVariables().check();
                                if (exec.cancelMessage()) this.cancelMessage = true;
                                if (exec.cancelBooster()) this.cancelBooster = true;
                                if (exec.cancelClaim()) this.cancelClaim = true;
                            }
                        } else if (rewardType.equals(RewardType.ADD_BOOST)) {
                            RewardMethods.addBoost(entityReceiver, reward, cancelMessage);
                        } else if (rewardType.equals(RewardType.REMOVE_BOOST)) {
                            RewardMethods.removeBoost(entityReceiver, reward, cancelMessage);
                        } else if (rewardType.equals(RewardType.ADD_TIME)) {
                            RewardMethods.addTime(entityReceiver, reward, cancelMessage);
                        } else if (rewardType.equals(RewardType.REMOVE_TIME)) {
                            RewardMethods.removeTime(entityReceiver, reward, cancelMessage);
                        } else if (rewardType.equals(RewardType.SET_BOOSTER)) {
                            RewardMethods.setBooster(entityReceiver, reward, cancelMessage);
                        } else if (rewardType.equals(RewardType.ACCUMULATE_BOOSTER)) {
                            accumulateFounded = true;
                            boolean cancelled = RewardMethods.accumulateBooster(entityReceiver, reward, cancelMessage);
                            if (!cancelled) accumulateApplied = true;
                        }
                    }

                    if (executeCancellationRewards) {
                        if (rewardType.equals(RewardType.CANCEL_MESSAGE)) {
                            cancelMessage = true;
                        } else if (rewardType.equals(RewardType.CANCEL_BOOSTER)) {
                            cancelBooster = true;
                        } else if (rewardType.equals(RewardType.CANCEL_CLAIM)) {
                            cancelClaim = true;
                        }
                    }
                }
            }
        }

        if (accumulateFounded) {
            cancelClaim = !accumulateApplied;
        }
    }


    /**
     * Obtain entityReceiver depending on type of receiver
     */
    private LivingEntity getEntityReceiver(Player executor, List<VariableArg> variables, RewardReceiver rewardReceiver) {
        if (rewardReceiver.getRewardReceiverType() == RewardReceiverType.PLACEHOLDER && rewardReceiver.hasRewardReceiver()) {
            String name = UtilString.get(rewardReceiver.getRewardReceiver())
                    .setDefaultNumberRandomVariable().setVariables(variables)
                    .setPlaceholders(executor).setChangeOutputPlaceholder()
                    .setMathPlaceholder().setTimeFormatter().apply();

            Player p = Bukkit.getPlayer(name);
            if (p != null && p.isOnline()) {
                return p;
            }
            return null;
        }
        return executor;
    }
}
