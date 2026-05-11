package com.garageims.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.garageims.model.Supplier;
import com.garageims.util.UIUtils;

public class SupplierDialog extends JDialog {

    private boolean saved = false;

    private final JTextField nameField = new JTextField();
    private final JTextField contactField = new JTextField();
    private final JTextField addressField = new JTextField();

    public SupplierDialog(Frame owner, String title, Supplier supplier) {
        super(owner, title, true);

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
        UIUtils.styleTextField(contactField);
        UIUtils.styleTextField(addressField);

        JPanel form = new UIUtils.RoundedPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, 0, "Supplier Name", nameField);
        addField(form, gbc, 1, "Contact", contactField);
        addField(form, gbc, 2, "Address", addressField);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = UIUtils.inlinePanel();
        actions.setBorder(new EmptyBorder(2, 0, 0, 0));
        var saveBtn = UIUtils.gradientButton("Save Supplier", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
        var cancelBtn = UIUtils.gradientButton("Cancel", UIUtils.DANGER, UIUtils.DANGER_DARK);
        actions.add(saveBtn);
        actions.add(cancelBtn);
        root.add(actions, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        if (supplier != null) {
            nameField.setText(supplier.getName());
            contactField.setText(supplier.getContact());
            addressField.setText(supplier.getAddress());
        }

        UIUtils.applyDialogSize(this, owner, 980, 460);
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
        if (UIUtils.isBlank(nameField.getText())) {
            UIUtils.showError(this, "Supplier name is required.");
            return;
        }
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public Supplier getSupplier() {
        return new Supplier(
                0,
                nameField.getText().trim(),
                contactField.getText().trim(),
                addressField.getText().trim()
        );
    }
}