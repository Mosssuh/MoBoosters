package mv.mossuh.moboosters.UTILITIES;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import mv.mossuh.moboosters.MoBoosters;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import org.bukkit.entity.Player;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MANAGER.ActiveBoosterManager;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

import java.util.UUID;

public class PAPI extends PlaceholderExpansion {
    private final MoBoosters instance = MoBoosters.getInstance();

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }
    /**
     * Since this expansion requires api access to the plugin "SomePlugin"
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "Mossuh";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "moboosters";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return instance.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        UUID uuid = player.getUniqueId();

        if (identifier.toLowerCase().startsWith("has_booster_")) {
            // %moboosters_has_booster_<duration type>:<identifier>:<booster type>:<applicator type>:<boosted>%
            String boosterInformation = identifier.replaceAll(" ", "").replace("has_booster_", "");
            String[] boosterInformationSplit = boosterInformation.split(":" ,5);
            if (boosterInformationSplit.length == 5) {
                DurationType durationType = UtilMethods.getDurationType(boosterInformationSplit[0]);
                String identifierString = boosterInformationSplit[1];
                BoosterType boosterType = UtilMethods.getBoosterType(boosterInformationSplit[2]);
                ApplicatorType applicatorType = ApplicatorType.convert(boosterInformationSplit[3]);
                String boosted = boosterInformationSplit[4];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifierString, boosterType, applicatorType, boosted);
                Booster booster = Booster.getBooster(boosterIdentifier, uuid);
                ActiveBooster active = ActiveBoosterManager.getActiveBooster(booster);
                return String.valueOf(active.getBoosts().getBoost(durationType).getBoost() > 0);
            }
        } else if (identifier.toLowerCase().startsWith("boost_with_base_")) {
            // %moboosters_boost_with_base_<duration type>:<identifier>:<booster type>:<applicator type>:<boosted>%
            String boosterInformation = identifier.replaceAll(" ", "").replace("boost_with_base_", "");
            String[] boosterInformationSplit = boosterInformation.split(":" ,5);
            if (boosterInformationSplit.length == 5) {
                DurationType durationType = UtilMethods.getDurationType(boosterInformationSplit[0]);
                String identifierString = boosterInformationSplit[1];
                BoosterType boosterType = UtilMethods.getBoosterType(boosterInformationSplit[2]);
                ApplicatorType applicatorType = ApplicatorType.convert(boosterInformationSplit[3]);
                String boosted = boosterInformationSplit[4];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifierString, boosterType, applicatorType, boosted);
                Booster booster = Booster.getBooster(boosterIdentifier, uuid);

                if (identifierString.equalsIgnoreCase("all")) {
                    double boost = ActiveBoosterManager.getBoost(durationType, booster, true);
                    return boost+"";
                }

                ActiveBooster active = ActiveBoosterManager.getActiveBooster(booster);
                return String.valueOf(active.getBoosts().getBoost(durationType).getBoost() + 1);
            }
        } else if (identifier.toLowerCase().startsWith("boost_")) {
            // %moboosters_boost_<duration type>:<identifier>:<booster type>:<applicator type>:<boosted>%
            String boosterInformation = identifier.replaceAll(" ", "").replace("boost_", "");
            String[] boosterInformationSplit = boosterInformation.split(":" ,5);
            if (boosterInformationSplit.length == 5) {
                DurationType durationType = UtilMethods.getDurationType(boosterInformationSplit[0]);
                String identifierString = boosterInformationSplit[1];
                BoosterType boosterType = UtilMethods.getBoosterType(boosterInformationSplit[2]);
                ApplicatorType applicatorType = ApplicatorType.convert(boosterInformationSplit[3]);
                String boosted = boosterInformationSplit[4];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifierString, boosterType, applicatorType, boosted);
                Booster booster = Booster.getBooster(boosterIdentifier, uuid);

                if (identifierString.equalsIgnoreCase("all")) {
                    double boost = ActiveBoosterManager.getBoost(durationType, booster, true);
                    return boost+"";
                }

                ActiveBooster active = ActiveBoosterManager.getActiveBooster(booster);
                return String.valueOf(active.getBoosts().getBoost(durationType).getBoost());
            }
        } else if (identifier.toLowerCase().startsWith("duration_")) {
            // %moboosters_duration_<identifier>:<booster type>:<applicator type>:<boosted>%
            String boosterInformation = identifier.replaceAll(" ", "").replace("duration_", "");
            String[] boosterInformationSplit = boosterInformation.split(":" ,4);
            if (boosterInformationSplit.length == 4) {
                //DurationType durationType = DurationType.TEMP;
                String identifierString = boosterInformationSplit[0];
                BoosterType boosterType = UtilMethods.getBoosterType(boosterInformationSplit[1]);
                ApplicatorType applicatorType = ApplicatorType.convert(boosterInformationSplit[2]);
                String boosted = boosterInformationSplit[3];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifierString, boosterType, applicatorType, boosted);
                Booster booster = Booster.getBooster(boosterIdentifier, uuid);
                ActiveBooster active = ActiveBoosterManager.getActiveBooster(booster);
                return String.valueOf(active.getBoosts().getTemporary().getDuration().getRemainingTime());
            }
        } else if (identifier.toLowerCase().startsWith("formatted_duration_")) {
            // %moboosters_formatted_duration_<identifier>:<booster type>:<applicator type>:<boosted>%
            String boosterInformation = identifier.replaceAll(" ", "").replace("formatted_duration_", "");
            String[] boosterInformationSplit = boosterInformation.split(":" ,4);
            if (boosterInformationSplit.length == 4) {
                //DurationType durationType = DurationType.TEMP;
                String identifierString = boosterInformationSplit[0];
                BoosterType boosterType = UtilMethods.getBoosterType(boosterInformationSplit[1]);
                ApplicatorType applicatorType = ApplicatorType.convert(boosterInformationSplit[2]);
                String boosted = boosterInformationSplit[3];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifierString, boosterType, applicatorType, boosted);
                Booster booster = Booster.getBooster(boosterIdentifier, uuid);
                ActiveBooster active = ActiveBoosterManager.getActiveBooster(booster);
                return String.valueOf(active.getBoosts().getTemporary().getDuration().getRemainingTimeFormatted());
            }
        }

        return "";
    }
}
