package com.company;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

public class AutoClickerView extends JPanel {

    private final JSpinner sleepTimeSpinner;
    private final JCheckBox checkBoxClickerEnable;
    private final JButton newPositionButton;
    private final JLabel newPositionLabel;

    public AutoClickerView() {
        sleepTimeSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 5000, 100));
        checkBoxClickerEnable=new JCheckBox("AutoClicker Activado");
        newPositionButton = new JButton("Cambiar posición de autoclick");
        newPositionLabel = new JLabel("", SwingConstants.CENTER);

        JPanel centerGrid = new JPanel(new GridLayout(0, 2));
        // First row on centerGrid
        centerGrid.add(new JLabel("Tiempo de espera entre clicks: ", SwingConstants.CENTER));
        centerGrid.add(sleepTimeSpinner);

        // Seconds row on centerGrid
        centerGrid.add(new JLabel());
        centerGrid.add(checkBoxClickerEnable);

        // Thirds row on centerGrid
        centerGrid.add(newPositionButton);
        centerGrid.add(newPositionLabel);

        // BorderLayour
        BorderLayout mainBorderLayout = new BorderLayout();
        setLayout(mainBorderLayout);
        add(new JLabel("AutoClicker Config", SwingConstants.CENTER), BorderLayout.NORTH);
        add(centerGrid, BorderLayout.CENTER);

    }

    public void initDefaultValues(Integer defaultSleepTime, boolean autoClickEnable, Point currentMouseClickPosition){
        sleepTimeSpinner.setValue(defaultSleepTime);
        checkBoxClickerEnable.setSelected(autoClickEnable);

        updatePosition(currentMouseClickPosition);
    }

    public Integer getCurrentSleepTime(){
        return (Integer) sleepTimeSpinner.getValue();
    }

    public void setCheckboxSelectedState(boolean newSelectedState, boolean newAvailavilityState){
        checkBoxClickerEnable.setSelected(newSelectedState);
        checkBoxClickerEnable.setEnabled(newAvailavilityState);

    }

    public void updatePosition(Point newPoint){
        String newText = String.format("(%d,%d)", newPoint.x, newPoint.y);
        newPositionLabel.setText(newText);
    }

    public void addListeners(ActionListener actionController, ChangeListener changeController, ItemListener itemController) {
        sleepTimeSpinner.addChangeListener(changeController);
        checkBoxClickerEnable.addItemListener(itemController);

        newPositionButton.setActionCommand(AutoClickerController.NEW_POSITION_BUTTON);
        newPositionButton.addActionListener(actionController);
    }

}
