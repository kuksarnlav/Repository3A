package bsu.rfe.java.group6.lab3.Kuksa.varA11;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class SimpleGUI extends JFrame {

    private JButton button = new JButton("Calculate");
    private JButton buttonMC = new JButton("MC");
    private JButton buttonMPlus = new JButton("M+");
    private JTextField inputX = new JTextField("",1);
    private JTextField inputY = new JTextField("",1);
    private JTextField inputZ = new JTextField("",1);
    private JLabel labelX = new JLabel(" x");
    private JLabel labelY = new JLabel(" y");
    private JLabel labelZ = new JLabel(" z");
    private JRadioButton radio1 = new JRadioButton("Formula №1");
    private JRadioButton radio2 = new JRadioButton("Formula №2");

    eHandler handler = new eHandler();

    public SimpleGUI(){
        super("Lab 2A");
        this.setBounds(100, 100, 250, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(6,2,2,2));

        container.add(labelX);
        container.add(inputX);
        container.add(labelY);
        container.add(inputY);
        container.add(labelZ);
        container.add(inputZ);

        ButtonGroup group = new ButtonGroup();
        group.add(radio1);
        group.add(radio2);
        container.add(radio1);
        radio1.setSelected(true);
        container.add(radio2);

        container.add(buttonMC);
        container.add(buttonMPlus);
        container.add(button);

        buttonMC.addActionListener(handler);
        buttonMPlus.addActionListener(handler);
        button.addActionListener(handler);
    }

    public class eHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button){
            }
            if (e.getSource() == buttonMC){
            }
            if (e.getSource() == buttonMPlus){
            }
        }
    }
}