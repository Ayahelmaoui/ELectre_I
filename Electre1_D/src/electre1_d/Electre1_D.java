package electre1_d;

import electre1_d.NodeGraph;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Electre1_D {

    static String defaultPoisValue = "1,2,0,1,1,1,1,1,1,1";
    static String defaultMPValue = "13.3,8,350,245,3.73,3.84,15.41,0,0,3;19.2,"
            + "8,400,175,3.08,3.845,17.05,0,0,3;30.4,4,95.1,113,3.77,1.513,16.9,1,1,5";
    static String defauld = "0.7";
    static String defaulc = "0.3";
    static double[][] data;
    static double[][] Conc;
    static double[][] Disc;
    static int[][] Surc;
    static double[] pois;
    static int[][] connections;
    static Color lightBlue = new Color(173, 216, 230);
    static Color SlateBlue = new Color(106, 90, 250);
    static Color Plum = new Color(221, 160, 221);

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JButton ouvrirButton = new JButton("Ouvrir");
        JButton nouveauButton = new JButton("Nouveau");
        JButton enregistrerButton = new JButton("Enregistrer");
        JButton quitterButton = new JButton("Quitter");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(ouvrirButton);
        panel.add(nouveauButton);
        panel.add(enregistrerButton);
        panel.add(quitterButton);

        frame.add(panel);
        frame.setVisible(true);

        ouvrirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    data = read.readCSVFile(fileChooser.getSelectedFile().getAbsolutePath());

                    Object[][] tableData = read.convertToObjects(data);
                    String[] columnNames = new String[data[0].length];
                    for (int j = 0; j < data[0].length; j++) {
                        columnNames[j] = "Crit " + (j + 1);
                    }

                    JTable table = new JTable(tableData, columnNames);
                    table.setBackground(Color.LIGHT_GRAY);
                    table.setForeground(Color.WHITE);

                    JScrollPane scrollPane = new JScrollPane(table);

                    JLabel poisLabel = new JLabel("Pois: ");
                    JTextField poisField = new JTextField(defaultPoisValue,10);

                    JPanel dataPanel = new JPanel();
                    dataPanel.setLayout(new BorderLayout());
                    dataPanel.add(scrollPane, BorderLayout.CENTER);

                    JLabel cLabel = new JLabel("Seuil c: ");
                    JTextField cField = new JTextField(defaulc,3);
                    JLabel dLabel = new JLabel("Seuil d: ");
                    JTextField dField = new JTextField(defauld,3);

                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new FlowLayout());
                    inputPanel.add(poisLabel);
                    inputPanel.add(poisField);
                    inputPanel.add(cLabel);
                    inputPanel.add(cField);
                    inputPanel.add(dLabel);
                    inputPanel.add(dField);

                    JPanel mainPanel = new JPanel();
                    mainPanel.setLayout(new BorderLayout());
                    mainPanel.setBackground(Color.LIGHT_GRAY);
                    mainPanel.add(dataPanel, BorderLayout.CENTER);
                    mainPanel.add(inputPanel, BorderLayout.SOUTH);

                    JButton traitementButton = new JButton("Traitement");
                    traitementButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Access the "pois" value
                            String poisValue = poisField.getText();
                            double c = Double.parseDouble(cField.getText());
                            double d = Double.parseDouble(dField.getText());
                            pois = read.parseDoubleArray(poisValue); //1,2,0,1,1,1,1,1,1,1
                            System.out.println("Traitement button clicked!");
                            System.out.println("Pois value: " + poisValue);
                            Conc = Agregation.MatriceConcordance(pois, data);
                            Disc = Agregation.MatriceDiscordonce(pois, data);
                            Surc = Agregation.sommet_sommet(Conc, Disc, c, d);
                            // Create JTables for each matrix
                            // Create JTables for each matrix
                            // Convert matrices to Object[][] type
                            Object[][] concData = read.convertToObjects(Conc);
                            Object[][] discData = read.convertToObjects(Disc);
                            Object[][] surcData = read.convertIntToObjects(Surc);

                            //Column names 
                            String[] ResColumnNames = new String[data[0].length];
                            for (int j = 0; j < data[0].length; j++) {
                                ResColumnNames[j] = "Proj " + (j + 1);
                            }
                            // Create JTables for each matrix
                            JTable concTable = new JTable(concData, ResColumnNames);
                            concTable.setBackground(Plum);
                            concTable.setForeground(Color.WHITE);
                            JScrollPane concScrollPane = new JScrollPane(concTable);
                            JTable discTable = new JTable(discData, ResColumnNames);
                            discTable.setBackground(SlateBlue);
                            discTable.setForeground(Color.WHITE);
                            JScrollPane discScrollPane = new JScrollPane(discTable);

                            JTable surcTable = new JTable(surcData, ResColumnNames);
                            surcTable.setBackground(lightBlue);
                            surcTable.setForeground(Color.WHITE);
                            JScrollPane surcScrollPane = new JScrollPane(surcTable);

                            // Create a JTabbedPane
                            JTabbedPane tabbedPane = new JTabbedPane();
                            tabbedPane.addTab("Conc", concScrollPane);
                            tabbedPane.addTab("Disc", discScrollPane);
                            tabbedPane.addTab("Surc", surcScrollPane);

                            JFrame frame = new JFrame("Result Tables");
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.add(tabbedPane);
                            frame.pack();
                            frame.setVisible(true);
                            JButton affichageButton = new JButton("Affichage");
                            affichageButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    ArrayList<Integer> index = new ArrayList<>();
                                    System.out.println("Affichage button clicked!");
                                    JFrame resultFrame = new JFrame("Result Frame");
                                    resultFrame.setSize(300, 200);

                                    // Create a label for the result
                                    JLabel resultLabel = new JLabel(Agregation.Exploitation(Surc));
                                    resultLabel.setHorizontalAlignment(JLabel.CENTER);

                                    // Set the layout manager for the result frame
                                    resultFrame.setLayout(new BorderLayout());

                                    // Add the result label to the result frame
                                    resultFrame.add(resultLabel, BorderLayout.CENTER);

                                    // Set the result frame visible
                                    resultFrame.setVisible(true);

                                }
                            });
                        }
                    });

                    JFrame frame = new JFrame("Data Table");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.add(mainPanel);
                    frame.add(traitementButton, BorderLayout.SOUTH);
                    frame.pack();
                    frame.setVisible(true);
                }
            }
        });

        nouveauButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JTextField mpField = new JTextField(defaultMPValue, 20);
                JTextField poiseField = new JTextField(defaultPoisValue, 10);
                JTextField cField = new JTextField(defauld, 3);
                JTextField dField = new JTextField(defaulc, 3);

                JPanel panel = new JPanel();
                panel.add(new JLabel("MP:"));
                panel.add(mpField);
                panel.add(new JLabel("Pois:"));
                panel.add(poiseField);
                panel.add(new JLabel("Seuil c: "));
                panel.add(cField);
                panel.add(new JLabel("Seuil d: "));
                panel.add(dField);

                JButton traitementButton = new JButton("Traitement");
                traitementButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//13.3,8,350,245,3.73,3.84,15.41,0,0,3;19.2,8,400,175,3.08,3.845,17.05,0,0,3;30.4,4,95.1,113,3.77,1.513,16.9,1,1,5
                        String poisValue = poiseField.getText();
                        double c = Double.parseDouble(cField.getText());
                        double d = Double.parseDouble(dField.getText());
                        pois = read.parseDoubleArray(poisValue); //1,2,0,1,1,1,1,1,1,1
                        data = read.parseDoubleMatrix(mpField.getText());

                        Object[][] tableData = read.convertToObjects(data);

                        String[] datacolumnNames = new String[data[0].length];
                        for (int j = 0; j < data[0].length; j++) {
                            datacolumnNames[j] = "Crit " + (j + 1);
                        }

                        // Access the "pois" value
                        System.out.println("Traitement button clicked!");
                        System.out.println("Pois value: " + poisValue);
                        Conc = Agregation.MatriceConcordance(pois, data);
                        Disc = Agregation.MatriceDiscordonce(pois, data);
                        Surc = Agregation.sommet_sommet(Conc, Disc, c, d);
                        // Create JTables for each matrix
                        // Create JTables for each matrix
                        // Convert matrices to Object[][] type
                        Object[][] concData = read.convertToObjects(Conc);
                        Object[][] discData = read.convertToObjects(Disc);
                        Object[][] surcData = read.convertIntToObjects(Surc);

                        // Create JTables for each matrix
                        JTable table = new JTable(tableData, datacolumnNames);
                        table.setBackground(Color.LIGHT_GRAY);
                        table.setForeground(Color.WHITE);
                        JScrollPane MpscrollPane = new JScrollPane(table);

                        String[] resultcolumnNames = new String[Conc[0].length];
                        for (int j = 0; j < Conc[0].length; j++) {
                            resultcolumnNames[j] = "Proj " + (j + 1);
                        }
                        JTable concTable = new JTable(concData, resultcolumnNames);
                        concTable.setBackground(Plum);
                        concTable.setForeground(Color.WHITE);
                        JScrollPane concScrollPane = new JScrollPane(concTable);
                        JTable discTable = new JTable(discData, resultcolumnNames);
                        discTable.setBackground(SlateBlue);
                        discTable.setForeground(Color.WHITE);
                        JScrollPane discScrollPane = new JScrollPane(discTable);

                        JTable surcTable = new JTable(surcData, resultcolumnNames);
                        surcTable.setBackground(lightBlue);
                        surcTable.setForeground(Color.WHITE);
                        JScrollPane surcScrollPane = new JScrollPane(surcTable);

                        // Create a JTabbedPane
                        JTabbedPane tabbedPane = new JTabbedPane();
                        tabbedPane.addTab("MP", MpscrollPane);
                        tabbedPane.addTab("Conc", concScrollPane);
                        tabbedPane.addTab("Disc", discScrollPane);
                        tabbedPane.addTab("Surc", surcScrollPane);

                        JButton affichageButton = new JButton("Affichage");
                        affichageButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ArrayList<Integer> index = new ArrayList<>();
                                System.out.println("Affichage button clicked!");
                                JFrame resultFrame = new JFrame("Result Frame");
                                resultFrame.setSize(300, 200);

                                // Create a label for the result
                                JLabel resultLabel = new JLabel(Agregation.Exploitation(Surc));
                                resultLabel.setHorizontalAlignment(JLabel.CENTER);

                                // Set the layout manager for the result frame
                                resultFrame.setLayout(new BorderLayout());

                                // Add the result label to the result frame
                                resultFrame.add(resultLabel, BorderLayout.CENTER);

                                // Set the result frame visible
                                resultFrame.setVisible(true);

                            }
                        });

                        JFrame frame = new JFrame("Result Tables");
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.add(tabbedPane);
                        frame.add(affichageButton, BorderLayout.SOUTH);
                        frame.pack();
                        frame.setVisible(true);
                    }
                });
                JFrame frame = new JFrame("Data Table");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(panel);
                frame.add(traitementButton, BorderLayout.SOUTH);
                frame.pack();
                frame.setVisible(true);

            }
        });

        enregistrerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField mpField = new JTextField(defaultMPValue,20);
                JTextField poiseField = new JTextField(defaultPoisValue,10);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(2, 2));
                panel.add(new JLabel("mp:"));
                panel.add(mpField);
                panel.add(new JLabel("Pois:"));
                panel.add(poiseField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Enregistrer",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String mpValue = mpField.getText();
                    String poiseValue = poiseField.getText();
//0.2,0.1,0.3,0.2,0.2
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/moi/Documents/ElectreFile.txt"));
                        writer.write("mp: " + mpValue);
                        writer.newLine();
                        writer.write("Pois: " + poiseValue);
                        writer.close();
                        System.out.println("Données enregistrées dans le fichier ElectreFile.txt");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        quitterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

}
//                                for (int i = 0; i < Surc.length; i++) {
//                                    for (int j = 0; j < Surc[0].length; j++) {
//                                        if (Surc[i][j] == 1) {
//                                            index.add(i);
//                                            index.add(j);
//                                        }
//                                    }
//                                }
//                                System.out.println(index);
//                                connections = new int[index.size() / 2][index.size() / 2];
//                                for (int i = 0; i < connections.length; i++) {
//                                    for (int j = 0; j < connections[0].length; j++) {
//                                        connections[i][j] = index.get(i);
//                                    }
//                                }
//
//                                SwingUtilities.invokeLater(() -> {
//                                    NodeGraph example = new NodeGraph(data.length, 5, connections);
//                                    example.setVisible(true);
//                                });
