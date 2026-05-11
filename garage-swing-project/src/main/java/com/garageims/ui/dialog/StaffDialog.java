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

import com.garageims.model.User;
import com.garageims.util.UIUtils;

public class StaffDialog extends JDialog {

    private boolean saved = false;

    private final JTextField fullNameField = new JTextField();
    private final JTextField usernameField = new JTextField();
    private final JTextField passwordField = new JTextField();

    public StaffDialog(Frame owner, String title, User user) {
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

        UIUtils.styleTextField(fullNameField);
        UIUtils.styleTextField(usernameField);
        UIUtils.styleTextField(passwordField);

        JPanel form = new UIUtils.RoundedPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, 0, "Full Name", fullNameField);
        addField(form, gbc, 1, "Username", usernameField);
        addField(form, gbc, 2, "Password", passwordField);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = UIUtils.inlinePanel();
        actions.setBorder(new EmptyBorder(2, 0, 0, 0));
        var saveBtn = UIUtils.gradientButton("Save Staff", UIUtils.SUCCESS, UIUtils.SUCCESS_DARK);
        var cancelBtn = UIUtils.gradientButton("Cancel", UIUtils.DANGER, UIUtils.DANGER_DARK);
        actions.add(saveBtn);
        actions.add(cancelBtn);
        root.add(actions, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        if (user != null) {
            fullNameField.setText(user.getFullName());
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
        }

        pack();
        setSize(980, 460);
        setLocationRelativeTo(owner);
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
        if (UIUtils.isBlank(fullNameField.getText())
                || UIUtils.isBlank(usernameField.getText())
                || UIUtils.isBlank(passwordField.getText())) {
            UIUtils.showError(this, "All fields are required.");
            return;
        }
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public User getUser() {
        User user = new User();
        user.setFullName(fullNameField.getText().trim());
        user.setUsername(usernameField.getText().trim());
        user.setPassword(passwordField.getText().trim());
        user.setRole("STAFF");
        return user;
    }
}