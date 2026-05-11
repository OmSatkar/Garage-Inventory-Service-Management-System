package com.garageims.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.garageims.util.UIUtils;

public class StatsCard extends JPanel {
    private final JLabel titleLabel;
    private final JLabel valueLabel;
    private final Color c1;
    private final Color c2;

    public StatsCard(String title, String value, Color c1, Color c2) {
        this.c1 = c1;
        this.c2 = c2;
        setOpaque(false);
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(12, 14, 12, 14));

        titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.font(15, true));
        titleLabel.setForeground(Color.WHITE);

        valueLabel = new JLabel(value, SwingConstants.RIGHT);
        valueLabel.setFont(UIUtils.font(28, true));
        valueLabel.setForeground(Color.WHITE);

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
        g2.setColor(new Color(255, 255, 255, 45));
        g2.fillOval(-18, getHeight() - 56, 70, 70);
        g2.dispose();
        super.paintComponent(g);
    }
}
