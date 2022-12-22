import javax.swing.*;
import bsu.rfe.java.group6.lab3.Kuksa.varA11.*;

public class Main {
    public static void main(String[] args) {
        Double[] a = {-10., 0., 2., 5., 4.};
        HornersScheme frame = new HornersScheme(a);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}