package mv.mossuh.moboosters.DATA.Database;

import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class PersonalData extends BoosterData<PersonalBooster> {
    @Override
    protected String getTableName() {
        return "PersonalBoosters";
    }

    @Override
    protected BoosterType getBoosterType() {
        return BoosterType.PERSONAL;
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
    protected PersonalBooster createBoosterFromRow(ResultSet rs) throws SQLException {
        UUID uuid = UUID.fromString(rs.getString("uuid"));
        String identifier = rs.getString("identifier");
        ApplicatorType applicatorType = ApplicatorType.convert(rs.getString("applicatorType"));
        String boosted = rs.getString("boosted");

        BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, BoosterType.PERSONAL, applicatorType, boosted);
        return new PersonalBooster(uuid, boosterIdentifier);
    }

    @Override
    protected ActiveBooster toActiveBooster(PersonalBooster booster, ResultSet rs) throws SQLException {
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
    protected void setStatementValues(PreparedStatement ps, PersonalBooster booster, Boost boost) throws SQLException {
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
    protected void setStatementKey(PreparedStatement ps, PersonalBooster booster) throws SQLException {
        BoosterIdentifier id = booster.getIdentifier();
        ps.setString(1, booster.getUUID().toString());
        ps.setString(2, id.getIdentifier());
        ps.setString(3, id.getApplicatorType().name());
        ps.setString(4, id.getBoosted());
    }
}
