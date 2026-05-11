package com.garageims.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.garageims.util.UIUtils;

public class HomeFrame extends JFrame {

    private final JLabel welcomeLabel = new JLabel("", JLabel.LEFT);
    private final JLabel subTitleLabel = new JLabel(
            "<html><div style='width:470px;'>A smart desktop workspace for managing inventory, suppliers, services, billing, staff and daily garage operations efficiently.</div></html>");
    private final JLabel marqueeLabel = new JLabel("", JLabel.CENTER);
    private final JLabel clockLabel = new JLabel("", JLabel.LEFT);

    private final String typingText = "Welcome to GarageFlow";
    private int typingIndex = 0;

    private final String marqueeText =
            "   Smart Garage Operations  •  Inventory  •  Services  •  Billing  •  Suppliers  •  Staff  •  Tasks  •  Fast  •  Reliable  •   ";
    private int marqueeIndex = 0;

    public HomeFrame() {
        setTitle("GarageFlow - Garage Inventory & Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new PremiumBackgroundPanel();
        root.setLayout(new BorderLayout(20, 20));
        root.setBorder(new EmptyBorder(22, 22, 22, 22));
        setContentPane(root);

        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildCenterSection(), BorderLayout.CENTER);
        root.add(buildBottomBar(), BorderLayout.SOUTH);

        UIUtils.applyFrameSize(this, 1220, 720);
        setResizable(false);

        startTypingAnimation();
        startMarqueeAnimation();
        startClockAnimation();
    }

    private JPanel buildTopBar() {
        JPanel topBar = new GlassCardPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("GarageFlow");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(24, 52, 84));

        JLabel sub = new JLabel("Garage Inventory & Management System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(90, 108, 128));

        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(sub);

        JLabel logoPlaceholder = new JLabel("LOGO", JLabel.CENTER);
//        ImageIcon icon = new ImageIcon("C:\\Users\\Dell\\Downloads\\img1.png");
//        Image img = icon.getImage();
//        // Resize to 100x100 pixels with smooth scaling
//        Image scaledImg = img.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
//        logoPlaceholder.setIcon(new ImageIcon(scaledImg));


        
        logoPlaceholder.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoPlaceholder.setForeground(new Color(70, 98, 130));
        logoPlaceholder.setPreferredSize(new Dimension(150, 48));
        logoPlaceholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 205, 228), 1, true),
                new EmptyBorder(8, 14, 8, 14)));

        topBar.add(left, BorderLayout.WEST);
        topBar.add(logoPlaceholder, BorderLayout.EAST);
        return topBar;
    }

    private JPanel buildCenterSection() {
        JPanel center = new JPanel(new GridLayout(1, 2, 20, 20));
        center.setOpaque(false);

        center.add(buildHeroPanel());
        center.add(buildFeaturePanel());

        return center;
    }

    private JPanel buildHeroPanel() {
        JPanel hero = new GlassCardPanel();
        hero.setLayout(new BorderLayout(0, 18));
        hero.setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        RoundedBadge iconBadge = new RoundedBadge("GMS", new Color(52, 152, 219), new Color(41, 128, 185));
        iconBadge.setAlignmentX(LEFT_ALIGNMENT);

        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        welcomeLabel.setForeground(new Color(28, 50, 79));
        welcomeLabel.setAlignmentX(LEFT_ALIGNMENT);

        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        subTitleLabel.setForeground(new Color(74, 93, 118));
        subTitleLabel.setAlignmentX(LEFT_ALIGNMENT);

        clockLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        clockLabel.setForeground(new Color(32, 102, 201));
        clockLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel smallInfo = new JLabel("Efficient • Organized • Professional Garage Management"); //instead line- "Professional garage operations made simple, fast and organized."
        smallInfo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        smallInfo.setForeground(new Color(88, 104, 124));
        smallInfo.setAlignmentX(LEFT_ALIGNMENT);

        content.add(iconBadge);
        content.add(Box.createVerticalStrut(16));
        content.add(welcomeLabel);
        content.add(Box.createVerticalStrut(14));
        content.add(subTitleLabel);
        content.add(Box.createVerticalStrut(18));
        content.add(clockLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(smallInfo);

        hero.add(content, BorderLayout.NORTH);
        hero.add(buildButtonPanel(), BorderLayout.SOUTH);

        return hero;
    }

    private JPanel buildFeaturePanel() {
        JPanel outer = new GlassCardPanel();
        outer.setLayout(new BorderLayout(0, 16));
        outer.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel heading = new JLabel("System Highlights");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(28, 50, 79));

        JPanel grid = new JPanel(new GridLayout(3, 2, 18, 18));
        grid.setOpaque(false);

        grid.add(featureCard("IC", new Color(52, 152, 219), "Inventory Control",
                "Track stock, categories and low stock alerts"));
        grid.add(featureCard("BS", new Color(155, 89, 182), "Billing System",
                "Generate and print professional invoices"));
        grid.add(featureCard("SR", new Color(46, 204, 113), "Service Records",
                "Manage vehicle service details efficiently"));
        grid.add(featureCard("SP", new Color(241, 196, 15), "Suppliers",
                "Maintain supplier details and linkage"));
        grid.add(featureCard("SM", new Color(26, 188, 156), "Staff Management",
                "Control staff users and responsibilities"));
        grid.add(featureCard("TT", new Color(231, 76, 60), "Task Tracking",
                "Assign and monitor staff tasks easily"));

        outer.add(heading, BorderLayout.NORTH);
        outer.add(grid, BorderLayout.CENTER);

        return outer;
    }

    private JPanel featureCard(String badgeText, Color badgeColor, String title, String desc) {
        JPanel card = new SoftFeatureCard();
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        RoundedBadge badge = new RoundedBadge(badgeText, badgeColor, darker(badgeColor));
        JLabel titleLabel = new JLabel("<html><b>" + title + "</b></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(31, 57, 92));

        JLabel descLabel = new JLabel(
                "<html><div style='width:220px;'>" + desc + "</div></html>"
        );
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(96, 112, 131));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setOpaque(false);
        top.add(badge);
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        descLabel.setAlignmentX(LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(descLabel);

        card.add(top, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildButtonPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row1.setOpaque(false);

        PremiumButton adminBtn = new PremiumButton("Login as Admin",
                new Color(52, 152, 219), new Color(41, 128, 185));
        PremiumButton staffBtn = new PremiumButton("Login as Staff",
                new Color(46, 204, 113), new Color(39, 174, 96));

        adminBtn.addActionListener(e -> openLogin("ADMIN"));
        staffBtn.addActionListener(e -> openLogin("STAFF"));

        row1.add(adminBtn);
        row1.add(staffBtn);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row2.setOpaque(false);

        PremiumButton aboutBtn = new PremiumButton("About System",
                new Color(241, 196, 15), new Color(243, 156, 18));
        PremiumButton exitBtn = new PremiumButton("Exit",
                new Color(231, 76, 60), new Color(192, 57, 43));

        aboutBtn.addActionListener(e -> showAboutDialog());
        exitBtn.addActionListener(e -> System.exit(0));

        row2.add(aboutBtn);
        row2.add(exitBtn);

        wrapper.add(row1);
        wrapper.add(Box.createVerticalStrut(14));
        wrapper.add(row2);

        return wrapper;
    }

    private JPanel buildBottomBar() {
        JPanel bottom = new GlassCardPanel();
        bottom.setLayout(new BorderLayout());
        bottom.setBorder(new EmptyBorder(12, 18, 12, 18));

        marqueeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        marqueeLabel.setForeground(new Color(32, 102, 201));

        bottom.add(marqueeLabel, BorderLayout.CENTER);
        return bottom;
    }

    private void openLogin(String role) {
        new LoginFrame(role).setVisible(true);
        dispose();
    }

    private void showAboutDialog() {
        String text = """
                GarageFlow

                Garage Inventory & Management System

                Modules:
                • Inventory Management
                • Supplier Management
                • Service Records
                • Billing System
                • Staff Management
                • Task Management

                Technology:
                • Java Swing
                • JDBC
                • PostgreSQL
                """;
        UIUtils.showInfo(this, text);
    }

    private void startTypingAnimation() {
        Timer timer = new Timer(55, e -> {
            if (typingIndex <= typingText.length()) {
                welcomeLabel.setText(typingText.substring(0, typingIndex));
                typingIndex++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setInitialDelay(250);
        timer.start();
    }

    private void startMarqueeAnimation() {
        Timer timer = new Timer(120, e -> {
            String shown = marqueeText.substring(marqueeIndex) + marqueeText.substring(0, marqueeIndex);
            marqueeLabel.setText(shown);
            marqueeIndex = (marqueeIndex + 1) % marqueeText.length();
        });
        timer.start();
    }

    private void startClockAnimation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy  |  hh:mm:ss a");
        Timer timer = new Timer(1000, e -> clockLabel.setText(LocalDateTime.now().format(formatter)));
        timer.setInitialDelay(0);
        timer.start();
    }

    private Color darker(Color c) {
        return new Color(
                Math.max(0, c.getRed() - 28),
                Math.max(0, c.getGreen() - 28),
                Math.max(0, c.getBlue() - 28));
    }

    private static class PremiumBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(214, 232, 255),
                    getWidth(), getHeight(), new Color(183, 212, 245));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 65));
            g2.fill(new Ellipse2D.Double(-90, 60, 320, 320));
            g2.fill(new Ellipse2D.Double(getWidth() - 250, 30, 280, 280));
            g2.fill(new Ellipse2D.Double(getWidth() / 2.0 - 160, getHeight() - 120, 320, 190));

            g2.setColor(new Color(255, 255, 255, 40));
            g2.fill(new Ellipse2D.Double(120, getHeight() - 170, 180, 180));
            g2.fill(new Ellipse2D.Double(getWidth() - 380, getHeight() - 220, 220, 220));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class GlassCardPanel extends JPanel {
        GlassCardPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(6, 8, getWidth() - 12, getHeight() - 10, 28, 28);

            g2.setColor(new Color(248, 252, 255, 232));
            g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 28, 28);

            g2.setColor(new Color(184, 207, 231));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 9, getHeight() - 9, 28, 28);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class SoftFeatureCard extends JPanel {
        private boolean hovered = false;

        SoftFeatureCard() {
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, hovered ? 20 : 12));
            g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 22, 22);

            g2.setColor(hovered ? new Color(252, 254, 255) : new Color(247, 251, 255, 240));
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 22, 22);

            g2.setColor(new Color(201, 220, 238));
            g2.drawRoundRect(0, 0, getWidth() - 7, getHeight() - 7, 22, 22);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedBadge extends JPanel {
        private final String text;
        private final Color c1;
        private final Color c2;

        RoundedBadge(String text, Color c1, Color c2) {
            this.text = text;
            this.c1 = c1;
            this.c2 = c2;
            setOpaque(false);
            setPreferredSize(new Dimension(44, 44));
            setMaximumSize(new Dimension(44, 44));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
            g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

            Font f = new Font("Segoe UI", Font.BOLD, 14);
            g2.setFont(f);
            var fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class PremiumButton extends JButton {
        private final Color topColor;
        private final Color bottomColor;
        private boolean hovered = false;

        PremiumButton(String text, Color topColor, Color bottomColor) {
            super(text);
            this.topColor = topColor;
            this.bottomColor = bottomColor;

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(190, 48));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color t = hovered ? brighten(topColor, 18) : topColor;
            Color b = hovered ? brighten(bottomColor, 18) : bottomColor;

            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(6, 8, getWidth() - 10, getHeight() - 10, 18, 18);

            g2.setColor(new Color(0, 0, 0, hovered ? 32 : 20));
            g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 18, 18);

            g2.setPaint(new GradientPaint(0, 0, t, 0, getHeight(), b));
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 18, 18);

            g2.setColor(new Color(255, 255, 255, hovered ? 90 : 55));
            g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 18, 18);

            g2.dispose();
            super.paintComponent(g);
        }

        private Color brighten(Color color, int amount) {
            return new Color(
                    Math.min(255, color.getRed() + amount),
                    Math.min(255, color.getGreen() + amount),
                    Math.min(255, color.getBlue() + amount));
        }
    }
}