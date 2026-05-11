package com.garageims.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.garageims.model.InventoryItem;
import com.garageims.model.Supplier;
import com.garageims.util.UIUtils;

public class InventoryDialog extends JDialog {

    private boolean saved = false;

    private final JTextField nameField = new JTextField();
    private final JComboBox<String> categoryBox = new JComboBox<>(
            new String[] { "Engine", "Oil", "Tyre", "Electrical", "Service", "General" });
    private final JTextField qtyField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JComboBox<Supplier> supplierBox;
    private final JTextField dateField = new JTextField(LocalDate.now().toString());

    public InventoryDialog(Frame owner, String title, List<Supplier> suppliers, InventoryItem item) {
        super(owner, title, true);

        supplierBox = new JComboBox<>(suppliers.toArray(new Supplier[0]));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel root = new UIUtils.GradientPanel();
        root.setLayout(new BorderLayout(12, 14));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        JLabel heading = new JLabel(title);
        heading.setFont(UIUtils.font(21, true));
        heading.setForeground(UIUtils.TEXT_DARK);
        root.add(heading, BorderLayout.NORTH);

        UIUtils.styleTextField(nameField);
        UIUtils.styleTextField(qtyField);
        UIUtils.styleTextField(priceField);
        UIUtils.styleTextField(dateField);
        UIUtils.styleCombo(categoryBox);
        UIUtils.styleCombo(supplierBox);

        JPanel form = new UIUtils.RoundedPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, 0, "Item Name", nameField);
        addField(form, gbc, 1, "Category", categoryBox);
        addField(form, gbc, 2, "Quantity", qtyField);
        addField(form, gbc, 3, "Price", priceField);
        addField(form, gbc, 4, "Supplier", supplierBox);
        addField(form, gbc, 5, "Date (YYYY-MM-DD)", dateField);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = UIUtils.inlinePanel();
        actions.setBorder(new EmptyBorder(2, 0, 0, 0));
        var saveBtn = UIUtils.gradientButton("Save Item", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
        var cancelBtn = UIUtils.gradientButton("Cancel", UIUtils.DANGER, UIUtils.DANGER_DARK);
        actions.add(saveBtn);
        actions.add(cancelBtn);
        root.add(actions, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        if (item != null) {
            nameField.setText(item.getName());
            categoryBox.setSelectedItem(item.getCategory());
            qtyField.setText(String.valueOf(item.getQuantity()));
            priceField.setText(String.valueOf(item.getPrice()));
            dateField.setText(item.getDateAdded() != null ? item.getDateAdded().toString() : LocalDate.now().toString());

            if (item.getSupplierId() > 0) {
                for (int i = 0; i < supplierBox.getItemCount(); i++) {
                    Supplier s = supplierBox.getItemAt(i);
                    if (s != null && s.getId() == item.getSupplierId()) {
                        supplierBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        UIUtils.applyDialogSize(this, owner, 1080, 560);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(UIUtils.subtitle(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(UIUtils.roundedInput(field), gbc);
    }

    private void onSave() {
        try {
            if (UIUtils.isBlank(nameField.getText())
                    || UIUtils.isBlank(qtyField.getText())
                    || UIUtils.isBlank(priceField.getText())
                    || supplierBox.getSelectedItem() == null
                    || UIUtils.isBlank(dateField.getText())) {
                UIUtils.showError(this, "Please fill all fields.");
                return;
            }

            Integer.parseInt(qtyField.getText().trim());
            new BigDecimal(priceField.getText().trim());
            LocalDate.parse(dateField.getText().trim());

            saved = true;
            dispose();
        } catch (Exception e) {
            UIUtils.showError(this, "Please enter valid values.");
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public InventoryItem getItem() {
        InventoryItem item = new InventoryItem();

        item.setName(nameField.getText().trim());
        item.setCategory(String.valueOf(categoryBox.getSelectedItem()));
        item.setQuantity(Integer.parseInt(qtyField.getText().trim()));
        item.setPrice(new BigDecimal(priceField.getText().trim()));

        Supplier selectedSupplier = (Supplier) supplierBox.getSelectedItem();
        if (selectedSupplier != null) {
            item.setSupplierId(selectedSupplier.getId());
            item.setSupplierName(selectedSupplier.getName());
        }

        item.setDateAdded(LocalDate.parse(dateField.getText().trim()));
        return item;
    }
}