package mv.mossuh.moboosters.MODEL.Hook.Applicator;

import java.util.Set;
import java.util.UUID;

public interface StateApplicatorHook extends ApplicatorHook {
    String getBoosted();

    void onUpdateBoost(Set<UUID> affected, double newBoost, double oldBoost);
    void onRemoveBoost(Set<UUID> affected, double boostToRemove);
}
