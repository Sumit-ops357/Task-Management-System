import java.sql.*;

public class JdbcTest {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/task_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&zeroDateTimeBehavior=CONVERT_TO_NULL&defaultAuthenticationPlugin=com.mysql.cj.protocol.a.NativePasswordPlugin&authenticationPlugins=com.mysql.cj.protocol.a.NativePasswordPlugin";
        String user = "root";
        String pass = "Sumit@123";
        System.out.println("Attempting connection...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("SUCCESS! Connected to MySQL: " + con.getMetaData().getDatabaseProductVersion());
            con.close();
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
            Throwable cause = e.getCause();
            while (cause != null) {
                System.out.println("  Caused by: " + cause.getClass().getName() + ": " + cause.getMessage());
                cause = cause.getCause();
            }
        }
    }
}
