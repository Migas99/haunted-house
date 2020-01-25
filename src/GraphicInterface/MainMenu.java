package GraphicInterface;

import HauntedHouse.HauntedHouseGraph;
import HauntedHouse.MapManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainMenu extends JFrame {

    HauntedHouseGraph mapGraph;
    boolean soundEnable = true;
    MapManager mapManager = new MapManager();
    JButton playButton;
    JButton mapButton;
    JComboBox settings;
    JLabel background = new JLabel();
    int level;

    public MainMenu() {
        super("HauntedHouse");
        this.setLocationByPlatform(true);
        this.setSize(700, 700);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainMenu();
        pressPlayButtonMenu();

        this.setVisible(true);
    }

    public void mainMenu() {
        background.setLayout(new GridBagLayout());
        background.setIcon(new ImageIcon("resources/background.gif"));

        JLabel title = new JLabel();
        GridBagConstraints gbc = new GridBagConstraints();
        playButton = new JButton();
        mapButton = new JButton();
        settings = new JComboBox();

        playButton.setPreferredSize(new Dimension(300, 50));
        playButton.setText("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 30));

        mapButton.setPreferredSize(new Dimension(300, 50));
        mapButton.setText("MAP PREVIEW");
        mapButton.setFont(new Font("Arial", Font.BOLD, 30));

        title.setIcon(new ImageIcon("resources/title.png"));

        //TITLE
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 25, 150, 25);
        background.add(title, gbc);

        //START BUTTON
        gbc.gridy = 1;
        gbc.insets = new Insets(25, 25, 25, 25);
        background.add(playButton, gbc);

        //START BUTTON
        gbc.gridy = 2;
        background.add(mapButton, gbc);

        //PRESS BUTTON MAP PREVIEW
        pressMapButton();

        this.add(background);
    }

    public void pressMapButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        JLabel map = new JLabel();
        JComboBox mapsList = new JComboBox();
        JButton backButton = new JButton();
        JButton confirmButton = new JButton();
        mapButton.addActionListener((ActionEvent event) -> {
            map.setIcon(null);
            mapsList.removeAllItems();
            buttonsPanel.setPreferredSize(new Dimension(700, 80));
            map.setPreferredSize(new Dimension(700, 620));

            //MapsList
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(25, 15, 25, 0);
            mapsList.setPreferredSize(new Dimension(150, 30));
            Iterator iterator = mapManager.getMaps().iterator();
            while (iterator.hasNext()) {
                mapsList.addItem(iterator.next());
            }
            mapsList.setSelectedItem(null);
            buttonsPanel.add(mapsList, gbc);

            //Confirm button
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(25, 0, 25, 300);
            confirmButton.setText("Confirm");
            confirmButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(confirmButton, gbc);

            //Back button
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.insets = new Insets(25, 0, 25, 15);
            backButton.setText("Back");
            backButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(backButton, gbc);

            mainPanel.add(buttonsPanel, BorderLayout.PAGE_START);
            mainPanel.add(map, BorderLayout.PAGE_END);

            //UPDATE
            this.remove(this.background);
            this.add(mainPanel);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        confirmButton.addActionListener((ActionEvent event) -> {
            map.setIcon(new ImageIcon("resources/giratina.gif"));
        });

        backButton.addActionListener((ActionEvent event) -> {
            mainPanel.removeAll();
            this.remove(mainPanel);
            this.add(this.background);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });
    }

    public void pressPlayButtonMenu() {
        this.soundEnable = true;
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel mainPanel = new JLabel();
        mainPanel.setLayout(new BorderLayout());
        JComboBox mapsList = new JComboBox();
        mainPanel.setIcon(new ImageIcon("resources/background.gif"));
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        JTextField inputUsername = new JTextField();
        JButton backButton = new JButton();
        JButton soundButton = new JButton();
        JButton easyButton = new JButton();
        JButton normalButton = new JButton();
        JButton hardButton = new JButton();
        JButton simulationButton = new JButton();
        JButton startButton = new JButton();

        playButton.addActionListener((ActionEvent event) -> {
            inputUsername.setText("");
            this.mapGraph = null;
            mapsList.removeAllItems();
            mapsList.setSelectedItem(null);
            buttonsPanel.setPreferredSize(new Dimension(700, 80));
            infoPanel.setPreferredSize(new Dimension(700, 620));

            //DEFAULT DIFFICULTY
            normalButton.doClick();

            //////buttonsPanel
            //backbutton
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(25, 450, 25, 10);
            backButton.setText("BACK");
            backButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(backButton, gbc);

            //soundButton
            gbc.gridx = 1;
            gbc.insets = new Insets(25, 10, 25, 10);
            soundButton.setText("Sound: ON");
            this.soundEnable = true;
            soundButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(soundButton, gbc);

            //////infoPanel
            //Username title
            JLabel title = new JLabel();
            title.setText("USERNAME");
            title.setForeground(Color.white);
            title.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.insets = new Insets(10, 0, 0, 0);
            infoPanel.add(title, gbc);

            //Username input
            inputUsername.setPreferredSize(new Dimension(300, 30));
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            infoPanel.add(inputUsername, gbc);

            //MAP
            JLabel map = new JLabel();
            map.setText("SELECT MAP");
            map.setForeground(Color.white);
            map.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 3;
            gbc.insets = new Insets(50, 0, 0, 0);
            infoPanel.add(map, gbc);
            //MapsList
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.insets = new Insets(0, 0, 0, 0);
            mapsList.setPreferredSize(new Dimension(300, 30));
            Iterator iterator = mapManager.getMaps().iterator();
            while (iterator.hasNext()) {
                mapsList.addItem(iterator.next());
            }
            mapsList.setSelectedItem(null);
            infoPanel.add(mapsList, gbc);

            //Difficulty title
            JLabel difficulty = new JLabel();
            difficulty.setText("DIFFICULTY");
            difficulty.setForeground(Color.white);
            difficulty.setPreferredSize(new Dimension(100, 30));
            gbc.gridy = 4;
            gbc.insets = new Insets(50, 0, 0, 0);
            infoPanel.add(difficulty, gbc);

            //EASY BUTTON
            easyButton.setText("EASY");
            easyButton.setPreferredSize(new Dimension(100, 30));
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            infoPanel.add(easyButton, gbc);

            //NORMAL BUTTON
            normalButton.setText("NORMAL");
            normalButton.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            infoPanel.add(normalButton, gbc);

            //HARD BUTTON
            hardButton.setText("HARD");
            hardButton.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 2;
            gbc.insets = new Insets(0, 0, 0, 0);
            infoPanel.add(hardButton, gbc);

            //START BUTTON
            startButton.setText("START");
            startButton.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.insets = new Insets(100, 0, 100, 0);
            infoPanel.add(startButton, gbc);

            //SIMULATION BUTTON
            simulationButton.setText("SIMULATE");
            simulationButton.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 2;
            gbc.insets = new Insets(100, 0, 100, 0);
            infoPanel.add(simulationButton, gbc);

            mainPanel.add(buttonsPanel, BorderLayout.PAGE_START);
            mainPanel.add(infoPanel, BorderLayout.PAGE_END);

            //UPDATE
            this.remove(this.background);
            this.add(mainPanel);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        backButton.addActionListener((ActionEvent event) -> {
            mainPanel.removeAll();
            this.remove(mainPanel);
            this.add(this.background);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        soundButton.addActionListener((ActionEvent event) -> {
            if (soundEnable) {
                soundEnable = false;
                soundButton.setText("Sound: OFF");
            } else {
                soundEnable = true;
                soundButton.setText("Sound: ON");
            }
        });

        easyButton.addActionListener((ActionEvent event) -> {
            this.setLevel(1);
            easyButton.setBackground(new Color(204, 204, 204));
            normalButton.setBackground(new JButton().getBackground());
            hardButton.setBackground(new JButton().getBackground());
        });

        normalButton.addActionListener((ActionEvent event) -> {
            this.setLevel(2);
            normalButton.setBackground(new Color(204, 204, 204));
            easyButton.setBackground(new JButton().getBackground());
            hardButton.setBackground(new JButton().getBackground());
        });

        hardButton.addActionListener((ActionEvent event) -> {
            this.setLevel(3);
            hardButton.setBackground(new Color(204, 204, 204));
            easyButton.setBackground(new JButton().getBackground());
            normalButton.setBackground(new JButton().getBackground());
        });

        simulationButton.addActionListener((ActionEvent event) -> {

        });

        startButton.addActionListener((ActionEvent event) -> {
            if (mapsList.getSelectedItem() != null && inputUsername.getText() != null) {
                try {
                    this.mapGraph = this.mapManager.loadMapFromJSON((String) mapsList.getSelectedItem());
                    this.mapGraph.setLevel(this.level);
                    this.mapGraph.setPlayerName(inputUsername.getText());
                    this.mapGraph.setDifficulty();
                } catch (FileNotFoundException ex) {
                }
                JLabel map = null;
                 Thread wait = null;
                if (this.level == 1) {
                    map = this.showMap(mainPanel);
                    wait = new Thread(new Wait(map, 5000));
                } else if (this.level == 2){
                    map = this.showMap(mainPanel);
                    wait = new Thread(new Wait(map, 1000));
                } else if (this.level == 3){
                    wait = new Thread(new Wait(mainPanel, 0));
                }
                wait.start();
            }
        });
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void pressPlayButton(JLabel map) {
        GamePhase game = new GamePhase(this, this.background, this.soundEnable, this.mapGraph, map);
    }

    public JLabel showMap(JLabel mainPanel) {
        JLabel map = new JLabel();
        map.setIcon(new ImageIcon("resources/giratina.gif"));

        //UPDATE
        this.remove(mainPanel);
        this.add(map);
        SwingUtilities.updateComponentTreeUI(this);
        this.setVisible(true);

        return map;
    }

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
    }

    public class Wait implements Runnable {

        private JLabel label;
        private int time;

        public Wait(JLabel label, int time) {
            this.label = label;
            this.time = time;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
                pressPlayButton(this.label);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException!");
            }
        }

    }
}
