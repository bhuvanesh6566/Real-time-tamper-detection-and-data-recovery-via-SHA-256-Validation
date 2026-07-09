import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DropAndCreate {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/self_healing_db_2.0?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "bhuvan@10";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Drop table
            System.out.println("Dropping table...");
            stmt.executeUpdate("DROP TABLE IF EXISTS records");

            // Create table in specific column order
            System.out.println("Creating table...");
            String createSql = "CREATE TABLE records (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "original_grade VARCHAR(255)," +
                    "original_roll_number BIGINT," +
                    "original_student_name VARCHAR(255)," +
                    "student_name VARCHAR(255)," +
                    "roll_number BIGINT," +
                    "grade VARCHAR(255)," +
                    "status VARCHAR(255)," +
                    "hash VARCHAR(64)" +
                    ")";
            stmt.executeUpdate(createSql);
            System.out.println("Table created successfully with new column order!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
