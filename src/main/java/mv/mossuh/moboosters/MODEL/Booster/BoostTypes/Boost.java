package mv.mossuh.moboosters.MODEL.Booster.BoostTypes;

import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

public interface Boost {
    double getBoost();
    void addBoost(double boost);
    void setBoost(double boost);
    void removeBoost(double boost);
    DurationType getDurationType();
    boolean isActive();
    void cancel();
}
