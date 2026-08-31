package mv.mossuh.moboosters.EVENT.Applicators;

import mv.mossuh.moarmors.API.Events.PieceChangeExpEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class MoArmorsBoost extends EventApplicatorHook {

    public MoArmorsBoost() {
        super(ApplicatorType.MOARMORS);
    }

    @EventHandler
    public void exp(PieceChangeExpEvent event) {
        ReceiveType type = event.getReceiveType();
        if (!type.equals(ReceiveType.ADD)) return;

        UUID uuid = event.getUUID();

        Piece pet = event.getPiece();
        ConfigArmor config = pet.getConfigArmor();
        ArmorIdentifier armorIdentifier = config.getArmorIdentifier();

        String identifier = armorIdentifier.getBoosterIdentifier();
        String boosted = "exp";

        double boost = 0;
        if (identifier != null && armorIdentifier.isBoosterIdentifier()) {
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
