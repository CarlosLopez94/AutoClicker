package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;

public class AutoClickerController implements ActionListener, ChangeListener, ItemListener, AWTEventListener {
    public static final String NEW_POSITION_BUTTON = "NEW_POSITION_BUTTON";

    private Semaphore autoClickerSemaphore;

    private final AutoClicker autoClicker;
    private final AutoClickerView autoClickerView;

    private boolean waitingNewPosition;

    public AutoClickerController(AutoClicker autoClicker, AutoClickerView autoClickerView) {
        this.autoClicker = autoClicker;
        this.autoClickerView = autoClickerView;

        waitingNewPosition = false;
    }

    public void start() {
        // Default values
        Integer currentSleepTimeMillis = autoClicker.getCurrentSleepTimeMillis();
        boolean autoClickEnable = autoClicker.isEnabled();
        Point currentMouseClickPosition  = autoClicker.getMouseClickPosition();

        autoClickerView.initDefaultValues(currentSleepTimeMillis, autoClickEnable, currentMouseClickPosition);

        // Bind controller to view
        autoClickerView.addListeners(this, this, this);

        // Start autoClicker
        autoClickerSemaphore = autoClicker.getSemaphore();
        autoClicker.start();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
       if(e.getSource() instanceof JSpinner){
            // Get value
            Integer newValue = autoClickerView.getCurrentSleepTime();
            // Update model
            autoClicker.setCurrentSleepTimeMillis(newValue);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Check new state is selected
        autoClicker.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        if (waitingNewPosition && event.paramString().contains("FOCUS_LOST")) {
            waitingNewPosition = false;

            Point newMousePosition = MouseInfo.getPointerInfo().getLocation();
            autoClicker.setMouseClickPosition(newMousePosition);
            autoClickerView.updatePosition(newMousePosition);

            autoClicker.setEnabled(true);
            autoClickerView.setCheckboxSelectedState(true, true);

            autoClickerSemaphore.release();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (NEW_POSITION_BUTTON.equals(e.getActionCommand())) {
            autoClicker.setEnabled(false);

            autoClickerView.setCheckboxSelectedState(false, false);
            waitingNewPosition = true;
        }
    }
}
