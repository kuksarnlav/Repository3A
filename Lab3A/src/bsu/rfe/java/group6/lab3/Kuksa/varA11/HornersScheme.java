package bsu.rfe.java.group6.lab3.Kuksa.varA11;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class HornersScheme extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    // Массив коэффициентов многочлена
    private Double[] coefficients;
    private JFileChooser fileChooser = null;
    private JMenuItem aboutMenuItem;
    private JLabel aboutPhotoLabel;
    private JLabel aboutNameTF;
    private JButton githubLinkButton;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToCSVMenuItem;
    private JMenuItem colorClosestToPrimesMenuItem;

    private JMenuItem colorClosestToSearchMenuItem;
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    private HornerTableCellRenderer renderer = new HornerTableCellRenderer();
    private HornerTableModel data;

    public HornersScheme(Double[] coefficients) {
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu aboutMenu = new JMenu("Справка");
        menuBar.add(aboutMenu);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);

        Action aboutAction = new AbstractAction("Автор") {
            public void actionPerformed(ActionEvent event) {
                JDialog dialog = new JDialog(HornersScheme.this, "Автор", true);
                dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setSize(515, 560);

                aboutNameTF = new JLabel("Кузнецов Александр");
                githubLinkButton = new JButton();
                aboutPhotoLabel = new JLabel();
                aboutPhotoLabel.setIcon(getImageFromGithubProfile("https://avatars.githubusercontent.com/u/90519481?v=4"));

                githubLinkButton.setText("<HTML><FONT color=\"#000099\"><U>https://github.com/Sanb4ik</U></FONT></HTML>");
                githubLinkButton.setHorizontalAlignment(SwingConstants.LEFT);
                githubLinkButton.setBorderPainted(false);
                githubLinkButton.setOpaque(false);
                githubLinkButton.setBackground(Color.WHITE);
                githubLinkButton.setToolTipText("github.com/Sanb4ik");
                githubLinkButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(new URI("https://github.com/Sanb4ik"));
                            } catch (IOException e) { /* TODO: error handling */ } catch (URISyntaxException ignored) {
                            }
                        }
                    }
                });

                Box box = Box.createVerticalBox();
                box.add(aboutPhotoLabel);
                box.add(aboutNameTF);
                box.add(githubLinkButton);

                Box hbox = Box.createHorizontalBox();
                hbox.add(Box.createHorizontalStrut(20));
                hbox.add(box);

                dialog.getContentPane().add(hbox);

                dialog.setVisible(true);
            }
        };

        aboutMenuItem = aboutMenu.add(aboutAction);

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
        Action saveToCSV = new AbstractAction("Сохранить в CSV") {

            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(HornersScheme.this) == JFileChooser.APPROVE_OPTION)
                    saveToCSV(fileChooser.getSelectedFile());
            }
        };
        saveToCSVMenuItem = fileMenu.add(saveToCSV);
        saveToCSVMenuItem.setEnabled(false);

        JCheckBoxMenuItem colorClosestToPrimesCB = new JCheckBoxMenuItem("Простые");
        Action colorClosestToPrimesAction = new AbstractAction() {
            public void actionPerformed(ActionEvent event){
                renderer.setNeedle(colorClosestToPrimesCB.isSelected());
                getContentPane().repaint();
            }
        };

        JCheckBoxMenuItem colorClosestToSearch = new JCheckBoxMenuItem("Поиск");
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
                // Запросить пользователя ввести искомую строку
                String value =
                        JOptionPane.showInputDialog(HornersScheme.this, "Введите значение для поиска",
                                "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                // Установить введенное значение в качестве иголки
                renderer.setNeedle(value);
                // Обновить таблицу
                getContentPane().repaint();
            }
        };
        colorClosestToPrimesCB.addActionListener(colorClosestToPrimesAction);
        colorClosestToSearch.addActionListener(searchValueAction);


        colorClosestToPrimesMenuItem = tableMenu.add(colorClosestToPrimesCB);
        colorClosestToSearchMenuItem = tableMenu.add(colorClosestToSearch);
        colorClosestToSearchMenuItem.setEnabled(false);
        colorClosestToPrimesMenuItem.setEnabled(false);

        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.setPreferredSize(new Dimension((int) hboxRange.getMaximumSize().getWidth(), (int) (hboxRange.getMinimumSize().getHeight()) * 2));
        getContentPane().add(hboxRange, BorderLayout.SOUTH);
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new HornerTableModel(from, to, step, HornersScheme.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);
                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    saveToTextMenuItem.setEnabled(true);
                    saveToCSVMenuItem.setEnabled(true);
                    colorClosestToPrimesMenuItem.setEnabled(true);
                    colorClosestToSearchMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HornersScheme.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();
                hBoxResult.add(new JPanel());
                saveToTextMenuItem.setEnabled(false);
                saveToCSVMenuItem.setEnabled(false);
                colorClosestToPrimesMenuItem.setEnabled(false);
                getContentPane().validate();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension((int) hboxButtons.getMaximumSize().getWidth(), (int) hboxButtons.getMinimumSize().getHeight() * 2));
        getContentPane().add(hboxButtons, BorderLayout.NORTH);
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    private ImageIcon getImageFromGithubProfile(String strUrl) {
        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ImageIcon(image);
    }

    protected void saveToTextFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);
            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");

            for (int i = coefficients.length - 1; i > 0; i--) {
                out.print(coefficients[i].toString() + "*X^" + i + " ");
            }
            out.println(coefficients[0]);

            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i, 0) + " равно " + data.getValueAt(i, 1) + ". С функцией Math.pow() равно " + data.getValueAt(i, 2) + ". Разница между ними " + data.getValueAt(i, 3));
            }
            out.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    protected void saveToCSV(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);

            for (int i = 0; i < 4; i++) {
                out.print(data.getColumnName(i) + ";");
            }
            out.println(";");
//            CSVWriter writer = new CSVWriter(out);
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println(data.getValueAt(i, 0) + ";" + data.getValueAt(i, 1) + ";" + data.getValueAt(i, 2) + ";" + data.getValueAt(i, 3));
            }
            out.close();
        } catch
        (FileNotFoundException ignored) {
        }
    }


}