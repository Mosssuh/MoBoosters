package mv.mossuh.moboosters.BOOSTERS.Items;

import mv.mossuh.mocore.UTILITIES.ARGS.ArgType;
import mv.mossuh.mocore.UTILITIES.ARGS.Args;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;

public class BoosterItem {

    private String code = "";
    private String boost = "";
    private String duration = "PERM";
    private DurationType durationType = DurationType.PERM;
    private BoosterIdentifier identifier = new BoosterIdentifier();
    private BoosterConfig boosterConfig = new BoosterConfig();
    private CommandArgs args = new CommandArgs(null);
    private ItemStack itemStack = new ItemStack(Material.AIR, 1);
    public BoosterItem(String code, String boost, String duration, BoosterIdentifier identifier, BoosterConfig boosterConfig, Args args, ItemStack itemStack) {
        if (code != null) { this.code = code; }
        if (boost != null) { this.boost = boost; }
        if (duration != null && !duration.equalsIgnoreCase("-1") && !duration.equalsIgnoreCase("perm") && !duration.equalsIgnoreCase("permanent")) {
            this.duration = duration;
            this.durationType = DurationType.TEMP;
        }
        if (identifier != null) { this.identifier = identifier; }
        if (args != null && args.getArgType().equals(ArgType.COMMAND)) {
            this.args = (CommandArgs) args;
        }
        if (boosterConfig != null) { this.boosterConfig = boosterConfig; }
        if (itemStack != null) { this.itemStack = itemStack; }
    }
    public BoosterItem() {}

    public boolean isBoosterItem() {
        return !code.isEmpty() && !boost.isEmpty() && UtilMethods.isRegisteredBooster(identifier, durationType);
    }
    public boolean isBoosterItem(boolean checkBoosterConfig) {
        if (checkBoosterConfig) {
            return !code.isEmpty() && !boost.isEmpty() && UtilMethods.isRegisteredBooster(identifier, durationType) && boosterConfig.isBooster();
        } else {
            return !code.isEmpty() && !boost.isEmpty() && UtilMethods.isRegisteredBooster(identifier, durationType);
        }
    }

    public String getCode() { return code; }
    public String getBoost() { return boost; }
    public boolean isBoost() {
        return UtilMethods.isNumeric(boost);
    }
    public String getDuration() { return duration; }
    public boolean hasDuration() {
        return UtilMethods.isNumeric(duration);
    }
    public DurationType getDurationType() { return durationType; }
    public BoosterIdentifier getIdentifier() { return identifier; }
    public BoosterConfig getBoosterConfig() { return boosterConfig; }
    public CommandArgs getArgs() { return args; }
    public ItemStack getItemStack() { return itemStack; }
}
