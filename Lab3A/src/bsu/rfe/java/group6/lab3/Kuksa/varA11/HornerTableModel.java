package bsu.rfe.java.group6.lab3.Kuksa.varA11;

import javax.swing.table.AbstractTableModel;

import java.awt.*;

import static bsu.rfe.java.group6.lab3.Kuksa.varA11.HornersScheme.data;

public class HornerTableModel extends AbstractTableModel {

    private final Double[] coefficients;
    private final Double from, to, step;

    public HornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    // гетеры
    public Double getFrom() { return from; }
    public Double getTo() { return to; }
    public Double getStep() { return step; }
    public int getColumnCount() { return 3; } // при подстановке 3, будет высвечиваться только 3 столбца
    public int getRowCount() { return (int)Math.ceil((to - from) / step) + 1; }

    public Object getValueAt(int row, int col) {
        double x = from + step * row;
        return switch (col) {
            case (0) -> x;
            case (1) -> calculateHorner(x);
            case (2) -> calculateDif(x); // ! заполнитель 3-го столбца
            default -> 0;
        };
    }

    public String getColumnName(int col) {
        return switch (col) {
            case 0 ->  "Значение X";
            case 1 -> "Значение многочлена";
            case 2 -> "Дробная часть";
            default -> "";
        };
    }

    public Class<?> getColumnClass(int col) { return Double.class; }

    private double calculateDif(double x){
        double cellValue;
        int integralPart; double difference;
        for (int i = 0; i < data.getRowCount(); i++) {
            cellValue = calculateHorner(x);
            integralPart = (int) cellValue;
            difference = cellValue - integralPart;
            if (difference == 0){
                return 1;
            }
        }
        return 0;
    }

/*    private boolean calculateDif(double x){
        double cellValue;
        int integralPart; double difference;
        for (int i = 0; i < data.getRowCount(); i++) {
            cellValue = calculateHorner(x);
            integralPart = (int) cellValue;
            difference = cellValue - integralPart;
            if (difference == 0){
                return true;
            }
        }
        return false;
    }*/

    private double calculateHorner(double x){
        Double b = coefficients[coefficients.length-1];
        for (int i = coefficients.length - 2; i >= 0; i--){ b = b * x + coefficients[i]; }
        return b;
    }
}

