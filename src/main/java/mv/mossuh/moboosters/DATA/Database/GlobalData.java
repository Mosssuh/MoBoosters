package mv.mossuh.moboosters.DATA.Database;

import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalData extends BoosterData<GlobalBooster> {
    @Override
    protected String getTableName() {
        return "GlobalBoosters";
    }

    @Override
    protected BoosterType getBoosterType() {
        return BoosterType.GLOBAL;
    }

    @Override
    protected boolean hasOwnerUUID() {
        return false;
    }

    @Override
    protected String getKeyColumns() {
        return "identifier = ? AND applicatorType = ? AND boosted = ?";
    }

    @Override
    protected int getKeyColumnsCount() {
        return 3;
    }

    @Override
    protected GlobalBooster createBoosterFromRow(ResultSet rs) throws SQLException {
        String identifier = rs.getString("identifier");
        ApplicatorType applicatorType = ApplicatorType.convert(rs.getString("applicatorType"));
        String boosted = rs.getString("boosted");

        BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, BoosterType.GLOBAL, applicatorType, boosted);
        return new GlobalBooster(boosterIdentifier);
    }

    @Override
    protected ActiveBooster toActiveBooster(GlobalBooster booster, ResultSet rs) throws SQLException {
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
    protected void setStatementValues(PreparedStatement ps, GlobalBooster booster, Boost boost) throws SQLException {
        BoosterIdentifier id = booster.getIdentifier();
        ps.setString(1, id.getIdentifier());
        ps.setString(2, id.getApplicatorType().name());
        ps.setString(3, id.getBoosted());
        ps.setDouble(4, boost.getBoost());
        ps.setString(5, boost.getDurationType().name());

        if (boost.getDurationType().equals(DurationType.TEMP)) {
            ps.setLong(6, ((mv.mossuh.moboosters.MODEL.Booster.BoostTypes.TemporaryBoost) boost).getDuration().getRemainingTime());
        } else {
            ps.setLong(6, 0);
        }
    }

    @Override
    protected void setStatementKey(PreparedStatement ps, GlobalBooster booster) throws SQLException {
        BoosterIdentifier id = booster.getIdentifier();
        ps.setString(1, id.getIdentifier());
        ps.setString(2, id.getApplicatorType().name());
        ps.setString(3, id.getBoosted());
    }
}
