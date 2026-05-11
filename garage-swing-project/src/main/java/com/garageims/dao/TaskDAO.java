package com.garageims.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.garageims.model.TaskItem;
import com.garageims.util.DBConnection;

public class TaskDAO {
    public List<TaskItem> getAll() throws SQLException {
        List<TaskItem> list = new ArrayList<>();
        String sql = "SELECT t.id, t.assigned_to, t.title, t.description, t.status, t.task_date, u.full_name " +
                "FROM tasks t LEFT JOIN users u ON t.assigned_to = u.id ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TaskItem task = new TaskItem();
                task.setId(rs.getInt("id"));
                task.setAssignedTo((Integer) rs.getObject("assigned_to"));
                task.setAssignedToName(rs.getString("full_name"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStatus(rs.getString("status"));
                task.setTaskDate(rs.getDate("task_date").toLocalDate());
                list.add(task);
            }
        }
        return list;
    }

    public List<TaskItem> getByStaff(int staffId) throws SQLException {
        List<TaskItem> list = new ArrayList<>();
        String sql = "SELECT t.id, t.assigned_to, t.title, t.description, t.status, t.task_date, u.full_name " +
                "FROM tasks t LEFT JOIN users u ON t.assigned_to = u.id WHERE t.assigned_to = ? ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TaskItem task = new TaskItem();
                    task.setId(rs.getInt("id"));
                    task.setAssignedTo((Integer) rs.getObject("assigned_to"));
                    task.setAssignedToName(rs.getString("full_name"));
                    task.setTitle(rs.getString("title"));
                    task.setDescription(rs.getString("description"));
                    task.setStatus(rs.getString("status"));
                    task.setTaskDate(rs.getDate("task_date").toLocalDate());
                    list.add(task);
                }
            }
        }
        return list;
    }

    public void add(TaskItem task) throws SQLException {
        String sql = "INSERT INTO tasks(assigned_to, title, description, status, task_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (task.getAssignedTo() == null) ps.setNull(1, java.sql.Types.INTEGER); else ps.setInt(1, task.getAssignedTo());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getStatus());
            ps.setDate(5, Date.valueOf(task.getTaskDate()));
            ps.executeUpdate();
        }
    }

    public void update(TaskItem task) throws SQLException {
        String sql = "UPDATE tasks SET assigned_to = ?, title = ?, description = ?, status = ?, task_date = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (task.getAssignedTo() == null) ps.setNull(1, java.sql.Types.INTEGER); else ps.setInt(1, task.getAssignedTo());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getStatus());
            ps.setDate(5, Date.valueOf(task.getTaskDate()));
            ps.setInt(6, task.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
