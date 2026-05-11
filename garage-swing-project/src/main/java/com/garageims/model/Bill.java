package com.garageims.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {
    private int id;
    private int serviceId;
    private BigDecimal totalAmount;
    private LocalDate billDate;
    private String customerName;
    private String vehicleNo;
    private String serviceType;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getVehicleNo() { return vehicleNo; }
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
}
