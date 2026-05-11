package com.garageims.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.garageims.model.ServiceRecord;
import com.garageims.util.UIUtils;

public class ServiceDialog extends JDialog {

    private boolean saved = false;

    private final JTextField customerField = new JTextField();
    private final JTextField vehicleField = new JTextField();
    private final JTextField serviceTypeField = new JTextField();
    private final JTextField costField = new JTextField();
    private final JTextField dateField = new JTextField(LocalDate.now().toString());
    private final JTextArea notesArea = new JTextArea(3, 20);

    public ServiceDialog(Frame owner, String title, ServiceRecord record) {
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

        UIUtils.styleTextField(customerField);
        UIUtils.styleTextField(vehicleField);
        UIUtils.styleTextField(serviceTypeField);
        UIUtils.styleTextField(costField);
        UIUtils.styleTextField(dateField);

        notesArea.setFont(UIUtils.font(15, false));
        notesArea.setForeground(UIUtils.TEXT_DARK);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setOpaque(false);
        notesArea.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel form = new UIUtils.RoundedPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, 0, "Customer Name", customerField);
        addField(form, gbc, 1, "Vehicle No", vehicleField);
        addField(form, gbc, 2, "Service Type", serviceTypeField);
        addField(form, gbc, 3, "Cost", costField);
        addField(form, gbc, 4, "Date (YYYY-MM-DD)", dateField);
        addField(form, gbc, 5, "Notes", notesArea);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = UIUtils.inlinePanel();
        actions.setBorder(new EmptyBorder(2, 0, 0, 0));
        var saveBtn = UIUtils.gradientButton("Save Service", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
        var cancelBtn = UIUtils.gradientButton("Cancel", UIUtils.DANGER, UIUtils.DANGER_DARK);
        actions.add(saveBtn);
        actions.add(cancelBtn);
        root.add(actions, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        if (record != null) {
            customerField.setText(record.getCustomerName());
            vehicleField.setText(record.getVehicleNo());
            serviceTypeField.setText(record.getServiceType());
            costField.setText(String.valueOf(record.getCost()));
            dateField.setText(record.getServiceDate() != null ? record.getServiceDate().toString() : LocalDate.now().toString());
            notesArea.setText(record.getNotes());
        }

        UIUtils.applyDialogSize(this, owner, 1120, 620);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panel.add(UIUtils.subtitle(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        if (field instanceof JTextArea area) {
            area.setRows(3);
            area.setPreferredSize(new java.awt.Dimension(420, 90));
            area.setMinimumSize(new java.awt.Dimension(420, 90));
            panel.add(UIUtils.roundedInput(area), gbc);
        } else {
            panel.add(UIUtils.roundedInput(field), gbc);
        }
    }

    private void onSave() {
        try {
            if (UIUtils.isBlank(customerField.getText())
                    || UIUtils.isBlank(vehicleField.getText())
                    || UIUtils.isBlank(serviceTypeField.getText())
                    || UIUtils.isBlank(costField.getText())
                    || UIUtils.isBlank(dateField.getText())) {
                UIUtils.showError(this, "Please fill all required fields.");
                return;
            }

            new BigDecimal(costField.getText().trim());
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

    public ServiceRecord getRecord() {
        ServiceRecord record = new ServiceRecord();
        record.setCustomerName(customerField.getText().trim());
        record.setVehicleNo(vehicleField.getText().trim());
        record.setServiceType(serviceTypeField.getText().trim());
        record.setCost(new BigDecimal(costField.getText().trim()));
        record.setServiceDate(LocalDate.parse(dateField.getText().trim()));
        record.setNotes(notesArea.getText().trim());
        return record;
    }
}