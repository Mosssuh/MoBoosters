package mv.mossuh.moboosters.MODEL.Booster.Duration;

import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.DATA.Config.Config.Config;

public class BoosterDuration {
    private long startTimeInMilliSeconds = 0;
    private long durationInSeconds = 0;

    public BoosterDuration(Long durationInSeconds) {
        if (durationInSeconds != null) { this.durationInSeconds = durationInSeconds; }
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public synchronized long getDuration() {
        return durationInSeconds;
    }
    public synchronized long getStartingTimeInMilliSeconds() { return startTimeInMilliSeconds; }

    public synchronized void addDuration(Long durationInSeconds) {
        long remainingDuration = getRemainingTime();
        if (durationInSeconds < 0) { durationInSeconds = 0L; }
        this.durationInSeconds = (remainingDuration + durationInSeconds);
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public synchronized void setDuration(Long durationInSeconds) {
        if (durationInSeconds < 0) { durationInSeconds = 0L; }
        this.durationInSeconds = durationInSeconds;
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public synchronized void removeDuration(Long durationInSeconds) {
        long remainingDuration = getRemainingTime();
        if (durationInSeconds < 0) { durationInSeconds = 0L; }

        long newDuration = remainingDuration - durationInSeconds;
        if (newDuration < 0) { newDuration = 0; }
        this.durationInSeconds = newDuration;
        this.startTimeInMilliSeconds = System.currentTimeMillis();
    }

    public synchronized long getRemainingTime() {
        return UtilMethods.showCooldown(durationInSeconds, startTimeInMilliSeconds);
    }

    public synchronized String getRemainingTimeFormatted() {
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
