import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://database-2.cluayo042wzc.ap-south-1.rds.amazonaws.com:3306/myappdb";
            String user = "admin";
            String password = "Azsx#1312";

            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                System.out.println(rs.getString("name") + " - " + rs.getString("email"));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
