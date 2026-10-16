package mv.mossuh.moboosters.DATA.Database;

import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.TeamBooster;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;
import mv.mossuh.mocore.ENUMS.PluginType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TeamData extends BoosterData<TeamBooster> {
    private PluginType type;
    public TeamData(PluginType type) {
        this.type = type;
    }

    @Override
    protected String getTableName() {
        return type.name() + "Boosters";
    }

    @Override
    protected String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS "+getTableName()+" (" +
               "uuid VARCHAR(36) NOT NULL, " +
               "identifier VARCHAR(255) NOT NULL, " +
               "applicatorType VARCHAR(255) NOT NULL, " +
               "boosted VARCHAR(255) NOT NULL, " +
               "boost DOUBLE NOT NULL, " +
               "durationType VARCHAR(255) NOT NULL, " +
               "remainingDuration BIGINT NOT NULL, " +
               "PRIMARY KEY (uuid, identifier, applicatorType, boosted, durationType))";
    }

    @Override
    protected BoosterType getBoosterType() {
        return BoosterType.TEAM;
    }

    @Override
    protected boolean hasOwnerUUID() {
        return true;
    }

    @Override
    protected String getKeyColumns() {
        return "uuid = ? AND identifier = ? AND applicatorType = ? AND boosted = ?";
    }

    @Override
    protected int getKeyColumnsCount() {
        return 4;
    }

    @Override
    protected TeamBooster createBoosterFromRow(ResultSet rs) throws SQLException {
        UUID uuid = UUID.fromString(rs.getString("uuid"));
        String identifier = rs.getString("identifier");
        ApplicatorType applicatorType = ApplicatorType.convert(rs.getString("applicatorType"));
        String boosted = rs.getString("boosted");

        BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, BoosterType.TEAM, applicatorType, boosted);
        return new TeamBooster(uuid, boosterIdentifier);
    }

    @Override
    protected ActiveBooster toActiveBooster(TeamBooster booster, ResultSet rs) throws SQLException {
        String durationType = rs.getString("durationType");
        Boost boost;

        if ("TEMP".equals(durationType)) {
            boost = createTemporaryBoost(rs);
        } else {
            boost = createPermanentBoost(rs);
        }

        return buildActiveBooster(booster, boost);
    }

    @Override
    protected void setStatementValues(PreparedStatement ps, TeamBooster booster, Boost boost) throws SQLException {
        BoosterIdentifier id = booster.getIdentifier();
        ps.setString(1, booster.getUUID().toString());
        ps.setString(2, id.getIdentifier());
        ps.setString(3, id.getApplicatorType().name());
        ps.setString(4, id.getBoosted());
        ps.setDouble(5, boost.getBoost());
        ps.setString(6, boost.getDurationType().name());

        if (boost.getDurationType().equals(DurationType.TEMP)) {
            ps.setLong(7, ((TemporaryBoost) boost).getDuration().getRemainingTime());
        } else {
            ps.setLong(7, 0);
        }
    }

    @Override
    protected void setStatementKey(PreparedStatement ps, TeamBooster booster) throws SQLException {
        BoosterIdentifier id = booster.getIdentifier();
        ps.setString(1, booster.getUUID().toString());
        ps.setString(2, id.getIdentifier());
        ps.setString(3, id.getApplicatorType().name());
        ps.setString(4, id.getBoosted());
    }
}
