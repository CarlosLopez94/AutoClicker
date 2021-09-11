package com.company;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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

        // Prepare Keyboard Binding
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(controller);
        // Clear previous logging configurations.
        LogManager.getLogManager().reset();

        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

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
