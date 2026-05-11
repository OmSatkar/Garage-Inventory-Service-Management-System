package com.garageims.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.garageims.model.TaskItem;
import com.garageims.model.User;
import com.garageims.util.UIUtils;

public class TaskDialog extends JDialog {

    private boolean saved = false;

    private final JComboBox<User> staffBox;
    private final JTextField titleField = new JTextField();
    private final JTextArea descArea = new JTextArea(3, 20);
    private final JComboBox<String> statusBox = new JComboBox<>(
            new String[] { "Pending", "In Progress", "Completed" });
    private final JTextField dateField = new JTextField(LocalDate.now().toString());

    public TaskDialog(Frame owner, String title, List<User> staffList, TaskItem task) {
        super(owner, title, true);

        staffBox = new JComboBox<>(staffList.toArray(new User[0]));

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

        UIUtils.styleTextField(titleField);
        UIUtils.styleTextField(dateField);
        UIUtils.styleCombo(staffBox);
        UIUtils.styleCombo(statusBox);

        descArea.setFont(UIUtils.font(15, false));
        descArea.setForeground(UIUtils.TEXT_DARK);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel form = new UIUtils.RoundedPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, 0, "Assign To", staffBox);
        addField(form, gbc, 1, "Title", titleField);
        addField(form, gbc, 2, "Description", descArea);
        addField(form, gbc, 3, "Status", statusBox);
        addField(form, gbc, 4, "Date (YYYY-MM-DD)", dateField);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = UIUtils.inlinePanel();
        actions.setBorder(new EmptyBorder(2, 0, 0, 0));
        var saveBtn = UIUtils.gradientButton("Save Task", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
        var cancelBtn = UIUtils.gradientButton("Cancel", UIUtils.DANGER, UIUtils.DANGER_DARK);
        actions.add(saveBtn);
        actions.add(cancelBtn);
        root.add(actions, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        if (task != null) {
            titleField.setText(task.getTitle());
            descArea.setText(task.getDescription());
            statusBox.setSelectedItem(task.getStatus());
            if (task.getTaskDate() != null) {
                dateField.setText(task.getTaskDate().toString());
            }

            if (task.getAssignedTo() != null) {
                for (int i = 0; i < staffBox.getItemCount(); i++) {
                    User user = staffBox.getItemAt(i);
                    if (user != null && user.getId() == task.getAssignedTo()) {
                        staffBox.setSelectedIndex(i);
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
            if (UIUtils.isBlank(titleField.getText())) {
                UIUtils.showError(this, "Task title is required.");
                return;
            }
            LocalDate.parse(dateField.getText().trim());
            saved = true;
            dispose();
        } catch (Exception ex) {
            UIUtils.showError(this, "Enter a valid date.");
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public TaskItem getTask() {
        TaskItem task = new TaskItem();
        User selected = (User) staffBox.getSelectedItem();
        task.setAssignedTo(selected == null ? null : selected.getId());
        task.setAssignedToName(selected == null ? null : selected.getFullName());
        task.setTitle(titleField.getText().trim());
        task.setDescription(descArea.getText().trim());
        task.setStatus((String) statusBox.getSelectedItem());
        task.setTaskDate(LocalDate.parse(dateField.getText().trim()));
        return task;
    }
}