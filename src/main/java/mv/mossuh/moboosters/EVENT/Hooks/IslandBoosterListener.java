package mv.mossuh.moboosters.EVENT.Hooks;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandQuitEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.MANAGER.ActiveBoosterManager;
import mv.mossuh.moboosters.MANAGER.HooksManager;
import mv.mossuh.moboosters.HOOK.StateApplicatorHook;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class IslandBoosterListener implements Listener {

    private final CentralBoosterListener central;

    public IslandBoosterListener(CentralBoosterListener central) {
        this.central = central;
    }

    @EventHandler
    public void disband(IslandDisbandEvent event) {
        Island island = event.getIsland();
        UUID islandUUID = island.getUniqueId();
        Set<UUID> members = UtilMethods.getIslandMembers(islandUUID);

        UUID ownerUUID = island.getOwner().getUniqueId();

        for (StateApplicatorHook module : HooksManager.getStateApplicators()) {
            Map<BoosterType, Double> boosts = ActiveBoosterManager.getBoosts(ownerUUID, module.getApplicatorType(), module.getBoosted());

            double boost = boosts.get(BoosterType.SUPERIORSKYBLOCK2);
            if (boost > 0) {
                central.executor(members, module, boost, 0, true);
            }
        }
    }

    @EventHandler
    public void quit(IslandQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        Set<UUID> members = new HashSet<>(Collections.singleton(playerUUID));

        for (StateApplicatorHook module : HooksManager.getStateApplicators()) {
            Map<BoosterType, Double> boosts = ActiveBoosterManager.getBoosts(playerUUID, module.getApplicatorType(), module.getBoosted());

            double boost = boosts.get(BoosterType.SUPERIORSKYBLOCK2);
            if (boost > 0) {
                central.executor(members, module, boost, 0, true);
            }
        }
    }

    @EventHandler
    public void kick(IslandKickEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        Set<UUID> members = new HashSet<>(Collections.singleton(playerUUID));

        for (StateApplicatorHook module : HooksManager.getStateApplicators()) {
            Map<BoosterType, Double> boosts = ActiveBoosterManager.getBoosts(playerUUID, module.getApplicatorType(), module.getBoosted());

            double boost = boosts.get(BoosterType.SUPERIORSKYBLOCK2);
            if (boost > 0) {
                central.executor(members, module, boost, 0, true);
            }
        }
    }

    @EventHandler
    public void join(IslandJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        Set<UUID> members = new HashSet<>(Collections.singleton(playerUUID));

        for (StateApplicatorHook module : HooksManager.getStateApplicators()) {
            Map<BoosterType, Double> boosts = ActiveBoosterManager.getBoosts(playerUUID, module.getApplicatorType(), module.getBoosted());

            double boost = boosts.get(BoosterType.SUPERIORSKYBLOCK2);
            if (boost > 0) {
                central.executor(members, module, boost, 0, false);
            }
        }
    }
}
