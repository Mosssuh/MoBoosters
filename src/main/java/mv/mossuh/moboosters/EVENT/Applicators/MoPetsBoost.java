package mv.mossuh.moboosters.EVENT.Applicators;

import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.mopets.API.Events.PetChangeExpEvent;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.ENUMS.ReceiveType;
import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class MoPetsBoost extends EventApplicatorHook {

    public MoPetsBoost() {
        super(ApplicatorType.MOPETS);
    }

    @EventHandler
    public void exp(PetChangeExpEvent event) {
        ReceiveType type = event.getReceiveType();
        if (!type.equals(ReceiveType.ADD)) return;

        UUID uuid = event.getUUID();

        Pet pet = event.getPet();
        ConfigPet config = pet.getConfigPet();
        PetIdentifier petIdentifier = config.getPetIdentifier();

        String identifier = petIdentifier.getBoosterIdentifier();
        String boosted = "exp";

        double boost = 0;

        if (identifier != null && petIdentifier.isBoosterIdentifier()) {
            boost = resolveBoost(uuid, identifier, boosted);
        } else {
            boost = resolveBoost(uuid, null, boosted);
        }
        if (boost <= 0) return;

        double exp = event.getExp();
        double boostedExp = exp * boost;
        event.addExp(boostedExp);
    }
}
