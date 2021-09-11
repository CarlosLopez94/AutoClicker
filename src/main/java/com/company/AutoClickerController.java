package com.company;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

public class AutoClickerController implements ActionListener, AWTEventListener {
    private Semaphore autoClickerSemaphore;

    private AutoClicker autoClicker;
    private AutoClickerView autoClickerView;

    public AutoClickerController(AutoClicker autoClicker, AutoClickerView autoClickerView) {
        this.autoClicker = autoClicker;
        this.autoClickerView = autoClickerView;
    }


    public void start() {
        // Bind controller to view
        autoClickerView.addActionListener(this);

        // Start autoClicker
        autoClickerSemaphore = autoClicker.getSemaphore();
        autoClicker.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "BOTON":
                autoClickerSemaphore.release();
                break;
        }
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
        System.out.println(event);
    }
}
