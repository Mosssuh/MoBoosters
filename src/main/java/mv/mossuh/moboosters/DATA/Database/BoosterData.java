package mv.mossuh.moboosters.DATA.Database;

import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.Duration.Boosts;
import mv.mossuh.moboosters.MODEL.Booster.Duration.BoosterDuration;
import mv.mossuh.moboosters.MANAGER.ActiveBoosterManager;
import mv.mossuh.moboosters.MoBoosters;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;
import mv.mossuh.moboosters.UTILITIES.UtilString;

import java.sql.*;
import java.util.List;

public abstract class BoosterData<T extends Booster> {
    private final static MoBoosters instance = MoBoosters.getInstance();

    // ==================== ABSTRACT METHODS ====================

    protected abstract String getTableName();

    protected abstract String getCreateTableSQL();

    protected abstract BoosterType getBoosterType();

    protected abstract T createBoosterFromRow(ResultSet rs) throws SQLException;

    protected abstract ActiveBooster toActiveBooster(T booster, ResultSet rs) throws SQLException;

    protected abstract void setStatementValues(PreparedStatement ps, T booster, Boost boost) throws SQLException;

    protected abstract void setStatementKey(PreparedStatement ps, T booster) throws SQLException;

    // ==================== TABLE CREATION ====================

    private void ensureTableExists(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(getCreateTableSQL());
        }
    }

    // ==================== LOAD: Database -> Memory ====================

    public void registerDatabaseInMaps() {
        try (Connection conn = instance.getConnection()) {
            if (conn == null) return;
            ensureTableExists(conn);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM " + getTableName())) {

                while (rs.next()) {
                    T booster = createBoosterFromRow(rs);
                    ActiveBooster activeBooster = toActiveBooster(booster, rs);

                    if (activeBooster.isValid()) {
                        Boost temp = activeBooster.getBoosts().getTemporary();
                        Boost perm = activeBooster.getBoosts().getPermanent();

                        if (temp.isActive()) {
                            ActiveBoosterManager.createActiveBooster(booster, temp);
                        }
                        if (perm.isActive()) {
                            ActiveBoosterManager.createActiveBooster(booster, perm);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            UtilString.get(Config.PREFIX + " &cFailed to load " + getBoosterType() + " boosters from database").hex().sendMessageInConsole();
            e.printStackTrace();
        }
    }

    // ==================== SAVE: Memory -> Database ====================

    public void registerMapsInDatabase() {
        String tableName = getTableName();
        String insertSQL = buildInsertSQL();
        String deleteSQL = "DELETE FROM " + tableName;

        try (Connection conn = instance.getConnection()) {
            if (conn == null) return;
            ensureTableExists(conn);
            conn.setAutoCommit(false);

            try (PreparedStatement deletePs = conn.prepareStatement(deleteSQL);
                 PreparedStatement insertPs = conn.prepareStatement(insertSQL)) {

                deletePs.executeUpdate();

                List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(getBoosterType());
                for (ActiveBooster activeBooster : activeBoosters) {
                    @SuppressWarnings("unchecked")
                    T booster = (T) activeBooster.getBooster();
                    if (!booster.isValid()) continue;

                    PermanentBoost perm = activeBooster.getBoosts().getPermanent();
                    TemporaryBoost temp = activeBooster.getBoosts().getTemporary();

                    if (perm.isActive()) {
                        setStatementValues(insertPs, booster, perm);
                        insertPs.addBatch();
                    }
                    if (temp.isActive()) {
                        setStatementValues(insertPs, booster, temp);
                        insertPs.addBatch();
                    }
                }

                insertPs.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            UtilString.get(Config.PREFIX + " &cFailed to save " + getBoosterType() + " boosters to database").hex().sendMessageInConsole();
            e.printStackTrace();
        }
    }

    // ==================== SINGLE BOOSTER OPERATIONS ====================

    public void saveActiveBooster(T booster, Boost boost) {
        if (booster == null || !booster.isValid() || boost == null || !boost.isActive()) return;

        try (Connection conn = instance.getConnection()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(buildInsertSQL())) {

                setStatementValues(ps, booster, boost);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            UtilString.get(Config.PREFIX + " &cFailed to save " + getBoosterType() + " booster").hex().sendMessageInConsole();
            e.printStackTrace();
        }
    }

    public void removeActiveBooster(DurationType durationType, T booster) {
        removeActiveBooster(durationType, java.util.Collections.singletonList(booster));
    }

    public void removeActiveBooster(DurationType durationType, List<T> boosters) {
        if (boosters == null || boosters.isEmpty()) return;

        String sql = "DELETE FROM " + getTableName() + " WHERE " + getKeyColumns() + " AND durationType = ?";

        try (Connection conn = instance.getConnection()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                for (T booster : boosters) {
                    if (booster == null || !booster.isValid()) continue;
                    setStatementKey(ps, booster);
                    ps.setString(getKeyColumnsCount() + 1, durationType.name());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            UtilString.get(Config.PREFIX + " &cFailed to remove " + getBoosterType() + " booster").hex().sendMessageInConsole();
            e.printStackTrace();
        }
    }

    public void removeActiveBooster(T booster) {
        removeActiveBooster(java.util.Collections.singletonList(booster));
    }

    public void removeActiveBooster(List<T> boosters) {
        if (boosters == null || boosters.isEmpty()) return;

        String sql = "DELETE FROM " + getTableName() + " WHERE " + getKeyColumns();

        try (Connection conn = instance.getConnection()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                for (T booster : boosters) {
                    if (booster == null || !booster.isValid()) continue;
                    setStatementKey(ps, booster);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            UtilString.get(Config.PREFIX + " &cFailed to remove " + getBoosterType() + " boosters").hex().sendMessageInConsole();
            e.printStackTrace();
        }
    }

    // ==================== SQL HELPERS ====================

    private String buildInsertSQL() {
        boolean hasOwner = hasOwnerUUID();
        String columns;
        String placeholders;
        if (hasOwner) {
            columns = "uuid, identifier, applicatorType, boosted, boost, durationType, remainingDuration";
            placeholders = "?, ?, ?, ?, ?, ?, ?";
        } else {
            columns = "identifier, applicatorType, boosted, boost, durationType, remainingDuration";
            placeholders = "?, ?, ?, ?, ?, ?";
        }

        if (Config.MYSQL_ENABLED) {
            String updateClause = hasOwner
                ? "uuid=VALUES(uuid), boost=VALUES(boost), durationType=VALUES(durationType), remainingDuration=VALUES(remainingDuration)"
                : "boost=VALUES(boost), durationType=VALUES(durationType), remainingDuration=VALUES(remainingDuration)";
            return "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ") ON DUPLICATE KEY UPDATE " + updateClause;
        } else {
            return "INSERT OR REPLACE INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";
        }
    }

    protected abstract boolean hasOwnerUUID();

    protected abstract String getKeyColumns();

    protected abstract int getKeyColumnsCount();

    // ==================== STATIC HELPER: Build ActiveBooster from boost data ====================

    protected static ActiveBooster buildActiveBooster(Booster booster, Boost boost) {
        if (boost instanceof TemporaryBoost) {
            return new ActiveBooster(booster, new Boosts(null, (TemporaryBoost) boost));
        } else if (boost instanceof PermanentBoost) {
            return new ActiveBooster(booster, new Boosts((PermanentBoost) boost, null));
        }
        return new ActiveBooster(booster, new Boosts(null, null));
    }

    protected static TemporaryBoost createTemporaryBoost(ResultSet rs) throws SQLException {
        double boost = rs.getDouble("boost");
        long remainingDuration = rs.getLong("remainingDuration");
        return new TemporaryBoost(boost, new BoosterDuration(remainingDuration));
    }

    protected static PermanentBoost createPermanentBoost(ResultSet rs) throws SQLException {
        double boost = rs.getDouble("boost");
        return new PermanentBoost(boost);
    }
}
