package com.garageims.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.garageims.model.Bill;
import com.garageims.util.DBConnection;

public class BillDAO {
    public List<Bill> getAll() throws SQLException {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT b.id, b.service_id, b.total_amount, b.bill_date, s.customer_name, s.vehicle_no, s.service_type FROM bills b JOIN services s ON b.service_id = s.id ORDER BY b.id DESC";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setServiceId(rs.getInt("service_id"));
                bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                bill.setBillDate(rs.getDate("bill_date").toLocalDate());
                bill.setCustomerName(rs.getString("customer_name"));
                bill.setVehicleNo(rs.getString("vehicle_no"));
                bill.setServiceType(rs.getString("service_type"));
                list.add(bill);
            }
        }
        return list;
    }

    public void add(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills(service_id, total_amount, bill_date) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bill.getServiceId());
            ps.setBigDecimal(2, bill.getTotalAmount());
            ps.setDate(3, Date.valueOf(bill.getBillDate()));
            ps.executeUpdate();
        }
    }
}
