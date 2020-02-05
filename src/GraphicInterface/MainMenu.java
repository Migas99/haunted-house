package GraphicInterface;

import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;
import HauntedHouse.ClassificationManager;
import HauntedHouse.HauntedHouseGraph;
import HauntedHouse.MapManager;
import LinkedList.ArrayUnorderedList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainMenu extends JFrame {

    HauntedHouseGraph mapGraph;
    boolean soundEnable = true;
    boolean sound18 = false;
    boolean simulation = false;
    MapManager mapManager = new MapManager();
    JButton playButton;
    JButton mapButton;
    JButton scoresButton;
    JComboBox settings;
    JLabel background = new JLabel();
    int level;

    //Creatas the main menu and set value of frame
    public MainMenu() {
        super("HauntedHouse");
        this.setLocationByPlatform(true);
        this.setSize(700, 700);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        mainMenu();

        this.setVisible(true);
    }

    public void mainMenu() {
        background.setLayout(new GridBagLayout());
        background.setIcon(new ImageIcon("resources/background.gif"));

        JLabel title = new JLabel();
        GridBagConstraints gbc = new GridBagConstraints();
        playButton = new JButton();
        mapButton = new JButton();
        scoresButton = new JButton();
        settings = new JComboBox();

        playButton.setPreferredSize(new Dimension(300, 50));
        playButton.setText("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 30));

        mapButton.setPreferredSize(new Dimension(300, 50));
        mapButton.setText("MAP PREVIEW");
        mapButton.setFont(new Font("Arial", Font.BOLD, 30));

        scoresButton.setPreferredSize(new Dimension(300, 50));
        scoresButton.setText("SCORE TABLE");
        scoresButton.setFont(new Font("Arial", Font.BOLD, 30));

        title.setIcon(new ImageIcon("resources/title.png"));

        //TITLE
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 25, 100, 25);
        background.add(title, gbc);

        //START BUTTON
        gbc.gridy = 1;
        gbc.insets = new Insets(25, 25, 25, 25);
        background.add(playButton, gbc);

        //MAP BUTTON
        gbc.gridy = 2;
        background.add(mapButton, gbc);

        //SCORES BUTTON
        gbc.gridy = 3;
        background.add(scoresButton, gbc);

        //PRESS BUTTON MAP PREVIEW
        pressMapButton();

        //PRESS BUTTON TO GO TO GAME MENU
        pressPlayButtonMenu();

        //PRESS BUTTON TO GO TO SCORES TABLE«
        pressScoresButton();

        this.add(background);
    }

    //SHOW SELECTED MAP
    public void pressMapButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel mainPanel = new JLabel();
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        JPanel barPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JLabel map = new JLabel();
        JComboBox mapsList = new JComboBox();
        JButton backButton = new JButton();
        JButton confirmButton = new JButton();
        mapButton.addActionListener((ActionEvent event) -> {
            map.setText("");
            mapsList.removeAllItems();
            buttonsPanel.setPreferredSize(new Dimension(700, 65));
            barPanel.setPreferredSize(new Dimension(700, 20));
            map.setPreferredSize(new Dimension(700, 600));
            barPanel.setBackground(Color.black);

            //MapsList
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 15, 0, 0);
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
            gbc.insets = new Insets(0, 0, 0, 300);
            confirmButton.setText("Confirm");
            confirmButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(confirmButton, gbc);

            //Back button
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 15);
            backButton.setText("Back");
            backButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(backButton, gbc);

            mainPanel.add(buttonsPanel, BorderLayout.PAGE_START);
            mainPanel.add(barPanel, BorderLayout.CENTER);
            mainPanel.add(map, BorderLayout.PAGE_END);

            //UPDATE
            this.remove(this.background);
            this.add(mainPanel);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        //Show selected map
        confirmButton.addActionListener((ActionEvent event) -> {
            HauntedHouseGraph tempMap = new HauntedHouseGraph();
            if (mapsList.getSelectedItem() != null) {
                try {
                    this.mapGraph = this.mapManager.loadMapFromJSON((String) mapsList.getSelectedItem());
                } catch (FileNotFoundException ex) {
                }

                map.setText(this.mapGraph.getMapPreview());
                map.setFont(new Font("Arial", Font.BOLD, 20));
                map.setHorizontalAlignment(SwingConstants.CENTER);
                map.setVerticalAlignment(SwingConstants.CENTER);
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        //Back to main Menu
        backButton.addActionListener((ActionEvent event) -> {
            mainPanel.removeAll();
            this.remove(mainPanel);
            this.add(this.background);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });
    }

    //SHOW SCORES TABLE
    public void pressScoresButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel mainPanel = new JLabel();
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        JPanel barPanel = new JPanel();
        JPanel scoresPanel = new JPanel(new GridBagLayout());
        JComboBox mapsList = new JComboBox();
        JComboBox difficultyList = new JComboBox();
        JButton backButton = new JButton();
        JButton confirmButton = new JButton();
        mainPanel.setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(scoresPanel);

        scoresButton.addActionListener((ActionEvent event) -> {
            mapsList.removeAllItems();
            scoresPanel.removeAll();
            difficultyList.removeAllItems();
            buttonsPanel.setPreferredSize(new Dimension(700, 65));
            barPanel.setPreferredSize(new Dimension(700, 20));
            scroll.setPreferredSize(new Dimension(700, 600));
            barPanel.setBackground(Color.black);

            //MapsList
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 15, 0, 0);
            mapsList.setPreferredSize(new Dimension(150, 30));
            Iterator iterator = mapManager.getMaps().iterator();
            while (iterator.hasNext()) {
                mapsList.addItem(iterator.next());
            }
            mapsList.setSelectedItem(null);
            buttonsPanel.add(mapsList, gbc);

            //DifficultyOptions
            gbc.gridx = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            difficultyList.setPreferredSize(new Dimension(100, 30));
            difficultyList.addItem("1");
            difficultyList.addItem("2");
            difficultyList.addItem("3");
            difficultyList.setSelectedItem(null);
            buttonsPanel.add(difficultyList, gbc);

            //Confirm button
            gbc.gridx = 2;
            gbc.insets = new Insets(0, 0, 0, 200);
            confirmButton.setText("Confirm");
            confirmButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(confirmButton, gbc);

            //Back button
            gbc.gridx = 3;
            gbc.insets = new Insets(0, 0, 0, 15);
            backButton.setText("Back");
            backButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(backButton, gbc);

            mainPanel.add(buttonsPanel, BorderLayout.PAGE_START);
            mainPanel.add(barPanel, BorderLayout.CENTER);
            mainPanel.add(scroll, BorderLayout.PAGE_END);

            //UPDATE
            this.remove(this.background);
            this.add(mainPanel);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        //Back to main Menu
        backButton.addActionListener((ActionEvent event) -> {
            mainPanel.removeAll();
            this.remove(mainPanel);
            this.add(this.background);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        //Confirm button
        confirmButton.addActionListener((ActionEvent event) -> {
            scoresPanel.removeAll();
            ClassificationManager<String> classificationM = new ClassificationManager();
            String mapSelected = (String) mapsList.getSelectedItem();
            String difficultySelected = (String) difficultyList.getSelectedItem();
            gbc.gridx = 0;
            gbc.insets = new Insets(10, 30, 0, 30);

            if (mapSelected != null && difficultySelected != null) {
                boolean check = new File("database/classifications/" + mapSelected + ".json").exists();
                if (check) {
                    ArrayUnorderedList<ArrayUnorderedList<String>> ok = null;
                    ArrayUnorderedList<String> kek = null;
                    try {
                        ok = classificationM.getClassificationTable(mapSelected, Integer.valueOf(difficultySelected));
                    } catch (EmptyCollectionException | FileNotFoundException ex) {
                    }
                    Iterator it1 = ok.iterator();
                    int i = 0;
                    while (it1.hasNext()) {
                        kek = (ArrayUnorderedList<String>) it1.next();
                        Iterator it2 = kek.iterator();
                        String pessoa = "<html>";
                        JLabel pessoaLabel = new JLabel();
                        pessoaLabel.setPreferredSize(new Dimension(600, 50));
                        pessoaLabel.setBackground(Color.black);
                        pessoaLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
                        int j = 0;
                        while (it2.hasNext()) {
                            String value = (String) it2.next();
                            if (j == 0) {
                                pessoa = pessoa + "Nome: " + value + "<br/>";
                            }
                            if (j == 4) {
                                pessoa = pessoa + "Pontuação: " + value + "<br/>";
                            }
                            j++;
                        }
                        pessoaLabel.setText(pessoa);
                        gbc.gridy = i;
                        scoresPanel.add(pessoaLabel, gbc);
                        i++;
                    }
                }
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });
    }

    //SHOW INTERFACE TO USER SELECT GAME PREFERENCES
    public void pressPlayButtonMenu() {
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
        JButton sound18Button = new JButton();
        JButton easyButton = new JButton();
        JButton normalButton = new JButton();
        JButton hardButton = new JButton();
        JButton simulationButton = new JButton();
        JButton startButton = new JButton();

        playButton.addActionListener((ActionEvent event) -> {
            inputUsername.setText("");
            this.mapGraph = new HauntedHouseGraph();
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
            gbc.insets = new Insets(0, 450, 0, 10);
            backButton.setText("BACK");
            backButton.setPreferredSize(new Dimension(100, 30));
            buttonsPanel.add(backButton, gbc);

            //sound18Button
            /*sound18Button.setPreferredSize(new Dimension(150, 30));
            if (!this.sound18) {
                sound18Button.setText("Sound +18: OFF");
            } else {
                sound18Button.setText("Sound +18: ON");
            }
            gbc.gridx = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            buttonsPanel.add(sound18Button, gbc);*/

            //soundButton
            gbc.gridx = 2;
            gbc.insets = new Insets(0, 0, 0, 10);
            if (this.soundEnable) {
                soundButton.setText("Sound: ON");
            } else {
                soundButton.setText("Sound: OFF");
            }
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

        //BACK TO MAIN MENU
        backButton.addActionListener((ActionEvent event) -> {
            mainPanel.removeAll();
            this.remove(mainPanel);
            this.add(this.background);
            SwingUtilities.updateComponentTreeUI(this);
            this.setVisible(true);
        });

        //SET SOUND
        soundButton.addActionListener((ActionEvent event) -> {
            if (soundEnable) {
                soundEnable = false;
                soundButton.setText("Sound: OFF");
            } else {
                soundEnable = true;
                soundButton.setText("Sound: ON");
            }
        });

        //DEF SOUND 18
        sound18Button.addActionListener((ActionEvent event) -> {
            if (sound18) {
                sound18 = false;
                sound18Button.setText("Sound +18: OFF");
            } else {
                sound18 = true;
                sound18Button.setText("Sound +18: ON");
            }
        });

        //SELECT EASY DIFFICULTY
        easyButton.addActionListener((ActionEvent event) -> {
            this.setLevel(1);
            easyButton.setBackground(new Color(204, 204, 204));
            normalButton.setBackground(new JButton().getBackground());
            hardButton.setBackground(new JButton().getBackground());
        });

        //SELECT NORMAL DIFFICULTY
        normalButton.addActionListener((ActionEvent event) -> {
            this.setLevel(2);
            normalButton.setBackground(new Color(204, 204, 204));
            easyButton.setBackground(new JButton().getBackground());
            hardButton.setBackground(new JButton().getBackground());
        });

        //SELECT HARD DIFFICULTY
        hardButton.addActionListener((ActionEvent event) -> {
            this.setLevel(3);
            hardButton.setBackground(new Color(204, 204, 204));
            easyButton.setBackground(new JButton().getBackground());
            normalButton.setBackground(new JButton().getBackground());
        });

        //START SIMULATION
        simulationButton.addActionListener((ActionEvent event) -> {
            this.simulation = true;
            if (mapsList.getSelectedItem() != null && inputUsername.getText() != null) {
                try {
                    this.mapGraph = this.mapManager.loadMapFromJSON((String) mapsList.getSelectedItem());
                    this.mapGraph.setLevel(this.level);
                    this.mapGraph.setPlayerName(inputUsername.getText());
                    this.mapGraph.setDifficulty();
                } catch (FileNotFoundException ex) {
                }
                JLabel path = null;
                Thread wait = null;
                path = this.showPath(mainPanel);
                wait = new Thread(new Wait(path, 5000));
                wait.start();
            }
        });

        //START GAME
        startButton.addActionListener((ActionEvent event) -> {
            this.simulation = false;
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
                map = this.showMap(mainPanel);
                if (this.level == 1) {
                    wait = new Thread(new Wait(map, 15000));
                } else if (this.level == 2) {
                    wait = new Thread(new Wait(map, 10000));
                } else if (this.level == 3) {
                    wait = new Thread(new Wait(map, 10000));
                }
                wait.start();
            }
        });
    }

    public void setLevel(int level) {
        this.level = level;
    }

    //Start sound game if is enable and start the game
    public void pressPlayButton(JLabel label) {
        Clip backgroundSound = null;
        if (this.soundEnable) {
            try {
                backgroundSound = AudioSystem.getClip();
                backgroundSound.open(this.backgroundSound());
                backgroundSound.start();
                backgroundSound.loop(backgroundSound.LOOP_CONTINUOUSLY);
            } catch (LineUnavailableException ex) {
            } catch (IOException ex) {
            }
        }
        int help = 3 - this.mapGraph.getLevel();
        GamePhase game = new GamePhase(this, this.background, this.soundEnable, this.mapGraph,
                label, backgroundSound, false, help, this.sound18, this.simulation);
    }

    public AudioInputStream backgroundSound() {
        AudioInputStream audioinputstream = null;
        try {
            File soundFile = new File("resources/background.wav");
            audioinputstream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException | IOException e) {
        }
        return audioinputstream;
    }

    //Show shortest path to the user
    public JLabel showPath(JLabel mainPanel) {
        String pathString = "";
        JLabel path = new JLabel();
        Iterator iterator = null;
        try {
            iterator = this.mapGraph.iteratorShortestPath(this.mapGraph.getCurrentPosition(), this.mapGraph.getEndPosition());
        } catch (EmptyCollectionException | PathNotFoundException | VertexNotFoundException ex) {
        }
        pathString = pathString + iterator.next() + "<br/>";
        while (iterator.hasNext()) {
            pathString = pathString + "|" + "<br/>";
            pathString = pathString + "V" + "<br/>";
            pathString = pathString + iterator.next() + "<br/>";
        }
        path.setText("<html><div style='text-align: center;'>" + pathString + "</div></html>");
        path.setFont(new Font("Arial", Font.BOLD, 20));
        path.setHorizontalAlignment(SwingConstants.CENTER);
        //UPDATE
        this.remove(mainPanel);
        this.add(path);
        SwingUtilities.updateComponentTreeUI(this);
        this.setVisible(true);

        return path;
    }

    //Show map to the user
    public JLabel showMap(JLabel mainPanel) {
        JLabel map = new JLabel();
        map.setText(this.mapGraph.getMapPreview());
        map.setFont(new Font("Arial", Font.BOLD, 20));
        map.setHorizontalAlignment(SwingConstants.CENTER);
        map.setVerticalAlignment(SwingConstants.CENTER);
        //UPDATE
        this.remove(mainPanel);
        this.add(map);
        SwingUtilities.updateComponentTreeUI(this);
        this.setVisible(true);

        return map;
    }

    //OPEN THE GAME
    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
    }

    //Thread waits time value and then start the game
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
