package com.garageims.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.garageims.model.ServiceRecord;
import com.garageims.util.DBConnection;

public class ServiceDAO {
    public List<ServiceRecord> getAll() throws SQLException {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT id, customer_name, vehicle_no, service_type, cost, service_date, notes FROM services ORDER BY id DESC";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ServiceRecord record = new ServiceRecord();
                record.setId(rs.getInt("id"));
                record.setCustomerName(rs.getString("customer_name"));
                record.setVehicleNo(rs.getString("vehicle_no"));
                record.setServiceType(rs.getString("service_type"));
                record.setCost(rs.getBigDecimal("cost"));
                record.setServiceDate(rs.getDate("service_date").toLocalDate());
                record.setNotes(rs.getString("notes"));
                list.add(record);
            }
        }
        return list;
    }

    public void add(ServiceRecord record) throws SQLException {
        String sql = "INSERT INTO services(customer_name, vehicle_no, service_type, cost, service_date, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, record.getCustomerName());
            ps.setString(2, record.getVehicleNo());
            ps.setString(3, record.getServiceType());
            ps.setBigDecimal(4, record.getCost());
            ps.setDate(5, Date.valueOf(record.getServiceDate()));
            ps.setString(6, record.getNotes());
            ps.executeUpdate();
        }
    }

    public void update(ServiceRecord record) throws SQLException {
        String sql = "UPDATE services SET customer_name = ?, vehicle_no = ?, service_type = ?, cost = ?, service_date = ?, notes = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, record.getCustomerName());
            ps.setString(2, record.getVehicleNo());
            ps.setString(3, record.getServiceType());
            ps.setBigDecimal(4, record.getCost());
            ps.setDate(5, Date.valueOf(record.getServiceDate()));
            ps.setString(6, record.getNotes());
            ps.setInt(7, record.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM services WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int getTodayCount() throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM services WHERE service_date = CURRENT_DATE"); ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
