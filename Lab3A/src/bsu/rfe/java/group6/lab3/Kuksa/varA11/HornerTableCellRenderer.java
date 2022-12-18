package bsu.rfe.java.group6.lab3.Kuksa.varA11;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class HornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private boolean needle = false;
    private char Sign;
    private String needle1 = " ";
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public HornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(10);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int col) {

        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);

        if (value instanceof Double) {
            double number = Double.parseDouble(formattedDouble);
            FlowLayout alignment;
            if (number > 0) {
                alignment = new FlowLayout(FlowLayout.RIGHT);
            }
            else
            if (number < 0){
                alignment = new FlowLayout(FlowLayout.LEFT);
            }
            else{ alignment = new FlowLayout(FlowLayout.CENTER); }
            panel.setLayout(alignment);
        }

        label.setText(formattedDouble);

        if (col==1 && needle1!=null && needle1.equals(formattedDouble)) { panel.setBackground(Color.RED);
        }
        else {
            panel.setBackground(Color.WHITE);
        }
        if (col==0 && needle1!=null && needle1.equals(formattedDouble)) {
            panel.setBackground(Color.RED);
        }

        if (col == 1 && needle ) {
            double actual_value = Double.parseDouble(formattedDouble);
            long closet_int_value =  Math.round(actual_value);
            if (Math.abs(actual_value - closet_int_value) < 0.1 &&
                    isPrime(closet_int_value)){
                panel.setBackground(Color.YELLOW);// если близко к простому то крашу
            }

        }

        return panel;
    }

    public void setNeedle(boolean needle) {
        this.needle = needle;
    }

    public void setNeedle(String needle) {
        this.needle1 = needle;
    }

    private boolean SameSign(String str) {
        char[] result = str.toCharArray();
        if(result[0] != '-' && Sign == '+') return false;
        else if(result[0] == '-' && Sign == '-') return false;
        else return true;
    }
    private boolean isPrime(long x) { // проверка на простые

        if (x == 0 || x == 1) {
            return false;
        }

        for (long i = 2; i <= (long) Math.floor(Math.sqrt(x)); i++) {
            if (x % i == 0)
                return false;
        }
        return true;
    }
}
