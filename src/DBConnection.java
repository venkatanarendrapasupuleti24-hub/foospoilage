import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/food_spoilage";
        String user = "root";
        String password = "Gayathri@123"; // change this

        return DriverManager.getConnection(url, user, password);
    }
}
