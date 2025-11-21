package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/transpologic_db";
    private static final String USER = "root";
    private static final String PASS = "";  // XAMPP default

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("DB Connected Successfully!");
        } catch (Exception e) {
            System.out.println("DB Connection Failed!");
            e.printStackTrace();
        }
        return conn;
    }
}
