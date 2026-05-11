package com.garageims.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.garageims.dao.UserDAO;
import com.garageims.model.User;
import com.garageims.util.UIUtils;

public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JComboBox<String> roleBox = new JComboBox<>(new String[] { "ADMIN", "STAFF" });

    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        this(null);
    }

    public LoginFrame(String preselectedRole) {
        setTitle("Login - Garage Inventory & Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new UIUtils.GradientPanel();
        root.setLayout(new BorderLayout(12, 14));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        JPanel card = new UIUtils.RoundedPanel();
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(30, 30, 24, 30));
        root.add(card, BorderLayout.CENTER);

        JLabel title = new JLabel("🔐 Login", JLabel.CENTER);
        title.setFont(UIUtils.font(30, true));
        title.setForeground(UIUtils.TEXT_DARK);
        card.add(title, BorderLayout.NORTH);

        UIUtils.styleTextField(usernameField);
        UIUtils.styleTextField(passwordField);
        UIUtils.styleCombo(roleBox);

        if (preselectedRole != null) {
            roleBox.setSelectedItem(preselectedRole.toUpperCase());
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, 0, "Username", usernameField);
        addField(form, gbc, 1, "Password", passwordField);
        addField(form, gbc, 2, "Role", roleBox);

        card.add(form, BorderLayout.CENTER);

        JPanel actions = UIUtils.inlinePanel();
        var loginBtn = UIUtils.gradientButton("Login", UIUtils.PRIMARY, UIUtils.PRIMARY_DARK);
        var backBtn = UIUtils.gradientButton("Back", UIUtils.WARNING, UIUtils.WARNING_DARK);
        var exitBtn = UIUtils.gradientButton("Exit", UIUtils.DANGER, UIUtils.DANGER_DARK);

        actions.add(loginBtn);
        actions.add(backBtn);
        actions.add(exitBtn);
        card.add(actions, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> doLogin());
        backBtn.addActionListener(e -> {
            new HomeFrame().setVisible(true);
            dispose();
        });
        exitBtn.addActionListener(e -> System.exit(0));

        getRootPane().setDefaultButton(loginBtn);

        pack();
        UIUtils.applyLoginFrameSize(this);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, javax.swing.JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(UIUtils.subtitle(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(UIUtils.roundedInput(field), gbc);
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String selectedRole = String.valueOf(roleBox.getSelectedItem());

        if (UIUtils.isBlank(username) || UIUtils.isBlank(password)) {
            UIUtils.showError(this, "Enter username and password.");
            return;
        }

        try {
            User user = userDAO.login(username, password);
            if (user == null) {
                UIUtils.showError(this, "Invalid username or password.");
                return;
            }

            if (!selectedRole.equalsIgnoreCase(user.getRole())) {
                UIUtils.showError(this, "Selected role does not match this account.");
                return;
            }

            new MainFrame(user).setVisible(true);
            dispose();
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
}