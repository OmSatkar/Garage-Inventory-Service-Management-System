package com.garageims.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.garageims.util.UIUtils;

public class SimpleBarChartPanel extends JPanel {
    private Map<String, Integer> data = new LinkedHashMap<>();

    public SimpleBarChartPanel() {
        setOpaque(false);
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int left = 45;
        int right = 20;
        int top = 25;
        int bottom = 40;
        int chartH = h - top - bottom;
        int baseline = h - bottom;

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(0, 0, w, h, 18, 18);
        g2.setColor(UIUtils.BORDER);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

        g2.setColor(UIUtils.TEXT_DARK);
        g2.setFont(UIUtils.font(18, true));
        g2.drawString("Weekly Snapshot", 18, 24);

        int max = Math.max(1, data.values().stream().max(Integer::compareTo).orElse(1));
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i <= 4; i++) {
            int y = baseline - (chartH * i / 4);
            g2.setColor(new Color(214, 223, 233));
            g2.drawLine(left, y, w - right, y);
            g2.setColor(UIUtils.TEXT_MUTED);
            g2.setFont(UIUtils.font(12, false));
            g2.drawString(String.valueOf(max * i / 4), 10, y + 5);
        }

        int count = Math.max(1, data.size());
        int slot = (w - left - right) / count;
        int barW = Math.max(24, slot / 2);
        Color[] palette = { UIUtils.PRIMARY, UIUtils.DANGER, UIUtils.WARNING, UIUtils.SUCCESS_DARK, UIUtils.PURPLE };
        FontMetrics fm = g2.getFontMetrics();
        int idx = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int x = left + idx * slot + (slot - barW) / 2;
            int barH = (int) ((entry.getValue() / (double) max) * (chartH - 18));
            int y = baseline - barH;
            g2.setColor(palette[idx % palette.length]);
            g2.fillRoundRect(x, y, barW, barH, 14, 14);
            g2.setColor(UIUtils.TEXT_DARK);
            String valueText = String.valueOf(entry.getValue());
            g2.drawString(valueText, x + (barW - fm.stringWidth(valueText)) / 2, y - 6);
            String label = entry.getKey();
            g2.drawString(label, x + (barW - fm.stringWidth(label)) / 2, baseline + 18);
            idx++;
        }
        g2.dispose();
    }
}
