package mv.mossuh.moboosters.APPLICATORS;

import mv.mossuh.moarmors.API.Events.PieceChangeExpEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DebugType;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
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

        double personal = 0;
        double global = 0;
        double superiorSkyblock2 = 0;

        if (identifier != null && armorIdentifier.isBoosterIdentifier()) {
            personal = ActiveBoosterManager.getBoosts(new PersonalBooster(uuid, new BoosterIdentifier(identifier, BoosterType.PERSONAL, ApplicatorType.MOPETS, "exp")), false);
            global = ActiveBoosterManager.getBoosts(new GlobalBooster(new BoosterIdentifier(identifier, BoosterType.GLOBAL, ApplicatorType.MOPETS, "exp")), false);
            superiorSkyblock2 = ActiveBoosterManager.getBoosts(new SuperiorSkyblock2Booster(UtilMethods.getIslandUUIDByPlayerUUID(uuid), new BoosterIdentifier(identifier, BoosterType.SUPERIORSKYBLOCK2, ApplicatorType.MOPETS, "exp")), false);
        } else {
            Map<BoosterType, Double> boosts = ActiveBoosterManager.getTotalBoostCached(uuid, applicator, boosted);
            personal = boosts.getOrDefault(BoosterType.PERSONAL, 0.0);
            global = boosts.getOrDefault(BoosterType.GLOBAL, 0.0);
            superiorSkyblock2 = boosts.getOrDefault(BoosterType.SUPERIORSKYBLOCK2, 0.0);
        }

        double total = personal+global+superiorSkyblock2;
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
