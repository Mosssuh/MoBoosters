package mv.mossuh.moboosters.APPLICATORS;

import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.mopets.API.Events.PetChangeExpEvent;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.ENUMS.ReceiveType;
import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MoPetsBoost implements Listener {

    @EventHandler
    public void boost(PetChangeExpEvent event) {
        ReceiveType type = event.getReceiveType();
        if (!type.equals(ReceiveType.ADD)) return;

        UUID uuid = event.getUUID();

        Pet pet = event.getPet();
        ConfigPet config = pet.getConfigPet();
        PetIdentifier petIdentifier = config.getPetIdentifier();

        String identifier = petIdentifier.getBoosterIdentifier();
        ApplicatorType applicator = ApplicatorType.MOPETS;
        String boosted = "exp";

        double total = 0;

        if (identifier != null && petIdentifier.isBoosterIdentifier()) {
            total = ActiveBoosterManager.getTotalBoostCached(uuid, identifier, applicator, boosted);
        } else {
            total = ActiveBoosterManager.getTotalBoostCached(uuid, applicator, boosted);
        }
        PlayerApplyBoostEvent playerApplyBoostEvent = new PlayerApplyBoostEvent(uuid, total, applicator, boosted);
        Bukkit.getPluginManager().callEvent(playerApplyBoostEvent);

        if (playerApplyBoostEvent.isCancelled()) {
            return;
        }

        double eventBoost = playerApplyBoostEvent.getBoost();
        if (eventBoost > 0) {
            UtilString.get(Config.PREFIX + " &c(" + applicator.name() + ")&8: &aApplying the &2x" + eventBoost + " &aboost to &2" + boosted)
                    .hex().sendMessageInConsole(DebugType.APPLICATORS);
            double exp = event.getExp();
            double boostedExp = exp * eventBoost;
            event.addExp(boostedExp);
        }
    }
}
