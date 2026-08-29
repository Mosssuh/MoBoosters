package mv.mossuh.moboosters.HOOK;

import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.DebugType;
import mv.mossuh.moboosters.MANAGER.ActiveBoosterManager;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class EventApplicatorHook implements ApplicatorHook, Listener {
    private final ApplicatorType applicator;

    public EventApplicatorHook(ApplicatorType applicator) {
        this.applicator = applicator != null ? applicator : ApplicatorType.NONE;
    }

    @Override
    public ApplicatorType getApplicatorType() { return applicator; }

    /*
    The identifier can be null to obtain all the boosts of the same APPLICATOR and BOOSTED.
     */
    protected final double resolveBoost(UUID uuid, String identifier, String boosted) {
        double total = 0;
        if (identifier == null) {
            total = ActiveBoosterManager.getTotalBoostCached(uuid, applicator, boosted);
        } else {
            total = ActiveBoosterManager.getTotalBoostCached(uuid, identifier, applicator, boosted);
        }

        PlayerApplyBoostEvent event = new PlayerApplyBoostEvent(uuid, total, applicator, boosted);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return 0;

        double boost = event.getBoost();
        if (boost > 0) {
            UtilString.get(Config.PREFIX + " &c(" + applicator.name() + ")&8: &aApplying the &2x" + boost + " &aboost to &2" + boosted)
                    .hex().sendMessageInConsole(DebugType.APPLICATORS);
        }
        return boost;
    }
}
