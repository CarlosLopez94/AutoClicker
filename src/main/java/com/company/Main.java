package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create model
        AutoClicker autoClickerThread = null;
        try {
            autoClickerThread = new AutoClicker();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        // Create view
        AutoClickerView view = new AutoClickerView();


        // Create controller
        AutoClickerController controller = new AutoClickerController(autoClickerThread, view);

        // Start controller
        controller.start();

        // Init Window
        Toolkit.getDefaultToolkit().addAWTEventListener(
                controller, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);

        JFrame autoClickerFrame = new JFrame("AutoClicker");
        autoClickerFrame.add(view);

        autoClickerFrame.setVisible(true);
        autoClickerFrame.setSize(new Dimension(600, 200));
        autoClickerFrame.setLocationRelativeTo(null);
        autoClickerFrame.setFocusable(true);
        autoClickerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        autoClickerFrame.setVisible(true);
    }

}
