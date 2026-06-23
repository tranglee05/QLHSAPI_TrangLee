package Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    public static Connection getConnection() {
        Connection cons = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyHocSinh;encrypt=true;trustServerCertificate=true;";
//            String dbUrl = "jdbc:sqlserver://TRANG\\MSSQLSERVER2025;databaseName=QuanLyHocSinh;encrypt=true;trustServerCertificate=true;";
            String username = "sa"; 
            String password = "123456"; 
            
            cons = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("Kết nối CSDL thành công!");
        } catch (Exception e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }
        return cons;
    }
    

}