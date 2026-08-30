package mv.mossuh.moboosters.EVENT;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.API.Events.ClaimItemBoosterEvent;
import mv.mossuh.moboosters.ACTIONS.ActionResult;
import mv.mossuh.moboosters.ACTIONS.Requirements;
import mv.mossuh.moboosters.ACTIONS.Rewards;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.MODEL.Booster.BoosterItem;
import mv.mossuh.moboosters.MANAGER.BoosterCreator;
import mv.mossuh.moboosters.UTILITIES.Enums.DebugType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.DATA.Config.Messages;

import java.util.List;
import java.util.UUID;

public class ClaimItemBooster implements Listener {
    private static final int cooldownInterval = Config.CLAIM_BOOSTER_ITEM_COOLDOWN;
    private static final DebugType debugType  = DebugType.CLAIM_BOOSTER;

    @EventHandler
    public void rightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        BoosterItem boosterItem = BoosterCreator.get(itemStack);
        if (!boosterItem.isBoosterItem()) return;

        event.setCancelled(true);

        UtilString.get(Config.PREFIX + " &b&lCLAIMING BOOSTER").hex().sendMessageInConsole(debugType);
        UtilString.get(Config.PREFIX + " &fBy: &7" + playerName).hex().sendMessageInConsole(debugType);

        DurationType durationType = boosterItem.getDurationType();
        BoosterIdentifier boosterIdentifier = boosterItem.getIdentifier();
        double boost = Double.parseDouble(boosterItem.getBoost());
        long duration = durationType.equals(DurationType.TEMP) ? Math.round(Double.parseDouble(boosterItem.getDuration())) : 0;

        if (!Config.IDENTIFIERS.hasIdentifier(boosterIdentifier.getIdentifier())) {
            UtilString.get(Messages.INVALID_IDENTIFIER).hex().sendMessage(player);
            UtilString.get(Config.PREFIX + " &fRegisted Identifier: &cfalse").hex().sendMessageInConsole(debugType);
            UtilString.get(Config.PREFIX + " &&8&m---------------------").hex().sendMessageInConsole(debugType);
            return;
        }

        UtilString.get(Config.PREFIX + " &fRegisted Identifier: &atrue").hex().sendMessageInConsole(debugType);

        String cooldownKey = Config.PLUGIN_NAME+"-ClaimItemBooster-rightClick-" + uuid;
        if (Cooldown.startAndIsOnCooldown(cooldownKey, cooldownInterval)) {
            String cooldownFormatted = Cooldown.showCooldownFormatted(cooldownKey, 2, Config.TIME_FORMAT);
            UtilString.get(Messages.COOLDOWN).hex()
                    .setPlaceholders(player)
                    .replaceString("%cooldown%", cooldownFormatted)
                    .sendMessage(player);
            UtilString.get(Config.PREFIX + " &fCooldown: &c"+cooldownFormatted).hex().sendMessageInConsole(debugType);
            UtilString.get(Config.PREFIX + " &&8&m---------------------").hex().sendMessageInConsole(debugType);
            return;
        }

        UtilString.get(Config.PREFIX + " &fCooldown: &aAVAILABLE").hex().sendMessageInConsole(debugType);

        ClaimItemBoosterEvent claimEvent = new ClaimItemBoosterEvent(player, boosterItem);
        Bukkit.getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) {
            UtilString.get(Config.PREFIX + " &cCancelled by Event").hex().sendMessageInConsole(debugType);
            UtilString.get(Config.PREFIX + " &&8&m---------------------").hex().sendMessageInConsole(debugType);
            return;
        };

        CommandArgs args = boosterItem.getArgs();
        BoosterType boosterType = boosterIdentifier.getBoosterType();
        boolean isAccumulative = Config.ACCUMULATIVE_BOOSTERS;

        Requirements requirements = new Requirements(player, boosterItem)
                .addAllDefaultVariables()
                .addArgsVariables(args)
                .check();
        ActionResult result = requirements.getActionResult();
        Rewards rewards = null;
        boolean cancelledMessage = false, cancelledBooster = false, cancelledClaim = false;

        if (result.hasApprovedRewards()) {
            rewards = new Rewards(result).executeCancellations().check();
            cancelledMessage = rewards.cancelMessage();
            cancelledBooster = rewards.cancelBooster();
            cancelledClaim = rewards.cancelClaim();
        }

        Booster booster = Booster.getBooster(boosterIdentifier, uuid);
        if (!booster.isValid()) {
            UtilString.get(Config.PREFIX + " &fBooster Info: &cINVALID").hex().sendMessageInConsole(debugType);
            UtilString.get(Config.PREFIX + " &&8&m---------------------").hex().sendMessageInConsole(debugType);
            return;
        };

        UtilString.get(Config.PREFIX + " &fBooster Info:").hex().sendMessageInConsole(debugType);
        UtilString.get(Config.PREFIX + " &f   > Identifier: &7" + boosterIdentifier.getIdentifier()).hex().sendMessageInConsole(debugType);
        UtilString.get(Config.PREFIX + " &f   > Booster Type: &7" + boosterIdentifier.getBoosterType().name()).hex().sendMessageInConsole(debugType);
        UtilString.get(Config.PREFIX + " &f   > Applicator: &7" + boosterIdentifier.getApplicatorType().name()).hex().sendMessageInConsole(debugType);
        UtilString.get(Config.PREFIX + " &f   > Boosted: &7" + boosterIdentifier.getBoosted()).hex().sendMessageInConsole(debugType);
        UtilString.get(Config.PREFIX + " &f   > Boost: &7x" + boost).hex().sendMessageInConsole(debugType);
        if (durationType.equals(DurationType.PERM)) {
            UtilString.get(Config.PREFIX + " &f   > Duration: &7PERM").hex().sendMessageInConsole(debugType);
        } else {
            UtilString.get(Config.PREFIX + " &f   > Duration: &7" + UtilMethods.showCooldownFormatted(duration, System.currentTimeMillis())).hex().sendMessageInConsole(debugType);
        }
        if (!cancelledMessage) {
            UtilString.get(Config.PREFIX + " &fMessage: &aSended").hex().sendMessageInConsole(debugType);
        } else {
            UtilString.get(Config.PREFIX + " &fMessage: &cCancelled").hex().sendMessageInConsole(debugType);
        }

        boolean appliedBooster = true;
        String appliedWay = "&cAlready Active";
        ActiveBooster activeBooster = BoostersAPI.getManager().getBooster(booster);

        if (!cancelledBooster) {
            if (durationType.equals(DurationType.TEMP)) {
                if (!activeBooster.isTempActive()) {
                    String message = UtilMethods.getTempBoosterStartMessage(boosterType);
                    sendMessageToReceiver(message, boosterType, player, args, boosterItem, cancelledMessage);
                    BoostersAPI.getManager().setTempBoost(booster, boost, duration);
                    appliedWay = "&aActivated";
                } else if (isAccumulative && activeBooster.getBoosts().getTemporary().getBoost() == boost) {
                    String message = UtilMethods.getTempBoosterAddTimeMessage(boosterType);
                    sendMessageToReceiver(message, boosterType, player, args, boosterItem, cancelledMessage);
                    BoostersAPI.getManager().addTime(booster, duration);
                    appliedWay = "&aAdded Time";
                } else {
                    // Another booster with different boost active
                    String message = UtilMethods.getBoosterAlreadyActive(boosterType);
                    sendMessageToReceiver(message, boosterType, player, args, boosterItem, cancelledMessage);
                    appliedBooster = false;
                }
            } else {
                if (!activeBooster.isPermActive()) {
                    String message = UtilMethods.getBoosterSetBoostMessage(boosterType);
                    sendMessageToReceiver(message, boosterType, player, args, boosterItem, cancelledMessage);
                    BoostersAPI.getManager().setPermBoost(booster, boost);
                    appliedWay = "&aActivated";
                } else {
                    if (isAccumulative) {
                        String message = UtilMethods.getBoosterAddBoostMessage(boosterType);
                        sendMessageToReceiver(message, boosterType, player, args, boosterItem, cancelledMessage);
                        BoostersAPI.getManager().addBoost(durationType, booster, boost);
                        appliedWay = "&aAdded Boost";
                    } else {
                        String message = UtilMethods.getBoosterSetBoostMessage(boosterType);
                        sendMessageToReceiver(message, boosterType, player, args, boosterItem, cancelledMessage);
                        BoostersAPI.getManager().setPermBoost(booster, boost);
                        appliedWay = "&aActivated";
                    }
                }
            }
        }
        UtilString.get(Config.PREFIX + " &fBooster: " + appliedWay).hex().sendMessageInConsole(debugType);
        if (!appliedBooster)  {
            UtilString.get(Config.PREFIX + " &&8&m---------------------").hex().sendMessageInConsole(debugType);
            return;
        };

        if (rewards != null) {
            rewards.executeDefault().check();
            cancelledClaim = rewards.cancelClaim();
            UtilString.get(Config.PREFIX + " &fRewards: &aExecuting...").hex().sendMessageInConsole(debugType);
        } else {
            UtilString.get(Config.PREFIX + " &fRewards: &cNo Approved").hex().sendMessageInConsole(debugType);
        }

        if (!cancelledClaim) {
            UtilString.get(Config.PREFIX + " &fClaim: &aClaimed").hex().sendMessageInConsole(debugType);
        } else {
            UtilString.get(Config.PREFIX + " &fClaim: &cCancelled").hex().sendMessageInConsole(debugType);
        }
        UtilString.get(Config.PREFIX + " &&8&m---------------------").hex().sendMessageInConsole(debugType);

        if (!cancelledClaim) {
            int amount = itemStack.getAmount() - 1;
            itemStack.setAmount(amount);

            if (ServerVersion.getCurrentVersion().equals(ServerVersion.MC1_8)) {
                player.getInventory().setItemInHand(itemStack);
                return;
            }

            EquipmentSlot hand = event.getHand();
            if (hand == EquipmentSlot.HAND) player.getInventory().setItemInMainHand(itemStack);
            else if (hand == EquipmentSlot.OFF_HAND) player.getInventory().setItemInOffHand(itemStack);

            player.updateInventory();
        }
    }

    private void sendMessageToReceiver(String messageReceiver, BoosterType boosterType, Player player, CommandArgs args, BoosterItem boosterItem, boolean cancelledMessage) {
        if (!cancelledMessage) {
            if (boosterType.equals(BoosterType.PERSONAL)) {
                UtilString.get(messageReceiver).setVariables(player).setVariables(boosterItem)
                        .setArgs(args).setPlaceholders(player).hex().sendMessage(player);
            } else if (boosterType.equals(BoosterType.GLOBAL)) {
                UtilString.get(messageReceiver).setVariables(player).setVariables(boosterItem)
                        .setArgs(args).setPlaceholders(player).hex().sendMessageToOnlinePlayers();
            } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2) && PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
                SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                if (superiorPlayer.hasIsland()) {
                    List<SuperiorPlayer> islandPlayers = superiorPlayer.getIsland().getIslandMembers(true);
                    for (SuperiorPlayer sPlayer : islandPlayers) {
                        if (sPlayer.isOnline()) {
                            UtilString.get(messageReceiver).setVariables(player).setVariables(boosterItem)
                                    .setArgs(args).setPlaceholders(player).hex().sendMessage(sPlayer.getUniqueId());
                        }
                    }
                }
            }
        }
    }
}
