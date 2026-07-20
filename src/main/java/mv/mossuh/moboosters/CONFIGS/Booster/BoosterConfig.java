package mv.mossuh.moboosters.CONFIGS.Booster;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoActions;
import mv.mossuh.moboosters.ENUMS.DurationType;

public class BoosterConfig {
    private String code = "";
    private String boost = "";
    private String duration = "PERM";
    private DurationType durationType = DurationType.PERM;
    private BoosterIdentifier identifier = new BoosterIdentifier(null, null, null, null);
    private BoosterInfo info = new BoosterInfo(null, null, null, null, null, null, null, null);
    private MoActions actions = new MoActions(null);
    public BoosterConfig(String code, String boost, String duration, BoosterIdentifier identifier, BoosterInfo info, MoActions actions) {
        if (code != null) { this.code = code; }
        if (boost != null) { this.boost = boost; }
        if (duration != null && !duration.equalsIgnoreCase("-1") && !duration.equalsIgnoreCase("perm") && !duration.equalsIgnoreCase("permanent")) {
            this.duration = duration;
            this.durationType = DurationType.TEMP;
        }
        if (identifier != null) { this.identifier = identifier; }
        if (info != null) { this.info = info; }
        if (actions != null) { this.actions = actions; }
    }
    public BoosterConfig() {}

    public boolean isBooster() {
        return !code.isEmpty() && !boost.isEmpty() && identifier.isIdentifier();
    }

    public String getCode() { return code; }
    public String getBoost() { return boost; }
    public String getDuration() { return duration; }
    public DurationType getDurationType() { return durationType; }
    public BoosterIdentifier getIdentifier() { return identifier; }
    public BoosterInfo getInfo() { return info; }
    public MoActions getActions() { return actions; }
}
