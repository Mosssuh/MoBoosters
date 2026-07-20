package mv.mossuh.moboosters.EVENTS;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.ENUMS.BoosterType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandDisband implements Listener {
    @EventHandler
    public void whenDisbandIsland(IslandDisbandEvent event) {
        Island island = event.getIsland();
        UUID islandUUID = island.getUniqueId();

        List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(BoosterType.SUPERIORSKYBLOCK2);
        List<Booster> islandActiveBoosters = new ArrayList<>();
        for (ActiveBooster activeBooster : activeBoosters) {
            SuperiorSkyblock2Booster booster = (SuperiorSkyblock2Booster) activeBooster.getBooster();
            if (booster.getUUID().equals(islandUUID)) {
                islandActiveBoosters.add(activeBooster.getBooster());
            }
        }

        BoostersAPI.getManager().removeBooster(islandActiveBoosters);
    }
}
