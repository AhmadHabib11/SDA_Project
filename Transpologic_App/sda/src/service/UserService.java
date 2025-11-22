package service;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {

    /**
     * Authenticate user with username and password
     * Checks against the database users table
     */
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, username);
            pst.setString(2, password); // Note: In production, use hashed passwords!
            
            ResultSet rs = pst.executeQuery();
            return rs.next(); // Returns true if user found
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Register a new user (optional - for future use)
     */
    public boolean register(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, username);
            pst.setString(2, password); // Note: In production, hash the password first!
            pst.setString(3, email);
            
            return pst.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}