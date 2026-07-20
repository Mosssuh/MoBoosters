package mv.mossuh.moboosters.BOOSTERS.BoosterTypes.BoostDuration;

import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.CONFIGS.Config.Config;

public class BoosterDuration {
    private long startTimeInMilliSeconds = 0;
    private long durationInSeconds = 0;

    public BoosterDuration(Long durationInSeconds) {
        if (durationInSeconds != null) { this.durationInSeconds = durationInSeconds; }
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public long getDuration() {
        return durationInSeconds;
    }
    public long getStartingTimeInMilliSeconds() { return startTimeInMilliSeconds; }

    public void addDuration(Long durationInSeconds) {
        long remainingDuration = getRemainingTime();
        if (durationInSeconds < 0) { durationInSeconds = 0L; }
        this.durationInSeconds = (remainingDuration + durationInSeconds);
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public void setDuration(Long durationInSeconds) {
        if (durationInSeconds < 0) { durationInSeconds = 0L; }
        this.durationInSeconds = durationInSeconds;
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public void removeDuration(Long durationInSeconds) {
        long remainingDuration = getRemainingTime();
        if (durationInSeconds < 0) { durationInSeconds = 0L; }

        long newDuration = remainingDuration - durationInSeconds;
        if (newDuration < 0) { newDuration = 0; }
        this.durationInSeconds = newDuration;
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public long getRemainingTime() {
        return UtilMethods.showCooldown(durationInSeconds, startTimeInMilliSeconds);
    }

    public String getRemainingTimeFormatted() {
        if (durationInSeconds <= 0) {
            return "0" + UtilString.get(Config.TIME_FORMAT_SECOND).hex().apply();
        } else {
            return UtilMethods.showCooldownFormatted(durationInSeconds, startTimeInMilliSeconds);
        }
    }

    public void resetBooster() {
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public void cancel() {
        this.startTimeInMilliSeconds = 0;
    }

}
