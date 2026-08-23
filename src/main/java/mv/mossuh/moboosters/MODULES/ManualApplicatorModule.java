package mv.mossuh.moboosters.MODULES;

import java.util.Set;
import java.util.UUID;

public interface ManualApplicatorModule extends ApplicatorModule {
    String getBoosted();

    void onUpdateBoost(Set<UUID> affected, double newBoost, double oldBoost);
    void onRemoveBoost(Set<UUID> affected, double boostToRemove);
}
