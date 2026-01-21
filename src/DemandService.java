import java.sql.*;

public class DemandService {

    public double estimateDemand(String name) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "SELECT AVG(used_quantity) FROM usage_log WHERE item_name=?"
        );
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        double avg = rs.next() ? rs.getDouble(1) : 0;
        con.close();
        return avg;
    }
}

