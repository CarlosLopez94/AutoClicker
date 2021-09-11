package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AutoClickerView extends JPanel {
    private JButton button;
    public AutoClickerView(){
        button = new JButton("Pulsame");
        add(button);
    }

    public void addActionListener(ActionListener controller){
        button.setActionCommand("BOTON");
        button.addActionListener(controller);
    }

}
