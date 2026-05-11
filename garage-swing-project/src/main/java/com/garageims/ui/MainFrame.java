package com.garageims.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.garageims.config.AppConfig;
import com.garageims.dao.BillDAO;
import com.garageims.dao.DashboardDAO;
import com.garageims.dao.InventoryDAO;
import com.garageims.dao.ServiceDAO;
import com.garageims.dao.SupplierDAO;
import com.garageims.dao.TaskDAO;
import com.garageims.dao.UserDAO;
import com.garageims.model.Bill;
import com.garageims.model.InventoryItem;
import com.garageims.model.ServiceRecord;
import com.garageims.model.Supplier;
import com.garageims.model.TaskItem;
import com.garageims.model.User;
import com.garageims.ui.components.SimpleBarChartPanel;
import com.garageims.ui.components.StatsCard;
import com.garageims.ui.dialog.InventoryDialog;
import com.garageims.ui.dialog.ServiceDialog;
import com.garageims.ui.dialog.StaffDialog;
import com.garageims.ui.dialog.SupplierDialog;
import com.garageims.ui.dialog.TaskDialog;
import com.garageims.util.UIUtils;

public class MainFrame extends JFrame {
    private final User currentUser;
    private final JPanel contentArea = new JPanel(new CardLayout());
    private final JLabel pageTitle = new JLabel("Dashboard");

    private final DashboardPanel dashboardPanel = new DashboardPanel();
    private final InventoryPanel inventoryPanel;
    private final SupplierPanel supplierPanel = new SupplierPanel();
    private final ServicePanel servicePanel = new ServicePanel();
    private final BillingPanel billingPanel = new BillingPanel();
    private final StaffManagementPanel staffManagementPanel = new StaffManagementPanel();
    private final TaskManagementPanel taskManagementPanel = new TaskManagementPanel();
    private final MyTaskPanel myTaskPanel = new MyTaskPanel();

    public MainFrame(User currentUser) {
        this.currentUser = currentUser;
        this.inventoryPanel = new InventoryPanel("ADMIN".equalsIgnoreCase(currentUser.getRole()));

        setTitle("Garage Inventory & Management System - " + currentUser.getRole());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        UIUtils.applyMainFrameSize(this);
        
        JPanel root = new UIUtils.GradientPanel();
        root.setLayout(new BorderLayout(14, 14));
        root.setBorder(UIUtils.padding(14, 14, 14, 14));
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainArea(), BorderLayout.CENTER);

        registerCards();
        showPage("Dashboard");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new UIUtils.RoundedPanel();
        sidebar.setPreferredSize(new Dimension(235, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(UIUtils.padding(18, 18, 18, 18));

        JLabel title = new JLabel("Garage IMS");
        title.setFont(UIUtils.font(24, true));
        title.setForeground(UIUtils.TEXT_DARK);
        JLabel role = new JLabel(currentUser.getFullName() + " · " + currentUser.getRole());
        role.setFont(UIUtils.font(13, false));
        role.setForeground(UIUtils.TEXT_MUTED);
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(role);
        sidebar.add(Box.createVerticalStrut(18));

        addSidebarButton(sidebar, "Dashboard", e -> showPage("Dashboard"));
        addSidebarButton(sidebar, "Inventory", e -> showPage("Inventory"));
        if (isAdmin()) {
            addSidebarButton(sidebar, "Suppliers", e -> showPage("Suppliers"));
        }
        addSidebarButton(sidebar, "Services", e -> showPage("Services"));
        addSidebarButton(sidebar, "Billing", e -> showPage("Billing"));
        if (isAdmin()) {
            addSidebarButton(sidebar, "Staff", e -> showPage("Staff"));
            addSidebarButton(sidebar, "Tasks", e -> showPage("Tasks"));
        } else {
            addSidebarButton(sidebar, "My Tasks", e -> showPage("My Tasks"));
        }

        sidebar.add(Box.createVerticalGlue());
        JButton logout = UIUtils.gradientButton("Logout", UIUtils.DANGER, UIUtils.DANGER_DARK);
        logout.addActionListener(e -> logout());
        sidebar.add(logout);
        return sidebar;
    }

    private void addSidebarButton(JPanel sidebar, String text, ActionListener action) {
        JButton btn = new UIUtils.SidebarButton(text);
        btn.addActionListener(action);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private JPanel buildMainArea() {
        JPanel main = UIUtils.transparentPanel();
        main.setLayout(new BorderLayout(0, 10));

        JPanel header = new UIUtils.RoundedPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(UIUtils.padding(12, 18, 12, 18));
        pageTitle.setFont(UIUtils.font(24, true));
        pageTitle.setForeground(UIUtils.TEXT_DARK);
        JLabel subtitle = new JLabel("Garage Inventory & Management System");
        subtitle.setFont(UIUtils.font(14, false));
        subtitle.setForeground(UIUtils.TEXT_MUTED);

        JPanel left = UIUtils.transparentPanel();
        left.setLayout(new BorderLayout());
        left.add(pageTitle, BorderLayout.NORTH);
        left.add(subtitle, BorderLayout.SOUTH);
        header.add(left, BorderLayout.WEST);

        //ImageIcon logo = new ImageIcon("src/main/resources/logo.png");
        //JLabel logoLabel = new JLabel(logo);
        //header.add(logoLabel, BorderLayout.EAST);
        
       //TO ADD LOGO REPLACE BELOW METHOD WITH THIS COMMENTED ONE
        JLabel logoPlaceholder = new JLabel(" ");
        logoPlaceholder.setPreferredSize(new Dimension(180, 40));
        header.add(logoPlaceholder, BorderLayout.EAST);
        

        contentArea.setOpaque(false);
        main.add(header, BorderLayout.NORTH);
        main.add(contentArea, BorderLayout.CENTER);
        return main;
    }

    private void registerCards() {
        contentArea.add(dashboardPanel, "Dashboard");
        contentArea.add(inventoryPanel, "Inventory");
        contentArea.add(supplierPanel, "Suppliers");
        contentArea.add(servicePanel, "Services");
        contentArea.add(billingPanel, "Billing");
        contentArea.add(staffManagementPanel, "Staff");
        contentArea.add(taskManagementPanel, "Tasks");
        contentArea.add(myTaskPanel, "My Tasks");
    }

    private JPanel wrapPage(JPanel panel) {
        return panel;
    }

    private void showPage(String name) {
        pageTitle.setText(name);
        ((CardLayout) contentArea.getLayout()).show(contentArea, name);
        switch (name) {
            case "Dashboard" -> dashboardPanel.refresh();
            case "Inventory" -> inventoryPanel.refresh();
            case "Suppliers" -> supplierPanel.refresh();
            case "Services" -> servicePanel.refresh();
            case "Billing" -> billingPanel.refresh();
            case "Staff" -> staffManagementPanel.refresh();
            case "Tasks" -> taskManagementPanel.refresh();
            case "My Tasks" -> myTaskPanel.refresh();
        }
    }

    private boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    private static abstract class ModulePanel extends JPanel {
        public ModulePanel() {
            setOpaque(false);
            setLayout(new BorderLayout(0, 8));
            setBorder(UIUtils.padding(0, 0, 6, 0));
        }

        abstract void refresh();
    }

    private class DashboardPanel extends ModulePanel {
        private final StatsCard itemsCard = new StatsCard("Total Items", "0", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
        private final StatsCard lowStockCard = new StatsCard("Low Stock", "0", UIUtils.DANGER, UIUtils.DANGER_DARK);
        private final StatsCard servicesCard = new StatsCard("Today's Services", "0", UIUtils.WARNING, UIUtils.WARNING_DARK);
        private final StatsCard staffCard = new StatsCard("Total Staff", "0", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
        private final StatsCard revenueCard = new StatsCard("Today's Revenue", "₹ 0.00", UIUtils.PURPLE, UIUtils.PURPLE_DARK);
        private final SimpleBarChartPanel chartPanel = new SimpleBarChartPanel();
        private final DefaultTableModel lowStockModel = new DefaultTableModel(new String[]{"Item", "Category", "Qty", "Supplier"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable lowStockTable = new JTable(lowStockModel);

        DashboardPanel() {
            JPanel cards = new JPanel(new GridLayout(2, 3, 12, 12));
            cards.setOpaque(false);
            cards.add(itemsCard);
            cards.add(lowStockCard);
            cards.add(servicesCard);
            cards.add(staffCard);
            cards.add(revenueCard);
            JPanel filler = UIUtils.transparentPanel();
            cards.add(filler);
            add(cards, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
            center.setOpaque(false);

            JPanel chartCard = UIUtils.sectionPanel("Dashboard Chart");
            chartPanel.setPreferredSize(new Dimension(540, 330));
            chartCard.add(chartPanel, BorderLayout.CENTER);
            center.add(chartCard);

            JPanel lowStockCardPanel = UIUtils.sectionPanel("Low Stock Alerts");
            UIUtils.styleTable(lowStockTable);
            lowStockTable.getColumnModel().getColumn(2).setCellRenderer(new QuantityCellRenderer());
            lowStockCardPanel.add(UIUtils.modernScroll(lowStockTable), BorderLayout.CENTER);
            center.add(lowStockCardPanel);

            add(center, BorderLayout.CENTER);
        }

        @Override
        void refresh() {
            InventoryDAO inventoryDAO = new InventoryDAO();
            ServiceDAO serviceDAO = new ServiceDAO();
            DashboardDAO dashboardDAO = new DashboardDAO();
            try {
                int totalItems = inventoryDAO.getTotalItems();
                int lowStock = inventoryDAO.getLowStockCount();
                int todayServices = serviceDAO.getTodayCount();
                int staffCount = dashboardDAO.getStaffCount();
                BigDecimal revenue = dashboardDAO.getTodayRevenue();

                itemsCard.setValue(String.valueOf(totalItems));
                lowStockCard.setValue(String.valueOf(lowStock));
                servicesCard.setValue(String.valueOf(todayServices));
                staffCard.setValue(String.valueOf(staffCount));
                revenueCard.setValue(UIUtils.money(revenue));

                Map<String, Integer> data = new LinkedHashMap<>();
                data.put("Items", totalItems);
                data.put("Low", lowStock);
                data.put("Services", todayServices);
                data.put("Staff", staffCount);
                chartPanel.setData(data);

                lowStockModel.setRowCount(0);
                for (InventoryItem item : inventoryDAO.getAll("", "All")) {
                    if (item.getQuantity() <= AppConfig.LOW_STOCK_THRESHOLD) {
                        lowStockModel.addRow(new Object[]{item.getName(), item.getCategory(), item.getQuantity(), item.getSupplierName()});
                    }
                }
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }
    }

    private class InventoryPanel extends ModulePanel {
        private final InventoryDAO inventoryDAO = new InventoryDAO();
        private final SupplierDAO supplierDAO = new SupplierDAO();
        private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Quantity", "Price", "Supplier", "Date", "Stock Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable table = new JTable(model);
        private final JTextField searchField = new JTextField(18);
        private final JComboBox<String> categoryBox = new JComboBox<>(new String[]{"All", "Engine", "Oil", "Tyre", "Electrical", "Service", "General"});
        private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        private final boolean adminView;

        InventoryPanel(boolean adminView) {
            this.adminView = adminView;
            UIUtils.styleTextField(searchField);
            UIUtils.styleCombo(categoryBox);
            UIUtils.styleTable(table);
            table.setRowSorter(sorter);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getColumnModel().getColumn(3).setCellRenderer(new QuantityCellRenderer());
            table.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());

            JPanel top = UIUtils.sectionPanel("Inventory Management");
            JPanel controls = UIUtils.inlinePanel();
            controls.add(new JLabel("Search"));
            controls.add(UIUtils.roundedInput(searchField));
            controls.add(new JLabel("Category"));
            controls.add(UIUtils.roundedInput(categoryBox));
            JButton apply = UIUtils.gradientButton("Apply Filter", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
            JButton reset = UIUtils.gradientButton("Reset", UIUtils.WARNING, UIUtils.WARNING_DARK);
            controls.add(apply); controls.add(reset);
            top.add(controls, BorderLayout.CENTER);
            add(top, BorderLayout.NORTH);

            JPanel tableCard = UIUtils.sectionPanel("Stock Table");
            tableCard.add(UIUtils.modernScroll(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            JPanel actionsCard = UIUtils.sectionPanel("Actions");
            JPanel buttons = UIUtils.inlinePanel();
            JButton addBtn = UIUtils.gradientButton("Add Item", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
            JButton editBtn = UIUtils.gradientButton(adminView ? "Update Item" : "Update Stock", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
            JButton deleteBtn = UIUtils.gradientButton("Delete Item", UIUtils.DANGER, UIUtils.DANGER_DARK);
            JButton refreshBtn = UIUtils.gradientButton("Refresh", UIUtils.PURPLE, UIUtils.PURPLE_DARK);
            buttons.add(addBtn); buttons.add(editBtn);
            if (adminView) buttons.add(deleteBtn);
            buttons.add(refreshBtn);
            actionsCard.add(buttons, BorderLayout.CENTER);
            add(actionsCard, BorderLayout.SOUTH);

            apply.addActionListener(e -> applyFilter());
            reset.addActionListener(e -> {
                searchField.setText("");
                categoryBox.setSelectedIndex(0);
                applyFilter();
            });
            addBtn.addActionListener(e -> addItem());
            editBtn.addActionListener(e -> editItem());
            deleteBtn.addActionListener(e -> deleteItem());
            refreshBtn.addActionListener(e -> refresh());
        }

        private void applyFilter() {
            RowFilter<DefaultTableModel, Object> filter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String keyword = searchField.getText().trim().toLowerCase();
                    String category = (String) categoryBox.getSelectedItem();
                    boolean keywordMatch = keyword.isBlank();
                    for (int i = 0; i < 6 && !keywordMatch; i++) {
                        Object v = entry.getValue(i);
                        if (v != null && v.toString().toLowerCase().contains(keyword)) keywordMatch = true;
                    }
                    boolean categoryMatch = category == null || "All".equalsIgnoreCase(category) || category.equals(entry.getValue(2));
                    return keywordMatch && categoryMatch;
                }
            };
            sorter.setRowFilter(filter);
        }

        @Override
        void refresh() {
            model.setRowCount(0);
            try {
                List<InventoryItem> items = inventoryDAO.getAll(searchField.getText().trim(), (String) categoryBox.getSelectedItem());
                for (InventoryItem item : items) {
                    model.addRow(new Object[]{item.getId(), item.getName(), item.getCategory(), item.getQuantity(), item.getPrice(), item.getSupplierName(), item.getDateAdded(), UIUtils.lowStockText(item.getQuantity())});
                }
                applyFilter();
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void addItem() {
            try {
                List<Supplier> suppliers = supplierDAO.getAll();
                if (suppliers.isEmpty()) {
                    UIUtils.showInfo(MainFrame.this, "Add supplier first before creating inventory items.");
                    return;
                }
                InventoryDialog dialog = new InventoryDialog(MainFrame.this, "Add Inventory Item", suppliers, null);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    inventoryDAO.add(dialog.getItem());
                    refresh();
                    dashboardPanel.refresh();
                }
            } catch (Exception ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void editItem() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select an inventory item first.");
                return;
            }
            try {
                InventoryItem item = new InventoryItem();
                item.setId((Integer) model.getValueAt(row, 0));
                item.setName(String.valueOf(model.getValueAt(row, 1)));
                item.setCategory(String.valueOf(model.getValueAt(row, 2)));
                item.setQuantity(Integer.parseInt(String.valueOf(model.getValueAt(row, 3))));
                item.setPrice(new BigDecimal(String.valueOf(model.getValueAt(row, 4))));
                item.setDateAdded(LocalDate.parse(String.valueOf(model.getValueAt(row, 6))));
                String supplierName = String.valueOf(model.getValueAt(row, 5));
                List<Supplier> suppliers = supplierDAO.getAll();
                for (Supplier supplier : suppliers) {
                    if (supplierName != null && supplierName.equals(supplier.getName())) {
                        item.setSupplierId(supplier.getId());
                        break;
                    }
                }
                InventoryDialog dialog = new InventoryDialog(MainFrame.this, adminView ? "Edit Inventory Item" : "Update Stock", suppliers, item);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    InventoryItem updated = dialog.getItem();
                    updated.setId(item.getId());
                    inventoryDAO.update(updated);
                    refresh();
                    dashboardPanel.refresh();
                }
            } catch (Exception ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void deleteItem() {
            if (!adminView) return;
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select an inventory item first.");
                return;
            }
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Delete selected item?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    inventoryDAO.delete((Integer) model.getValueAt(row, 0));
                    refresh();
                    dashboardPanel.refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }
    }

    private class SupplierPanel extends ModulePanel {
        private final SupplierDAO dao = new SupplierDAO();
        private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Supplier Name", "Contact", "Address"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable table = new JTable(model);

        SupplierPanel() {
            UIUtils.styleTable(table);
            JPanel tableCard = UIUtils.sectionPanel("Supplier Management");
            tableCard.add(UIUtils.modernScroll(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            JPanel bottom = UIUtils.sectionPanel("Actions");
            JPanel actions = UIUtils.inlinePanel();
            JButton addBtn = UIUtils.gradientButton("Add Supplier", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
            JButton editBtn = UIUtils.gradientButton("Edit Supplier", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
            JButton deleteBtn = UIUtils.gradientButton("Delete Supplier", UIUtils.DANGER, UIUtils.DANGER_DARK);
            JButton refreshBtn = UIUtils.gradientButton("Refresh", UIUtils.PURPLE, UIUtils.PURPLE_DARK);
            actions.add(addBtn); actions.add(editBtn); actions.add(deleteBtn); actions.add(refreshBtn);
            bottom.add(actions, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> addSupplier());
            editBtn.addActionListener(e -> editSupplier());
            deleteBtn.addActionListener(e -> deleteSupplier());
            refreshBtn.addActionListener(e -> refresh());
        }

        @Override
        void refresh() {
            model.setRowCount(0);
            try {
                for (Supplier supplier : dao.getAll()) {
                    model.addRow(new Object[]{supplier.getId(), supplier.getName(), supplier.getContact(), supplier.getAddress()});
                }
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void addSupplier() {
            SupplierDialog dialog = new SupplierDialog(MainFrame.this, "Add Supplier", null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    dao.add(dialog.getSupplier());
                    refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }

        private void editSupplier() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a supplier first.");
                return;
            }
            Supplier supplier = new Supplier((Integer) model.getValueAt(row, 0), String.valueOf(model.getValueAt(row, 1)), String.valueOf(model.getValueAt(row, 2)), String.valueOf(model.getValueAt(row, 3)));
            SupplierDialog dialog = new SupplierDialog(MainFrame.this, "Edit Supplier", supplier);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    Supplier updated = dialog.getSupplier();
                    updated.setId(supplier.getId());
                    dao.update(updated);
                    refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }

        private void deleteSupplier() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a supplier first.");
                return;
            }
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Delete selected supplier?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    dao.delete((Integer) model.getValueAt(row, 0));
                    refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }
    }

    private class ServicePanel extends ModulePanel {
        private final ServiceDAO dao = new ServiceDAO();
        private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Customer", "Vehicle No", "Service Type", "Cost", "Date", "Notes"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable table = new JTable(model);

        ServicePanel() {
            UIUtils.styleTable(table);
            JPanel tableCard = UIUtils.sectionPanel("Service Records");
            tableCard.add(UIUtils.modernScroll(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            JPanel bottom = UIUtils.sectionPanel("Actions");
            JPanel actions = UIUtils.inlinePanel();
            JButton addBtn = UIUtils.gradientButton("Add Service", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
            JButton editBtn = UIUtils.gradientButton("Edit Service", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
            JButton deleteBtn = UIUtils.gradientButton("Delete Service", UIUtils.DANGER, UIUtils.DANGER_DARK);
            JButton refreshBtn = UIUtils.gradientButton("Refresh", UIUtils.PURPLE, UIUtils.PURPLE_DARK);
            actions.add(addBtn); actions.add(editBtn); actions.add(deleteBtn); actions.add(refreshBtn);
            bottom.add(actions, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> addService());
            editBtn.addActionListener(e -> editService());
            deleteBtn.addActionListener(e -> deleteService());
            refreshBtn.addActionListener(e -> refresh());
        }

        @Override
        void refresh() {
            model.setRowCount(0);
            try {
                for (ServiceRecord record : dao.getAll()) {
                    model.addRow(new Object[]{record.getId(), record.getCustomerName(), record.getVehicleNo(), record.getServiceType(), record.getCost(), record.getServiceDate(), record.getNotes()});
                }
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void addService() {
            ServiceDialog dialog = new ServiceDialog(MainFrame.this, "Add Service Record", null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    dao.add(dialog.getRecord());
                    refresh();
                    billingPanel.refresh();
                    dashboardPanel.refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }

        private void editService() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a service record first.");
                return;
            }
            ServiceRecord record = new ServiceRecord();
            record.setId((Integer) model.getValueAt(row, 0));
            record.setCustomerName(String.valueOf(model.getValueAt(row, 1)));
            record.setVehicleNo(String.valueOf(model.getValueAt(row, 2)));
            record.setServiceType(String.valueOf(model.getValueAt(row, 3)));
            record.setCost(new BigDecimal(String.valueOf(model.getValueAt(row, 4))));
            record.setServiceDate(LocalDate.parse(String.valueOf(model.getValueAt(row, 5))));
            record.setNotes(String.valueOf(model.getValueAt(row, 6)));

            ServiceDialog dialog = new ServiceDialog(MainFrame.this, "Edit Service Record", record);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    ServiceRecord updated = dialog.getRecord();
                    updated.setId(record.getId());
                    dao.update(updated);
                    refresh();
                    billingPanel.refresh();
                    dashboardPanel.refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }

        private void deleteService() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a service record first.");
                return;
            }
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Delete selected service record?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    dao.delete((Integer) model.getValueAt(row, 0));
                    refresh();
                    billingPanel.refresh();
                    dashboardPanel.refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }
    }

    private class BillingPanel extends ModulePanel {
        private final BillDAO billDAO = new BillDAO();
        private final ServiceDAO serviceDAO = new ServiceDAO();
        private final DefaultTableModel model = new DefaultTableModel(new String[]{"Bill ID", "Service ID", "Customer", "Vehicle No", "Service", "Amount", "Date"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable table = new JTable(model);
        private final JComboBox<ServiceRecord> serviceBox = new JComboBox<>();
        private final JTextField amountField = new JTextField(12);
        private final JTextArea invoicePreview = new JTextArea();

        BillingPanel() {
            UIUtils.styleCombo(serviceBox);
            UIUtils.styleTextField(amountField);
            serviceBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof ServiceRecord record) {
                        label.setText(record.getId() + " - " + record.getCustomerName() + " - " + record.getServiceType());
                    } else {
                        label.setText("Select service");
                    }
                    return label;
                }
            });
            serviceBox.addActionListener(e -> updateInvoicePreviewFromSelection());

            UIUtils.styleTable(table);

            JPanel top = UIUtils.sectionPanel("Generate Bill");
            JPanel form = UIUtils.inlinePanel();
            form.add(new JLabel("Service"));
            form.add(UIUtils.roundedInput(serviceBox));
            form.add(new JLabel("Total Amount"));
            form.add(UIUtils.roundedInput(amountField));
            JButton generate = UIUtils.gradientButton("Generate Bill", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
            JButton print = UIUtils.gradientButton("Print Selected Bill", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
            form.add(generate); form.add(print);
            top.add(form, BorderLayout.CENTER);
            add(top, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
            center.setOpaque(false);

            JPanel tableCard = UIUtils.sectionPanel("Bill Records");
            tableCard.add(UIUtils.modernScroll(table), BorderLayout.CENTER);
            center.add(tableCard);

            JPanel previewCard = UIUtils.sectionPanel("Invoice Preview");
            invoicePreview.setEditable(false);
            invoicePreview.setFont(UIUtils.font(15, false));
            invoicePreview.setLineWrap(true);
            invoicePreview.setWrapStyleWord(true);
            previewCard.add(UIUtils.modernScroll(invoicePreview), BorderLayout.CENTER);
            center.add(previewCard);
            add(center, BorderLayout.CENTER);

            generate.addActionListener(e -> generateBill());
            print.addActionListener(e -> printSelectedBill());
            table.getSelectionModel().addListSelectionListener(e -> updateInvoiceFromTable());
        }

        @Override
        void refresh() {
            model.setRowCount(0);
            try {
                serviceBox.removeAllItems();
                for (ServiceRecord record : serviceDAO.getAll()) {
                    serviceBox.addItem(record);
                }
                for (Bill bill : billDAO.getAll()) {
                    model.addRow(new Object[]{bill.getId(), bill.getServiceId(), bill.getCustomerName(), bill.getVehicleNo(), bill.getServiceType(), bill.getTotalAmount(), bill.getBillDate()});
                }
                updateInvoicePreviewFromSelection();
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void updateInvoicePreviewFromSelection() {
            ServiceRecord selected = (ServiceRecord) serviceBox.getSelectedItem();
            if (selected == null) {
                amountField.setText("");
                invoicePreview.setText("Select a service to preview invoice.");
                return;
            }
            amountField.setText(selected.getCost().toString());
            invoicePreview.setText(buildInvoiceText(0, selected.getId(), selected.getCustomerName(), selected.getVehicleNo(), selected.getServiceType(), new BigDecimal(amountField.getText().trim()), LocalDate.now()));
        }

        private void updateInvoiceFromTable() {
            int row = selectedModelRow(table);
            if (row == -1) return;
            invoicePreview.setText(buildInvoiceText(
                    (Integer) model.getValueAt(row, 0),
                    (Integer) model.getValueAt(row, 1),
                    String.valueOf(model.getValueAt(row, 2)),
                    String.valueOf(model.getValueAt(row, 3)),
                    String.valueOf(model.getValueAt(row, 4)),
                    new BigDecimal(String.valueOf(model.getValueAt(row, 5))),
                    LocalDate.parse(String.valueOf(model.getValueAt(row, 6)))
            ));
        }

        private void generateBill() {
            ServiceRecord selected = (ServiceRecord) serviceBox.getSelectedItem();
            if (selected == null) {
                UIUtils.showInfo(MainFrame.this, "Select a service record first.");
                return;
            }
            try {
                BigDecimal total = new BigDecimal(amountField.getText().trim());
                Bill bill = new Bill();
                bill.setServiceId(selected.getId());
                bill.setTotalAmount(total);
                bill.setBillDate(LocalDate.now());
                billDAO.add(bill);
                refresh();
                dashboardPanel.refresh();
                UIUtils.showInfo(MainFrame.this, "Bill generated successfully.");
            } catch (Exception ex) {
                UIUtils.showError(MainFrame.this, "Enter a valid bill amount.");
            }
        }

        private void printSelectedBill() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a bill first.");
                return;
            }
            JTextArea area = new JTextArea(invoicePreview.getText());
            area.setFont(UIUtils.font(14, false));
            try {
                area.print();
            } catch (PrinterException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private String buildInvoiceText(int billId, int serviceId, String customer, String vehicle, String serviceType, BigDecimal amount, LocalDate date) {
            return "GARAGE INVOICE\n\n" +
                    "Bill ID: " + (billId == 0 ? "(new)" : billId) + "\n" +
                    "Service ID: " + serviceId + "\n" +
                    "Customer Name: " + customer + "\n" +
                    "Vehicle Number: " + vehicle + "\n" +
                    "Service Type: " + serviceType + "\n" +
                    "Bill Date: " + date + "\n" +
                    "----------------------------------\n" +
                    "Total Amount: " + UIUtils.money(amount) + "\n\n" +
                    "Thank you for visiting our garage.";
        }
    }

    private class StaffManagementPanel extends ModulePanel {
        private final UserDAO dao = new UserDAO();
        private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Full Name", "Username", "Password"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable table = new JTable(model);

        StaffManagementPanel() {
            UIUtils.styleTable(table);
            JPanel tableCard = UIUtils.sectionPanel("Staff Management");
            tableCard.add(UIUtils.modernScroll(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            JPanel bottom = UIUtils.sectionPanel("Actions");
            JPanel actions = UIUtils.inlinePanel();
            JButton addBtn = UIUtils.gradientButton("Add Staff", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
            JButton editBtn = UIUtils.gradientButton("Edit Staff", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
            JButton deleteBtn = UIUtils.gradientButton("Delete Staff", UIUtils.DANGER, UIUtils.DANGER_DARK);
            JButton refreshBtn = UIUtils.gradientButton("Refresh", UIUtils.PURPLE, UIUtils.PURPLE_DARK);
            actions.add(addBtn); actions.add(editBtn); actions.add(deleteBtn); actions.add(refreshBtn);
            bottom.add(actions, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> addStaff());
            editBtn.addActionListener(e -> editStaff());
            deleteBtn.addActionListener(e -> deleteStaff());
            refreshBtn.addActionListener(e -> refresh());
        }

        @Override
        void refresh() {
            model.setRowCount(0);
            try {
                for (User user : dao.getAllStaff()) {
                    model.addRow(new Object[]{user.getId(), user.getFullName(), user.getUsername(), user.getPassword()});
                }
                dashboardPanel.refresh();
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void addStaff() {
            StaffDialog dialog = new StaffDialog(MainFrame.this, "Add Staff", null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    dao.addStaff(dialog.getUser());
                    refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }

        private void editStaff() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a staff record first.");
                return;
            }
            User user = new User((Integer) model.getValueAt(row, 0), String.valueOf(model.getValueAt(row, 2)), String.valueOf(model.getValueAt(row, 3)), "STAFF", String.valueOf(model.getValueAt(row, 1)));
            StaffDialog dialog = new StaffDialog(MainFrame.this, "Edit Staff", user);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    User updated = dialog.getUser();
                    updated.setId(user.getId());
                    dao.updateStaff(updated);
                    refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }

        private void deleteStaff() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a staff record first.");
                return;
            }
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Delete selected staff member?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    dao.deleteStaff((Integer) model.getValueAt(row, 0));
                    refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }
    }

    private class TaskManagementPanel extends ModulePanel {
        private final TaskDAO dao = new TaskDAO();
        private final UserDAO userDAO = new UserDAO();
        private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Title", "Assigned To", "Status", "Date", "Description"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable table = new JTable(model);

        TaskManagementPanel() {
            UIUtils.styleTable(table);
            table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
            JPanel tableCard = UIUtils.sectionPanel("Task Management");
            tableCard.add(UIUtils.modernScroll(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            JPanel bottom = UIUtils.sectionPanel("Actions");
            JPanel actions = UIUtils.inlinePanel();
            JButton addBtn = UIUtils.gradientButton("Add Task", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
            JButton editBtn = UIUtils.gradientButton("Edit Task", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
            JButton deleteBtn = UIUtils.gradientButton("Delete Task", UIUtils.DANGER, UIUtils.DANGER_DARK);
            JButton refreshBtn = UIUtils.gradientButton("Refresh", UIUtils.PURPLE, UIUtils.PURPLE_DARK);
            actions.add(addBtn); actions.add(editBtn); actions.add(deleteBtn); actions.add(refreshBtn);
            bottom.add(actions, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> addTask());
            editBtn.addActionListener(e -> editTask());
            deleteBtn.addActionListener(e -> deleteTask());
            refreshBtn.addActionListener(e -> refresh());
        }

        @Override
        void refresh() {
            model.setRowCount(0);
            try {
                for (TaskItem task : dao.getAll()) {
                    model.addRow(new Object[]{task.getId(), task.getTitle(), task.getAssignedToName(), task.getStatus(), task.getTaskDate(), task.getDescription()});
                }
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void addTask() {
            try {
                TaskDialog dialog = new TaskDialog(MainFrame.this, "Add Task", userDAO.getAllStaff(), null);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    dao.add(dialog.getTask());
                    refresh();
                    myTaskPanel.refresh();
                }
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void editTask() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a task first.");
                return;
            }
            try {
                List<TaskItem> tasks = dao.getAll();
                TaskItem selected = tasks.stream().filter(t -> t.getId() == (Integer) model.getValueAt(row, 0)).findFirst().orElse(null);
                if (selected == null) return;
                TaskDialog dialog = new TaskDialog(MainFrame.this, "Edit Task", userDAO.getAllStaff(), selected);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    TaskItem updated = dialog.getTask();
                    updated.setId(selected.getId());
                    dao.update(updated);
                    refresh();
                    myTaskPanel.refresh();
                }
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }

        private void deleteTask() {
            int row = selectedModelRow(table);
            if (row == -1) {
                UIUtils.showInfo(MainFrame.this, "Select a task first.");
                return;
            }
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Delete selected task?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    dao.delete((Integer) model.getValueAt(row, 0));
                    refresh();
                    myTaskPanel.refresh();
                } catch (SQLException ex) {
                    UIUtils.showError(MainFrame.this, ex.getMessage());
                }
            }
        }
    }

    private class MyTaskPanel extends ModulePanel {
        private final TaskDAO dao = new TaskDAO();
        private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Title", "Status", "Date", "Description"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        private final JTable table = new JTable(model);

        MyTaskPanel() {
            UIUtils.styleTable(table);
            table.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());
            JPanel tableCard = UIUtils.sectionPanel("My Assigned Tasks");
            tableCard.add(UIUtils.modernScroll(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);
        }

        @Override
        void refresh() {
            model.setRowCount(0);
            try {
                for (TaskItem task : dao.getByStaff(currentUser.getId())) {
                    model.addRow(new Object[]{task.getId(), task.getTitle(), task.getStatus(), task.getTaskDate(), task.getDescription()});
                }
            } catch (SQLException ex) {
                UIUtils.showError(MainFrame.this, ex.getMessage());
            }
        }
    }

    private int selectedModelRow(JTable table) {
        int row = table.getSelectedRow();
        return row == -1 ? -1 : table.convertRowIndexToModel(row);
    }

    private static class QuantityCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int qty = Integer.parseInt(String.valueOf(value));
            if (!isSelected) {
                c.setForeground(qty <= AppConfig.LOW_STOCK_THRESHOLD ? UIUtils.DANGER_DARK : UIUtils.TEXT_DARK);
                c.setBackground(qty <= AppConfig.LOW_STOCK_THRESHOLD ? new Color(255, 236, 236) : Color.WHITE);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String text = value == null ? "" : value.toString();
            if (!isSelected) {
                Color bg = Color.WHITE;
                Color fg = UIUtils.TEXT_DARK;
                if ("Low".equalsIgnoreCase(text) || "Pending".equalsIgnoreCase(text)) {
                    bg = new Color(255, 236, 236);
                    fg = UIUtils.DANGER_DARK;
                } else if ("In Progress".equalsIgnoreCase(text)) {
                    bg = new Color(255, 246, 220);
                    fg = new Color(178, 110, 14);
                } else if ("Completed".equalsIgnoreCase(text) || "Good".equalsIgnoreCase(text)) {
                    bg = new Color(232, 248, 236);
                    fg = UIUtils.SUCCESS_DARK;
                }
                c.setBackground(bg);
                c.setForeground(fg);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }
}
