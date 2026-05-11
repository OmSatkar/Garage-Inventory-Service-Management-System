package com.garageims.ui;

import javax.swing.SwingUtilities;

import com.garageims.util.UIUtils;

public class GarageApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIUtils.setModernDefaults();
            new HomeFrame().setVisible(true);
        });
    }
}