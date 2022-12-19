import javax.swing.*;
import bsu.rfe.java.group6.lab3.Kuksa.varA11.*;

public class Main {
    public static void main(String[] args) {
        Double[] a = {5., 5., 3., 9., 1., 1., 8.};
        HornersScheme frame = new HornersScheme(a);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}