package mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes;

import mv.mossuh.moboosters.ENUMS.DurationType;

public interface Boost {
    double getBoost();
    void addBoost(double boost);
    void setBoost(double boost);
    void removeBoost(double boost);
    DurationType getDurationType();
    boolean isActive();
    void cancel();
}
