import java.sql.*;
import java.time.LocalDate;

public class InventoryService {

    // Add new food item
    public void addItem(String name, int qty, LocalDate expiry) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO inventory (item_name, quantity, entry_date, expiry_date) VALUES (?, ?, ?, ?)"
        );
        ps.setString(1, name);
        ps.setInt(2, qty);
        ps.setDate(3, Date.valueOf(LocalDate.now()));
        ps.setDate(4, Date.valueOf(expiry));
        ps.executeUpdate();
        con.close();
    }

    // FIFO consumption
    public void consumeItem(String name, int qty) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "SELECT id, quantity FROM inventory WHERE item_name=? ORDER BY entry_date ASC"
        );
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        while (rs.next() && qty > 0) {
            int id = rs.getInt("id");
            int available = rs.getInt("quantity");

            int used = Math.min(available, qty);
            qty -= used;

            PreparedStatement upd = con.prepareStatement(
                "UPDATE inventory SET quantity=? WHERE id=?"
            );
            upd.setInt(1, available - used);
            upd.setInt(2, id);
            upd.executeUpdate();
        }
        con.close();
    }

    // Expiry alert
    public void expiryAlert() throws Exception {
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(
            "SELECT item_name, expiry_date FROM inventory"
        );

        LocalDate today = LocalDate.now();
        while (rs.next()) {
            LocalDate exp = rs.getDate("expiry_date").toLocalDate();
            if (!exp.isAfter(today.plusDays(3))) {
                System.out.println("⚠ Expiry Alert: " + rs.getString("item_name"));
            }
        }
        con.close();
    }
}
