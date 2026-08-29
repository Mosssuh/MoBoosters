package mv.mossuh.moboosters.EVENT;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MANAGER.ActiveBoosterManager;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;

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
