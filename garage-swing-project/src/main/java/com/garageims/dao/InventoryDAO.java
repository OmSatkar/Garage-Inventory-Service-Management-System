package com.garageims.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.garageims.config.AppConfig;
import com.garageims.model.InventoryItem;
import com.garageims.util.DBConnection;

public class InventoryDAO {
    public List<InventoryItem> getAll(String keyword, String category) throws SQLException {
        List<InventoryItem> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.id, i.name, i.category, i.quantity, i.price, i.supplier_id, i.date_added, s.name AS supplier_name " +
                "FROM inventory i LEFT JOIN suppliers s ON i.supplier_id = s.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(i.name) LIKE ? OR LOWER(i.category) LIKE ? OR LOWER(COALESCE(s.name,'')) LIKE ?)");
            String value = "%" + keyword.toLowerCase() + "%";
            params.add(value);
            params.add(value);
            params.add(value);
        }
        if (category != null && !category.isBlank() && !"All".equalsIgnoreCase(category)) {
            sql.append(" AND i.category = ?");
            params.add(category);
        }
        sql.append(" ORDER BY i.id DESC");

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryItem item = new InventoryItem();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setCategory(rs.getString("category"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setSupplierId(rs.getInt("supplier_id"));
                    item.setSupplierName(rs.getString("supplier_name"));
                    item.setDateAdded(rs.getDate("date_added").toLocalDate());
                    list.add(item);
                }
            }
        }
        return list;
    }

    public void add(InventoryItem item) throws SQLException {
        String sql = "INSERT INTO inventory(name, category, quantity, price, supplier_id, date_added) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getCategory());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPrice());
            ps.setInt(5, item.getSupplierId());
            ps.setDate(6, Date.valueOf(item.getDateAdded()));
            ps.executeUpdate();
        }
    }

    public void update(InventoryItem item) throws SQLException {
        String sql = "UPDATE inventory SET name = ?, category = ?, quantity = ?, price = ?, supplier_id = ?, date_added = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getCategory());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPrice());
            ps.setInt(5, item.getSupplierId());
            ps.setDate(6, Date.valueOf(item.getDateAdded()));
            ps.setInt(7, item.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM inventory WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int getTotalItems() throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM inventory"); ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public int getLowStockCount() throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM inventory WHERE quantity <= ?");) {
            ps.setInt(1, AppConfig.LOW_STOCK_THRESHOLD);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
