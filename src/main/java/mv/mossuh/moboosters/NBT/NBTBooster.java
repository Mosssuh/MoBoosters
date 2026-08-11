package mv.mossuh.moboosters.NBT;

import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.mocore.NBT.NBTMethods;
import org.bukkit.inventory.ItemStack;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;

import java.util.UUID;

public class NBTBooster {

    public static void setHeadTexture(ItemStack itemStack, String value) {
        NBTMethods.setHeadTexture(itemStack, value);
    }
    public static void setCode(ItemStack itemStack, String code) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"Code", code);
    }
    public static void setIdentifier(ItemStack itemStack, String identifier) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"Identifier", identifier);
    }
    public static void setBoosterType(ItemStack itemStack, BoosterType boosterType) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"Type", boosterType.name());
    }
    public static void setApplicator(ItemStack itemStack, ApplicatorType applicatorType) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"Applicator", applicatorType.name());
    }
    public static void setBoosted(ItemStack itemStack, String boosted) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"Boosted", boosted);
    }
    public static void setBoost(ItemStack itemStack, String boost) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"Boost", boost);
    }
    public static void setDuration(ItemStack itemStack, String duration) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"Duration", duration);
    }
    public static void setUnique(ItemStack itemStack) {
        NBTMethods.setString(itemStack, "vItemUniqueID", UUID.randomUUID().toString());
    }
    public static void setArgs(ItemStack itemStack, String args) {
        NBTMethods.setString(itemStack, "vItemArgs", args);
    }


    public static String getCode(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"Code");
    }
    public static String getIdentifier(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"Identifier");
    }
    public static BoosterType getBoosterType(ItemStack itemStack) {
        return UtilMethods.getBoosterType(NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"Type"));
    }
    public static ApplicatorType getApplicator(ItemStack itemStack) {
        return ApplicatorType.convert(NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"Applicator"));
    }
    public static String getBoosted(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"Boosted");
    }
    public static String getBoost(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"Boost");
    }
    public static String getDuration(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"Duration");
    }
    public static Boolean isUnique(ItemStack itemStack) {
        String id = NBTMethods.getString(itemStack, "vItemUniqueID");
        return id != null;
    }
    public static String getArgs(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, "vItemArgs");
    }

}
