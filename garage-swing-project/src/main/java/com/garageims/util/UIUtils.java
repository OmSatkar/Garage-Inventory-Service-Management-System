package com.garageims.util;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

import com.garageims.config.AppConfig;

public final class UIUtils {
    public static final Color BG = new Color(236, 240, 246);
    public static final Color PANEL_TOP = new Color(218, 237, 251);
    public static final Color PANEL_BOTTOM = new Color(190, 219, 243);
    public static final Color TEXT_DARK = new Color(39, 58, 86);
    public static final Color TEXT_MUTED = new Color(92, 109, 128);
    public static final Color BORDER = new Color(156, 188, 218);
    public static final Color WHITE = Color.WHITE;

    public static final Color PRIMARY = new Color(45, 149, 247);
    public static final Color PRIMARY_DARK = new Color(19, 100, 216);
    public static final Color SUCCESS = new Color(149, 209, 107);
    public static final Color SUCCESS_DARK = new Color(73, 166, 95);
    public static final Color WARNING = new Color(255, 187, 67);
    public static final Color WARNING_DARK = new Color(232, 142, 36);
    public static final Color DANGER = new Color(247, 100, 107);
    public static final Color DANGER_DARK = new Color(223, 61, 79);
    public static final Color PURPLE = new Color(131, 116, 240);
    public static final Color PURPLE_DARK = new Color(88, 70, 206);

    private UIUtils() {}
    
    public static final Dimension MAIN_FRAME_SIZE = new Dimension(1366, 768);
    public static final Dimension LOGIN_FRAME_SIZE = new Dimension(760, 500);

    public static void applyFrameSize(javax.swing.JFrame frame, int width, int height) {
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
    }

    public static void applyMainFrameSize(javax.swing.JFrame frame) {
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.setSize(MAIN_FRAME_SIZE);
        frame.setLocationRelativeTo(null);
    }

    public static void applyLoginFrameSize(javax.swing.JFrame frame) {
        frame.setResizable(false);
        frame.setSize(LOGIN_FRAME_SIZE);
        frame.setLocationRelativeTo(null);
    }

    public static void applyDialogSize(javax.swing.JDialog dialog, java.awt.Component owner, int width, int height) {
        dialog.pack();
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(owner);
        dialog.setResizable(false);
    }

    public static JScrollPane wrapPageScroll(JPanel panel) {
        JScrollPane scrollPane = modernScroll(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setOpaque(false);
        return scrollPane;
    }

    public static void setModernDefaults() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", true);
    }

    public static Font font(int size, boolean bold) {
        return new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size);
    }

    public static Border padding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static JPanel transparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    public static JPanel sectionPanel(String title) {
        JPanel wrapper = new RoundedPanel();
        wrapper.setLayout(new BorderLayout(0, 12));
        wrapper.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel heading = new JLabel(title);
        heading.setFont(font(20, true));
        heading.setForeground(TEXT_DARK);
        wrapper.add(heading, BorderLayout.NORTH);
        return wrapper;
    }

    public static JPanel inlinePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        return panel;
    }

    public static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(14, true));
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static void styleTextField(JTextComponent field) {
        field.setFont(font(15, false));
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(10, 12, 10, 12));
        field.setForeground(TEXT_DARK);
        field.setMargin(new Insets(0, 0, 0, 0));
    }

    public static void styleCombo(JComboBox<?> combo) {
        combo.setFont(font(15, false));
        combo.setOpaque(false);
        combo.setBackground(WHITE);
        combo.setForeground(TEXT_DARK);
        combo.setBorder(new EmptyBorder(6, 8, 6, 8));
    }

    public static JButton gradientButton(String text, Color c1, Color c2) {
        return new GradientButton(text, c1, c2);
    }

    public static JComponent roundedInput(JComponent input) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(input);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        return new RoundedInputWrapper(panel);
    }

    public static JScrollPane modernScroll(JComponent comp) {
        JScrollPane sp = new JScrollPane(comp);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);
        return sp;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(font(14, false));
        table.setForeground(TEXT_DARK);
        table.setGridColor(new Color(219, 228, 236));
        table.setSelectionBackground(new Color(198, 227, 252));
        table.setSelectionForeground(TEXT_DARK);
        table.setAutoCreateRowSorter(true);
        JTableHeader header = table.getTableHeader();
        header.setFont(font(14, true));
        header.setForeground(TEXT_DARK);
        header.setBackground(new Color(238, 245, 251));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));
    }

    public static JLabel statusChip(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(6, 12, 6, 12));
        label.setFont(font(13, true));
        return label;
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String money(BigDecimal amount) {
        if (amount == null) return "₹ 0.00";
        return "₹ " + amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static String lowStockText(int qty) {
        return qty <= AppConfig.LOW_STOCK_THRESHOLD ? "Low" : "Good";
    }

    public static Color lowStockColor(int qty) {
        return qty <= AppConfig.LOW_STOCK_THRESHOLD ? DANGER : SUCCESS_DARK;
    }

    public static class GradientPanel extends JPanel {
        public GradientPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, PANEL_TOP, getWidth(), getHeight(), PANEL_BOTTOM));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            g2.setColor(new Color(255, 255, 255, 55));
            g2.fill(new Ellipse2D.Double(-80, getHeight() - 150, 260, 180));
            g2.fill(new Ellipse2D.Double(getWidth() - 240, -50, 240, 180));
            g2.setColor(BORDER);
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class RoundedPanel extends JPanel {
        public RoundedPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 230));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedInputWrapper extends JComponent {
        private final JComponent child;

        private RoundedInputWrapper(JComponent child) {
            this.child = child;
            setLayout(new BorderLayout());
            add(child);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class GradientButton extends JButton {
        private final Color top;
        private final Color bottom;

        public GradientButton(String text, Color top, Color bottom) {
            super(text);
            this.top = top;
            this.bottom = bottom;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(10, 18, 10, 18));
            setForeground(Color.WHITE);
            setFont(font(15, true));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(new Color(0, 0, 0, 25));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class SidebarButton extends JButton {
        public SidebarButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setHorizontalAlignment(LEFT);
            setForeground(TEXT_DARK);
            setFont(font(15, true));
            setBorder(new EmptyBorder(12, 14, 12, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? new Color(255, 255, 255, 210) : new Color(255, 255, 255, 150));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
