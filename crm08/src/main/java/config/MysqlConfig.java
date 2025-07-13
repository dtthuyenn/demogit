package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConfig {
    // Phương thức kết nối CSDL
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/crm_app";
        String user = "root";
        String password = "password"; // Đổi password cho đúng với MySQL của bạn
        Connection connection = null;

        try {
            // Load MySQL JDBC Driver (không bắt buộc với JDBC 4+ nhưng nên thêm)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Kết nối CSDL
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối dữ liệu thành công!");

        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver MySQL!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }

        return connection;
    }
}
