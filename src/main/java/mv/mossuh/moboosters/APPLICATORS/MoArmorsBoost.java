package mv.mossuh.moboosters.APPLICATORS;

import mv.mossuh.moarmors.API.Events.PieceChangeExpEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MoArmorsBoost implements Listener {

    @EventHandler
    public void boost(PieceChangeExpEvent event) {
        ReceiveType type = event.getReceiveType();
        if (!type.equals(ReceiveType.ADD)) return;

        UUID uuid = event.getUUID();

        Piece pet = event.getPiece();
        ConfigArmor config = pet.getConfigArmor();
        ArmorIdentifier armorIdentifier = config.getArmorIdentifier();

        String identifier = armorIdentifier.getBoosterIdentifier();
        ApplicatorType applicator = ApplicatorType.MOARMORS;
        String boosted = "exp";

        double total = 0;

        if (identifier != null && armorIdentifier.isBoosterIdentifier()) {
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
