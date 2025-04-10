package cpsc4620;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnector.make_connection();
            if (conn != null) {
                System.out.println("✅ Connected successfully to the database!");
                conn.close(); // Good practice to close the connection
            } else {
                System.out.println("❌ Failed to connect to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
