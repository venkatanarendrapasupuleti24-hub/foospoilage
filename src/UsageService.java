import java.sql.*;
import java.time.LocalDate;

public class UsageService {

    public void logUsage(String name, int qty) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO usage_log (item_name, used_quantity, usage_date) VALUES (?, ?, ?)"
        );
        ps.setString(1, name);
        ps.setInt(2, qty);
        ps.setDate(3, Date.valueOf(LocalDate.now()));
        ps.executeUpdate();
        con.close();
    }
}
