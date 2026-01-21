import java.sql.*;
import java.time.LocalDate;

public class WasteService {

    public void logWaste(String name, int qty, String reason) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO waste (item_name, wasted_quantity, reason, waste_date) VALUES (?, ?, ?, ?)"
        );
        ps.setString(1, name);
        ps.setInt(2, qty);
        ps.setString(3, reason);
        ps.setDate(4, Date.valueOf(LocalDate.now()));
        ps.executeUpdate();
        con.close();
    }
}
