package com.garageims.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.garageims.model.User;
import com.garageims.util.DBConnection;

public class UserDAO {
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, role, full_name FROM users WHERE username = ? AND password = ? AND is_active = true";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("full_name")
                    );
                }
            }
        }
        return null;
    }

    public List<User> getAllStaff() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, role, full_name FROM users WHERE role = 'STAFF' ORDER BY id DESC";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("full_name")));
            }
        }
        return users;
    }

    public void addStaff(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, full_name, is_active) VALUES (?, ?, 'STAFF', ?, true)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.executeUpdate();
        }
    }

    public void updateStaff(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, full_name = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        }
    }

    public void deleteStaff(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ? AND role = 'STAFF'";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
