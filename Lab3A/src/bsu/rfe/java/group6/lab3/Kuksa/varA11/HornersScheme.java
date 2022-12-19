package bsu.rfe.java.group6.lab3.Kuksa.varA11;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class HornersScheme extends JFrame {
    private static final int WIDTH = 700, HEIGHT = 500;
    private Double[] coefficients; // Массив коэффициентов многочлена
    private JFileChooser fileChooser = null;
    private JMenuItem aboutMenuItem;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem colorClosestToPrimesMenuItem, colorClosestToSearchMenuItem;
    private JTextField textFieldFrom, textFieldTo, textFieldStep;
    private Box hBoxResult;
    private HornerTableCellRenderer renderer = new HornerTableCellRenderer();
    public static HornerTableModel data;

    public HornersScheme(Double[] coefficients) {
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        JMenuBar menuBar = new JMenuBar(); setJMenuBar(menuBar);
        JMenu aboutMenu = new JMenu("Справка"); menuBar.add(aboutMenu);
        JMenu fileMenu = new JMenu("Файл"); menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица"); menuBar.add(tableMenu);

        // это Справка -> Автор
        Action aboutAction = new AbstractAction("Автор") {
            public void actionPerformed(ActionEvent event) {
                String message = "Кукса Валерий \n6 группа";
                JOptionPane.showMessageDialog(null, message, "Автор", JOptionPane.PLAIN_MESSAGE);
            }
        };
        aboutMenuItem = aboutMenu.add(aboutAction);

        // это сохранение в txt формат
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(HornersScheme.this) == JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };

        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);

        // это вероятнее всего закраска простых
        JCheckBoxMenuItem colorClosestToPrimesCB = new JCheckBoxMenuItem("Простые");
        Action colorClosestToPrimesAction = new AbstractAction() {
            public void actionPerformed(ActionEvent event){
                renderer.setNeedle(colorClosestToPrimesCB.isSelected());
                getContentPane().repaint(); // Обновить таблицу
            }
        };

        // это вероятнее всего закраска поиска
        JCheckBoxMenuItem colorClosestToSearch = new JCheckBoxMenuItem("Поиск");
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
                // Запросить пользователя ввести искомую строку
                String value = JOptionPane.showInputDialog(HornersScheme.this,
                        "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value); // Установить введенное значение в качестве иголки
                getContentPane().repaint(); // Обновить таблицу
            }
        };
        colorClosestToPrimesCB.addActionListener(colorClosestToPrimesAction);
        colorClosestToSearch.addActionListener(searchValueAction);

        colorClosestToPrimesMenuItem = tableMenu.add(colorClosestToPrimesCB);
        colorClosestToSearchMenuItem = tableMenu.add(colorClosestToSearch);
        colorClosestToSearchMenuItem.setEnabled(false);
        colorClosestToPrimesMenuItem.setEnabled(false);

        // это описание коробки ввода
        JLabel labelFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize()); // это сжатие по вертикали
        JLabel labelTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize()); // это сжатие по вертикали
        JLabel labelStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize()); // это сжатие по вертикали

        // это вставка элементов КОРОБКИ ВВОДА в контейнер (коробку)
        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        //
        hboxRange.add(Box.createHorizontalGlue()); // это клей левой стороны коробки
        hboxRange.add(labelFrom); hboxRange.add(Box.createHorizontalStrut(10)); // распорка шириной 10
        hboxRange.add(textFieldFrom); hboxRange.add(Box.createHorizontalStrut(10)); // распорка шириной 10
        hboxRange.add(labelTo); hboxRange.add(Box.createHorizontalStrut(10)); // распорка шириной 10
        hboxRange.add(textFieldTo); hboxRange.add(Box.createHorizontalStrut(10)); // распорка шириной 10
        hboxRange.add(labelStep); hboxRange.add(Box.createHorizontalStrut(10)); // распорка шириной 10
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue()); // это клей правой стороны коробки
        //
        hboxRange.setPreferredSize(new Dimension((int) hboxRange.getMaximumSize().getWidth(),
                (int) (hboxRange.getMinimumSize().getHeight()) * 2)); // это толщина коробки


        JButton buttonCalc = new JButton("Вычислить"); // рождение кнопки "Вычислить"
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    // перевод текста String поля ввода в double
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new HornerTableModel(from, to, step, HornersScheme.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(25); // ширина столбца таблицы
                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    saveToTextMenuItem.setEnabled(true);
                    colorClosestToPrimesMenuItem.setEnabled(true);
                    colorClosestToSearchMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            HornersScheme.this, "Ошибка в формате записи числа с плавающей точкой",
                            "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE); // ввод не числа
                }
            }
        });

        JButton buttonReset = new JButton("Очистить поля"); // рождение кнопки "Очистить поля"
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0"); textFieldTo.setText("1.0"); textFieldStep.setText("0.1");                      // начальные параметры
                hBoxResult.removeAll();
                hBoxResult.add(new JPanel()); // без этой строки очищение не происходит
                saveToTextMenuItem.setEnabled(false); // закрываем возможность нажать кнопку
                colorClosestToPrimesMenuItem.setEnabled(false); // закрываем возможность нажать чекбокс
                colorClosestToSearchMenuItem.setEnabled(false); // закрываем возможность нажать чекбокс
                getContentPane().validate(); // без этой строки очищение нке происходит
            }
        });

        Box hboxButtons = Box.createHorizontalBox(); // кнопки выстраиваются в ряд, а не столбец, как с VerticalBox
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        // add это добавление клея и кнопок
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30)); // расстояние между кнопками 30
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension((int) hboxButtons.getMaximumSize().getWidth(), (int) hboxButtons.getMinimumSize().getHeight() * 2));
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());

        getContentPane().add(hboxRange, BorderLayout.NORTH); // эта штука и добавляет наше поля ввода вверх
        getContentPane().add(hboxButtons, BorderLayout.SOUTH); // а эта штука добавляет наши кнопки вниз
        getContentPane().add(hBoxResult, BorderLayout.CENTER); // а эта штука соответсвенно ставит результат в центр
    }

    protected void saveToTextFile(File selectedFile) {
        try { // это по факту просто String message и всё
            PrintStream out = new PrintStream(selectedFile);
            out.print("y = ");
            for (int i = coefficients.length - 1; i > 0; i--) {
                out.print(coefficients[i].toString() + "x^" + i + " + ");
            }
            out.println(coefficients[0]);
            out.println(""); // эквивалент \n
            out.println("[" + data.getFrom() + " ; " + data.getTo() + "]  шаг: " + data.getStep());
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("x: " + data.getValueAt(i, 0) + " y: " + data.getValueAt(i, 1));
            }
            out.close();
        } catch (FileNotFoundException ignored) {}
    }
}