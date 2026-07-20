package mv.mossuh.moboosters.DATA.Datas;

import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.Boost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoosterDuration;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.CONFIGS.Configs;
import mv.mossuh.moboosters.MoBoosters;

import java.sql.*;
import java.util.*;

public class BoosterGlobalData {
    private static final Configs configs = MoBoosters.getInstance().getConfigs();
    public void registerDatabaseInMaps() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getGlobalBoostersData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS PermanentGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

            // identifier, booster type, applicator type, boosted, boost, remainingDuration

            ResultSet rsTemporary = stmt.executeQuery("SELECT * FROM TemporaryGlobalBoosters");
            while (rsTemporary.next()) {
                String identifier = rsTemporary.getString("identifier");
                BoosterType boosterType = BoosterType.GLOBAL;
                ApplicatorType applicatorType = UtilMethods.getApplicatorType(rsTemporary.getString("applicatorType"));
                String boosted = rsTemporary.getString("boosted");
                double boost = rsTemporary.getDouble("boost");
                long remainingDuration = rsTemporary.getLong("remainingDuration");

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                GlobalBooster booster = new GlobalBooster(boosterIdentifier);
                TemporaryBoost boosterBoost = new TemporaryBoost(boost, new BoosterDuration(remainingDuration));

                ActiveBoosterManager.setActiveBooster(booster, boosterBoost);
            }

            ResultSet rsPermanent = stmt.executeQuery("SELECT * FROM PermanentGlobalBoosters");
            while (rsPermanent.next()) {
                String identifier = rsPermanent.getString("identifier");
                BoosterType boosterType = BoosterType.GLOBAL;
                ApplicatorType applicatorType = UtilMethods.getApplicatorType(rsPermanent.getString("applicatorType"));
                String boosted = rsPermanent.getString("boosted");
                double boost = rsPermanent.getDouble("boost");

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                GlobalBooster booster = new GlobalBooster(boosterIdentifier);
                PermanentBoost boosterBoost = new PermanentBoost(boost);

                ActiveBoosterManager.setActiveBooster(booster, boosterBoost);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void registerMapsInDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getGlobalBoostersData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS PermanentGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

            List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(BoosterType.GLOBAL);

            if (!activeBoosters.isEmpty()) {
                for (ActiveBooster activeBooster : activeBoosters) {
                    GlobalBooster booster = (GlobalBooster) activeBooster.getBooster();
                    if (booster.isValid()) {
                        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                        String identifier = boosterIdentifier.getIdentifier();
                        String applicatorType = boosterIdentifier.getApplicatorType().name();
                        String boosted = boosterIdentifier.getBoosted();
                        PermanentBoost permanentBoost = activeBooster.getBoosts().getPermanent();
                        TemporaryBoost temporaryBoost = activeBooster.getBoosts().getTemporary();

                        if (permanentBoost.isActive()) {
                            double boost = permanentBoost.getBoost();
                            stmt.execute("INSERT OR REPLACE INTO PermanentGlobalBoosters (identifier, applicatorType, boosted, boost) VALUES('" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + boost + "')");
                        } else {
                            stmt.execute("DELETE FROM PermanentGlobalBoosters WHERE identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        }

                        if (temporaryBoost.isActive()) {
                            double boost = temporaryBoost.getBoost();
                            long remainingDuration = temporaryBoost.getDuration().getRemainingTime();
                            stmt.execute("INSERT OR REPLACE INTO TemporaryGlobalBoosters (identifier, applicatorType, boosted, boost, remainingDuration) VALUES('" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + boost + "', '" + remainingDuration + "')");
                        } else {
                            stmt.execute("DELETE FROM TemporaryGlobalBoosters WHERE identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        }
                    }
                }
            }

            stmt.execute("VACUUM");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerActiveBooster(GlobalBooster booster, Boost boost) {
        try {
            if (booster.isValid()) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getGlobalBoostersData().getFile().getAbsolutePath());
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
                stmt.execute("CREATE TABLE IF NOT EXISTS PermanentGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

                DurationType durationType = boost.getDurationType();
                BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                String identifier = boosterIdentifier.getIdentifier();
                String applicatorType = boosterIdentifier.getApplicatorType().name();
                String boosted = boosterIdentifier.getBoosted();
                double b = boost.getBoost();

                if (durationType.equals(DurationType.PERM)) {
                    if (boost.isActive()) {
                        stmt.execute("INSERT OR REPLACE INTO PermanentGlobalBoosters (identifier, applicatorType, boosted, boost) VALUES('" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + b + "')");
                    }
                } else if (durationType.equals(DurationType.TEMP)) {
                    TemporaryBoost temporaryBoost = (TemporaryBoost) boost;
                    long remainingDuration = temporaryBoost.getDuration().getRemainingTime();
                    stmt.execute("INSERT OR REPLACE INTO TemporaryGlobalBoosters (identifier, applicatorType, boosted, boost, remainingDuration) VALUES('" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + b + "', '" + remainingDuration + "')");
                }

                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeActiveBooster(DurationType durationType, GlobalBooster booster) {
        removeActiveBoosterMethod(durationType, new ArrayList<>(Collections.singleton(booster)));
    }
    public void removeActiveBooster(DurationType durationType, List<GlobalBooster> boosters) {
        removeActiveBoosterMethod(durationType, boosters);
    }
    public void removeActiveBoosterMethod(DurationType durationType, List<GlobalBooster> boosters) {
        try {
            if (!boosters.isEmpty()) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getGlobalBoostersData().getFile().getAbsolutePath());
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
                stmt.execute("CREATE TABLE IF NOT EXISTS PermanentGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

                for (GlobalBooster booster : boosters) {
                    if (booster.isValid()) {
                        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                        String identifier = boosterIdentifier.getIdentifier();
                        String applicatorType = boosterIdentifier.getApplicatorType().name();
                        String boosted = boosterIdentifier.getBoosted();

                        if (durationType.equals(DurationType.TEMP)) {
                            stmt.execute("DELETE FROM TemporaryPersonalBoosters WHERE identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        } else if (durationType.equals(DurationType.PERM)) {
                            stmt.execute("DELETE FROM PermanentPersonalBoosters WHERE identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        }
                    }
                }
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeActiveBooster(GlobalBooster booster) {
        removeActiveBoosterMethod(new ArrayList<>(Collections.singleton(booster)));
    }
    public void removeActiveBooster(List<GlobalBooster> boosters) {
        removeActiveBoosterMethod(boosters);
    }
    public void removeActiveBoosterMethod(List<GlobalBooster> boosters) {
        try {
            if (!boosters.isEmpty()) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getGlobalBoostersData().getFile().getAbsolutePath());
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
                stmt.execute("CREATE TABLE IF NOT EXISTS PermanentGlobalBoosters (identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

                for (GlobalBooster booster : boosters) {
                    if (booster.isValid()) {
                        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                        String identifier = boosterIdentifier.getIdentifier();
                        String applicatorType = boosterIdentifier.getApplicatorType().name();
                        String boosted = boosterIdentifier.getBoosted();

                        stmt.execute("DELETE FROM TemporaryPersonalBoosters WHERE identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        stmt.execute("DELETE FROM PermanentPersonalBoosters WHERE identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                    }
                }
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
