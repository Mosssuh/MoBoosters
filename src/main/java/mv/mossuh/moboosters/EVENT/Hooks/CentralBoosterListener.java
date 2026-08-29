package mv.mossuh.moboosters.EVENT.Hooks;

import mv.mossuh.moboosters.API.Events.*;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DebugType;
import mv.mossuh.moboosters.MANAGER.HooksManager;
import mv.mossuh.moboosters.HOOK.StateApplicatorHook;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class CentralBoosterListener implements Listener {

    @EventHandler
    private void onTemporaryActivate(ActivateTemporaryBoosterEvent event) {
        if (event.isCancelled()) return;
        Booster booster = event.getBooster();
        BoosterIdentifier identifier = booster.getIdentifier();
        StateApplicatorHook module = HooksManager.getStateApplicator(identifier.getApplicatorType(), identifier.getBoosted());
        Set<UUID> players = members(event.getBooster());
        executor(players, module, event.getBoost().getBoost(), event.getOldBoost().getBoost(), false);
    }

    @EventHandler
    private void onPermanentActivate(ActivatePermanentBoosterEvent event) {
        if (event.isCancelled()) return;
        Booster booster = event.getBooster();
        BoosterIdentifier identifier = booster.getIdentifier();
        StateApplicatorHook module = HooksManager.getStateApplicator(identifier.getApplicatorType(), identifier.getBoosted());
        Set<UUID> players = members(event.getBooster());
        executor(players, module, event.getBoost().getBoost(), event.getOldBoost().getBoost(), false);
    }

    @EventHandler
    private void onIncreaseBoost(IncreaseBoostBoosterEvent event) {
        if (event.isCancelled()) return;
        Booster booster = event.getBooster();
        BoosterIdentifier identifier = booster.getIdentifier();
        StateApplicatorHook module = HooksManager.getStateApplicator(identifier.getApplicatorType(), identifier.getBoosted());
        Set<UUID> players = members(event.getBooster());
        executor(players, module, event.getBoost(), 0, false);
    }

    @EventHandler
    private void onRemoveBoost(RemoveBoostBoosterEvent event) {
        if (event.isCancelled()) return;
        Booster booster = event.getBooster();
        BoosterIdentifier identifier = booster.getIdentifier();
        StateApplicatorHook module = HooksManager.getStateApplicator(identifier.getApplicatorType(), identifier.getBoosted());
        Set<UUID> players = members(event.getBooster());
        executor(players, module, event.getBoost(), 0, true);
    }

    @EventHandler
    private void onTemporaryEnds(EndsTemporaryBoosterEvent event) {
        Booster booster = event.getBooster();
        BoosterIdentifier identifier = booster.getIdentifier();
        StateApplicatorHook module = HooksManager.getStateApplicator(identifier.getApplicatorType(), identifier.getBoosted());
        Set<UUID> players = members(event.getBooster());

        executor(players, module, event.getBoost().getBoost(), 0, true);
    }

    private Set<UUID> members(Booster booster) {
        Set<UUID> players = new HashSet<>();
        BoosterType type = booster.getBoosterType();
        switch (type) {
            case PERSONAL:
                players.add(booster.getUUID());
                break;
            case GLOBAL:
                break;
            case SUPERIORSKYBLOCK2:
                UtilMethods.getIslandMembers(booster.getUUID());
                break;
        }
        return players;
    }

    public void executor(Set<UUID> players, StateApplicatorHook module, double currentBoost, double oldBoost, boolean isRemoval) {
        if (module == null) return;

        if (!isRemoval && currentBoost == oldBoost) return;

        ApplicatorType applicator = module.getApplicatorType();
        String boosted = module.getBoosted();

        String actionType = isRemoval ? "Removing" : "Updating";
        UtilString.get(Config.PREFIX + " &c(" + applicator.name() + ")&8: &a" + actionType + " boost of &2x" + currentBoost + " &afor &2" + boosted)
                .hex().sendMessageInConsole(DebugType.APPLICATORS);
        if (isRemoval) {
            module.onRemoveBoost(players, currentBoost);
        } else {
            module.onUpdateBoost(players, currentBoost, oldBoost);
        }
    }
}
