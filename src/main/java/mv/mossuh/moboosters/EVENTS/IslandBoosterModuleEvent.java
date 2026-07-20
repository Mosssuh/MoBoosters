package mv.mossuh.moboosters.EVENTS;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.MANAGERS.IslandModuleManager;
import java.util.UUID;

public class IslandBoosterModuleEvent implements Listener {
    @EventHandler
    public void disband(IslandDisbandEvent event) {
        UUID islandUUID = event.getIsland().getUniqueId();
        IslandModuleManager.onIslandDisband(islandUUID);
    }

    @EventHandler
    public void quit(IslandQuitEvent event) {
        UUID islandUUID = event.getIsland().getUniqueId();
        UUID playerUUID = event.getPlayer().getUniqueId();
        IslandModuleManager.onIslandQuit(islandUUID, playerUUID);
    }

    @EventHandler
    public void kick(IslandKickEvent event) {
        UUID islandUUID = event.getIsland().getUniqueId();
        UUID playerUUID = event.getTarget().getUniqueId();
        IslandModuleManager.onIslandKick(islandUUID, playerUUID);
    }

    @EventHandler
    public void join(IslandJoinEvent event) {
        UUID islandUUID = event.getIsland().getUniqueId();
        UUID playerUUID = event.getPlayer().getUniqueId();
        IslandModuleManager.onIslandJoin(islandUUID, playerUUID);
    }
}
