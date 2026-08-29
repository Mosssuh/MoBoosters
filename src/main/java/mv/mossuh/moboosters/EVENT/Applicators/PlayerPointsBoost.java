package mv.mossuh.moboosters.EVENT.Applicators;

import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import org.black_ixx.playerpoints.event.PlayerPointsChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;


public class PlayerPointsBoost extends EventApplicatorHook implements Listener {

    public PlayerPointsBoost() {
        super(ApplicatorType.PLAYERPOINTS);
    }

    @EventHandler
    public void points(PlayerPointsChangeEvent event) {
        UUID uuid = event.getPlayerId();
        double obtained = event.getChange();
        String boosted = "points";

        double boost = resolveBoost(uuid, null, boosted);
        if (boost <= 0) return;

        double withBase = boost + 1;
        event.setChange((int) (obtained * withBase));
    }
}
