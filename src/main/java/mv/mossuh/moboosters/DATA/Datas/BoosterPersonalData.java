package mv.mossuh.moboosters.DATA.Datas;

import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.BOOSTERS.BoostTypes.Boost;
import mv.mossuh.moboosters.BOOSTERS.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.BOOSTERS.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.MANAGERS.ActiveBoosterManager;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoosterDuration;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DurationType;
import mv.mossuh.moboosters.CONFIGS.Configs;
import mv.mossuh.moboosters.MoBoosters;

import java.sql.*;
import java.util.*;

public class BoosterPersonalData {
    private static Configs configs = MoBoosters.getInstance().getConfigs();
    public void registerDatabaseInMaps() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getPersonalBoostersData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS PermanentPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

            // uuid, identifier, booster type, applicator type, boosted, boost, remainingDuration

            ResultSet rsTemporary = stmt.executeQuery("SELECT * FROM TemporaryPersonalBoosters");
            while (rsTemporary.next()) {
                UUID uuid = UUID.fromString(rsTemporary.getString("uuid"));
                String identifier = rsTemporary.getString("identifier");
                BoosterType boosterType = BoosterType.PERSONAL;
                ApplicatorType applicatorType = ApplicatorType.convert(rsTemporary.getString("applicatorType"));
                String boosted = rsTemporary.getString("boosted");
                double boost = rsTemporary.getDouble("boost");
                long remainingDuration = rsTemporary.getLong("remainingDuration");

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                PersonalBooster booster = new PersonalBooster(uuid, boosterIdentifier);
                TemporaryBoost boosterBoost = new TemporaryBoost(boost, new BoosterDuration(remainingDuration));

                ActiveBoosterManager.setActiveBooster(booster, boosterBoost);
            }

            ResultSet rsPermanent = stmt.executeQuery("SELECT * FROM PermanentPersonalBoosters");
            while (rsPermanent.next()) {
                UUID uuid = UUID.fromString(rsPermanent.getString("uuid"));
                String identifier = rsPermanent.getString("identifier");
                BoosterType boosterType = BoosterType.PERSONAL;
                ApplicatorType applicatorType = ApplicatorType.convert(rsPermanent.getString("applicatorType"));
                String boosted = rsPermanent.getString("boosted");
                double boost = rsPermanent.getDouble("boost");

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                PersonalBooster booster = new PersonalBooster(uuid, boosterIdentifier);
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
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getPersonalBoostersData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS PermanentPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

            List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(BoosterType.PERSONAL);

            if (!activeBoosters.isEmpty()) {
                for (ActiveBooster activeBooster : activeBoosters) {
                    PersonalBooster booster = (PersonalBooster) activeBooster.getBooster();
                    if (booster.isValid()) {
                        UUID uuid = booster.getUUID();
                        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                        String identifier = boosterIdentifier.getIdentifier();
                        String applicatorType = boosterIdentifier.getApplicatorType().name();
                        String boosted = boosterIdentifier.getBoosted();
                        PermanentBoost permanentBoost = activeBooster.getBoosts().getPermanent();
                        TemporaryBoost temporaryBoost = activeBooster.getBoosts().getTemporary();

                        if (permanentBoost.isActive()) {
                            double boost = permanentBoost.getBoost();
                            stmt.execute("INSERT OR REPLACE INTO PermanentPersonalBoosters (uuid, identifier, applicatorType, boosted, boost) VALUES('" + uuid + "', '" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + boost + "')");
                        } else {
                            stmt.execute("DELETE FROM PermanentPersonalBoosters WHERE uuid = '" + uuid + "' AND identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        }

                        if (temporaryBoost.isActive()) {
                            double boost = temporaryBoost.getBoost();
                            long remainingDuration = temporaryBoost.getDuration().getRemainingTime();
                            stmt.execute("INSERT OR REPLACE INTO TemporaryPersonalBoosters (uuid, identifier, applicatorType, boosted, boost, remainingDuration) VALUES('" + uuid + "', '" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + boost + "', '" + remainingDuration + "')");
                        } else {
                            stmt.execute("DELETE FROM TemporaryPersonalBoosters WHERE uuid = '" + uuid + "' AND identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
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

    public void registerActiveBooster(PersonalBooster booster, Boost boost) {
        try {
            if (booster.isValid()) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getPersonalBoostersData().getFile().getAbsolutePath());
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
                stmt.execute("CREATE TABLE IF NOT EXISTS PermanentPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

                UUID uuid = booster.getUUID();
                DurationType durationType = boost.getDurationType();
                BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                String identifier = boosterIdentifier.getIdentifier();
                String applicatorType = boosterIdentifier.getApplicatorType().name();
                String boosted = boosterIdentifier.getBoosted();
                double b = boost.getBoost();

                if (durationType.equals(DurationType.PERM)) {
                    if (boost.isActive()) {
                        stmt.execute("INSERT OR REPLACE INTO PermanentPersonalBoosters (uuid, identifier, applicatorType, boosted, boost) VALUES('" + uuid + "', '" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + b + "')");
                    }
                } else if (durationType.equals(DurationType.TEMP)) {
                    TemporaryBoost temporaryBoost = (TemporaryBoost) boost;
                    long remainingDuration = temporaryBoost.getDuration().getRemainingTime();
                    stmt.execute("INSERT OR REPLACE INTO TemporaryPersonalBoosters (uuid, identifier, applicatorType, boosted, boost, remainingDuration) VALUES('" + uuid + "', '" + identifier + "', '" + applicatorType + "', '" + boosted + "', '" + b + "', '" + remainingDuration + "')");
                }
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeActiveBooster(DurationType durationType, PersonalBooster booster) {
        removeActiveBoosterMethod(durationType, new ArrayList<>(Collections.singleton(booster)));
    }
    public void removeActiveBooster(DurationType durationType, List<PersonalBooster> boosters) {
        removeActiveBoosterMethod(durationType, boosters);
    }

    private void removeActiveBoosterMethod(DurationType durationType, List<PersonalBooster> boosters) {
        try {
            if (!boosters.isEmpty()) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getPersonalBoostersData().getFile().getAbsolutePath());
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
                stmt.execute("CREATE TABLE IF NOT EXISTS PermanentPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

                for (PersonalBooster booster : boosters) {
                    if (booster.isValid()) {
                        UUID uuid = booster.getUUID();
                        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                        String identifier = boosterIdentifier.getIdentifier();
                        String applicatorType = boosterIdentifier.getApplicatorType().name();
                        String boosted = boosterIdentifier.getBoosted();

                        if (durationType.equals(DurationType.TEMP)) {
                            stmt.execute("DELETE FROM TemporaryPersonalBoosters WHERE uuid = '" + uuid + "' AND identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        } else if (durationType.equals(DurationType.PERM)) {
                            stmt.execute("DELETE FROM PermanentPersonalBoosters WHERE uuid = '" + uuid + "' AND identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        }
                    }
                }

                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeActiveBooster(PersonalBooster booster) {
        removeActiveBooster(new ArrayList<>(Collections.singleton(booster)));
    }

    public void removeActiveBooster(List<PersonalBooster> boosters) {
        removeActiveBoosterMethod(boosters);
    }

    private void removeActiveBoosterMethod(List<PersonalBooster> boosters) {
        try {
            if (!boosters.isEmpty()) {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + configs.getPersonalBoostersData().getFile().getAbsolutePath());
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS TemporaryPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL, remainingDuration INTEGER)");
                stmt.execute("CREATE TABLE IF NOT EXISTS PermanentPersonalBoosters (uuid TEXT, identifier TEXT, applicatorType TEXT, boosted TEXT, boost REAL)");

                for (PersonalBooster booster : boosters) {
                    if (booster.isValid()) {
                        UUID uuid = booster.getUUID();
                        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
                        String identifier = boosterIdentifier.getIdentifier();
                        String applicatorType = boosterIdentifier.getApplicatorType().name();
                        String boosted = boosterIdentifier.getBoosted();

                        stmt.execute("DELETE FROM TemporaryPersonalBoosters WHERE uuid = '" + uuid + "' AND identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                        stmt.execute("DELETE FROM PermanentPersonalBoosters WHERE uuid = '" + uuid + "' AND identifier = '" + identifier + "' AND applicatorType = '" + applicatorType + "' AND boosted = '" + boosted + "'");
                    }
                }

                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
