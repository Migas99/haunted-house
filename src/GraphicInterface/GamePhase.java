package GraphicInterface;

import Exceptions.EdgeNotFoundException;
import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;
import HauntedHouse.ClassificationManager;
import HauntedHouse.HauntedHouseGraph;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GamePhase extends JLabel {

    private int help;
    private JFrame frame;
    private JLabel mainPanel;
    private ArrayUnorderedList<JButton> portas;
    private ArrayUnorderedList<String> portasNomes;
    private boolean sound;
    private boolean sound18;
    private boolean simulation;
    private HauntedHouseGraph mapGraph;
    private JLabel ghost;
    private JLabel previous;
    private Clip backgroundSound;
    private Clip deadSound;
    private Clip fantasmaSound;
    private boolean checkGhost;
    private JProgressBar healthBar;
    ClassificationManager manager;

    public GamePhase(JFrame frame, JLabel mainPanel, boolean sound, HauntedHouseGraph mapGraph,
            JLabel previous, Clip backgroundSound, boolean checkGhost, int help, boolean sound18, boolean simulation) {
        this.frame = frame;
        this.mainPanel = mainPanel;
        this.sound = sound;
        this.mapGraph = mapGraph;
        this.previous = previous;
        this.ghost = new JLabel();
        this.healthBar = new JProgressBar();
        this.portas = new ArrayUnorderedList();
        this.portasNomes = new ArrayUnorderedList();
        this.backgroundSound = backgroundSound;
        this.checkGhost = checkGhost;
        this.help = help;
        this.sound18 = sound18;
        this.simulation = simulation;
        manager = new ClassificationManager();

        this.setLayout(new BorderLayout());

        try {
            roomScreen();
        } catch (VertexNotFoundException e) {
        }
    }

    public void roomScreen() throws VertexNotFoundException {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel top = new JPanel(new GridBagLayout());
        JPanel center = new JPanel();
        JPanel bottom = new JPanel(new GridBagLayout());
        JButton giveUp = new JButton();
        JButton helpButton = new JButton();
        top.setPreferredSize(new Dimension(700, 100));
        top.setOpaque(false);
        center.setPreferredSize(new Dimension(700, 250));
        center.setOpaque(false);
        bottom.setPreferredSize(new Dimension(700, 350));
        bottom.setOpaque(false);
        this.setIcon(this.setBackgroundIcon());

        //////TOP
        //Health Bar
        healthBar.setPreferredSize(new Dimension(200, 30));
        healthBar.setValue((int) mapGraph.getHealthPoints());
        healthBar.setBackground(Color.red);
        healthBar.setForeground(Color.green);
        healthBar.setStringPainted(true);
        healthBar.setString("" + (int) mapGraph.getHealthPoints());
        gbc.insets = new Insets(0, 0, 0, 150);
        gbc.gridx = 0;
        gbc.gridy = 0;
        top.add(healthBar, gbc);

        //HelpButton
        helpButton.setPreferredSize(new Dimension(150, 30));
        helpButton.setText("Remaining: " + this.help);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 50);
        top.add(helpButton, gbc);
        //GiveUp
        giveUp.setText("GIVE UP");
        giveUp.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        top.add(giveUp, gbc);
        if (simulation) {
            helpButton.setEnabled(false);
            giveUp.setEnabled(false);
        }

        //////CENTER
        if (this.checkGhost) {
            this.setGhost();
            if (this.sound) {
                try {
                    this.fantasmaSound = AudioSystem.getClip();
                    this.fantasmaSound.open(this.fantasmaSound());
                    this.fantasmaSound.start();
                    this.fantasmaSound.loop(backgroundSound.LOOP_CONTINUOUSLY);
                } catch (LineUnavailableException ex) {
                } catch (IOException ex) {
                }
            }
        }
        center.add(this.ghost, JLabel.CENTER);

        //////BOTTOM
        this.setPortas(bottom);

        this.add(top, BorderLayout.PAGE_START);
        this.add(center, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.PAGE_END);

        //UPDATE
        this.frame.remove(this.previous);
        this.frame.add(this);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);

        giveUp.addActionListener((ActionEvent event) -> {
            giveUpScreen();
        });

        //responsable to point the right direction on map is help is avaiable
        helpButton.addActionListener((ActionEvent event) -> {
            int i = 0;
            if (this.help != 0) {
                this.help--;
                Iterator portasIterator = this.portasNomes.iterator();
                Iterator iteratorShortPath = null;
                try {
                    iteratorShortPath = mapGraph.iteratorShortestPath(mapGraph.getCurrentPosition(), mapGraph.getEndPosition());
                } catch (EmptyCollectionException | VertexNotFoundException | PathNotFoundException e) {
                    e.printStackTrace();
                }
                iteratorShortPath.next();
                String stringPorta = (String) iteratorShortPath.next();
                JLabel labelSeta = new JLabel();
                labelSeta.setIcon(new ImageIcon("resources/seta.png"));
                labelSeta.setBackground(new Color(0, 0, 0, 0));
                while (portasIterator.hasNext()) {
                    String iteratorString = (String) portasIterator.next();
                    if (iteratorString.equals(stringPorta)) {
                        gbc.gridx = i;
                        gbc.gridy = 1;
                        gbc.insets = new Insets(0, 5, 0, 5);
                        bottom.add(labelSeta, gbc);
                        SwingUtilities.updateComponentTreeUI(this.frame);
                        this.frame.setVisible(true);
                    } else {
                        i++;
                    }
                }
            }
        });
    }

    public void giveUpScreen() {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel giveUpLabel = new JLabel();
        giveUpLabel.setIcon(new ImageIcon("resources/giveup.gif"));
        giveUpLabel.setLayout(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        JPanel textPanel = new JPanel(new GridBagLayout());
        JButton backButton = new JButton();
        JButton resumeButton = new JButton();
        JLabel text = new JLabel();

        buttonsPanel.setPreferredSize(new Dimension(700, 200));
        buttonsPanel.setOpaque(false);
        textPanel.setPreferredSize(new Dimension(700, 500));
        textPanel.setOpaque(false);

        //UPDATE
        if (this.sound) {
            if (this.checkGhost) {
                this.stopSound(this.fantasmaSound);
            }
        }

        text.setText("<html><div style='text-align: center;'>" + "Are you sure <br/>about that?" + "</div></html>");
        text.setFont(new Font("Arial", Font.BOLD, 50));
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setVerticalAlignment(SwingConstants.CENTER);
        text.setForeground(Color.white);
        text.setBackground(Color.red);
        textPanel.add(text);

        backButton.setText("YES");
        backButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 100);
        buttonsPanel.add(backButton, gbc);

        resumeButton.setText("NO");
        resumeButton.setPreferredSize(new Dimension(200, 50));
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 1;
        buttonsPanel.add(resumeButton, gbc);

        giveUpLabel.add(buttonsPanel, BorderLayout.PAGE_END);
        giveUpLabel.add(textPanel, BorderLayout.PAGE_START);

        //UPDATE
        this.frame.remove(this);
        this.frame.add(giveUpLabel);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);
        backButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            if (this.sound) {
                this.stopSound(this.backgroundSound);
            }
            this.frame.remove(giveUpLabel);
            this.frame.add(this.mainPanel);
            SwingUtilities.updateComponentTreeUI(this.frame);
            this.frame.setVisible(true);
        });

        resumeButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            if (this.sound) {
                if (this.checkGhost) {
                    try {
                        this.fantasmaSound = AudioSystem.getClip();
                        this.fantasmaSound.open(this.fantasmaSound());
                        this.fantasmaSound.start();
                        this.fantasmaSound.loop(backgroundSound.LOOP_CONTINUOUSLY);
                    } catch (LineUnavailableException ex) {
                    } catch (IOException ex) {
                    }
                }
            }
            this.frame.remove(giveUpLabel);
            this.frame.add(this);
            SwingUtilities.updateComponentTreeUI(this.frame);
            this.frame.setVisible(true);
        });
    }

    public void winScreen() {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel winLabel = new JLabel();
        winLabel.setLayout(new GridBagLayout());
        winLabel.setIcon(new ImageIcon("resources/win.gif"));
        JButton backButton = new JButton();
        JLabel topScore = new JLabel();
        topScore.setText("");

        JLabel winText = new JLabel();
        winText.setPreferredSize(new Dimension(500, 100));
        String text = "<p>Congratulations " + this.mapGraph.getPlayerName() + "<p/>"
                + "<p>Your Score Was:<p/>"
                + "<p>" + this.mapGraph.getHealthPoints() + "<p/>";
        winText.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
        winText.setFont(new Font("Arial", Font.BOLD, 15));
        winText.setForeground(Color.white);
        winText.setHorizontalAlignment(SwingConstants.CENTER);
        winText.setVerticalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        winLabel.add(winText, gbc);

        topScore.setText("<html>" + this.getTopTen() + "</html>");
        topScore.setPreferredSize(new Dimension(500, 300));
        topScore.setForeground(Color.white);
        topScore.setHorizontalAlignment(SwingConstants.CENTER);
        topScore.setVerticalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        winLabel.add(topScore, gbc);

        backButton.setText("BACK");
        backButton.setPreferredSize(new Dimension(100, 30));
        gbc.gridy = 2;
        gbc.insets = new Insets(200, 0, 0, 0);
        winLabel.add(backButton, gbc);

        //UPDATE
        if (this.sound) {
            this.stopSound(this.backgroundSound);
            if (this.checkGhost) {
                this.stopSound(this.fantasmaSound);
            }
        }
        this.frame.remove(this);
        this.frame.add(winLabel);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);

        backButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            this.frame.remove(winLabel);
            this.frame.add(this.mainPanel);
            SwingUtilities.updateComponentTreeUI(this.frame);
            this.frame.setVisible(true);
        });
    }

    public String getTopTen() {
        ArrayUnorderedList<ArrayUnorderedList<String>> ok = null;
        String topTen = "<p>--------------------------------Top 10--------------------------<p/>";
        try {
            ok = this.manager.getClassificationTable(mapGraph.getMapName(), mapGraph.getLevel());
        } catch (FileNotFoundException | EmptyCollectionException ex) {
        }
        ArrayUnorderedList<String> kek;

        Iterator it1 = ok.iterator();
        int k = 0;
        while (it1.hasNext() && k < 10) {
            kek = (ArrayUnorderedList<String>) it1.next();
            Iterator it2 = kek.iterator();
            int j = 0;
            while (it2.hasNext()) {
                String value = (String) it2.next();
                if (j == 0) {
                    topTen = "<p>" + topTen + "Nome: " + value + "   ";
                }
                if (j == 4) {
                    topTen = topTen + "Pontuação: " + value + "<p/>";
                }
                j++;
            }
            k++;
        }
        return topTen;
    }

    public void simulationScreen() {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel winLabel = new JLabel();
        winLabel.setLayout(new GridBagLayout());
        winLabel.setIcon(new ImageIcon("resources/win.gif"));
        JButton backButton = new JButton();

        JLabel winText = new JLabel();
        winText.setPreferredSize(new Dimension(200, 500));
        String text = "Isto foi uma simulacao";
        winText.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
        winText.setFont(new Font("Arial", Font.BOLD, 30));
        winText.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(200, 0, 0, 0);
        winLabel.add(winText, gbc);

        backButton.setText("BACK");
        backButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridy = 1;
        gbc.insets = new Insets(175, 0, 0, 0);
        winLabel.add(backButton, gbc);

        //UPDATE
        if (this.sound) {
            this.stopSound(this.backgroundSound);
            if (this.checkGhost) {
                this.stopSound(this.fantasmaSound);
            }
        }
        this.frame.remove(this);
        this.frame.add(winLabel);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);

        backButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            this.frame.remove(winLabel);
            this.frame.add(this.mainPanel);
            SwingUtilities.updateComponentTreeUI(this.frame);
            this.frame.setVisible(true);
        });
    }

    public void deadScreen() {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel deadLabel = new JLabel();
        deadLabel.setLayout(new GridBagLayout());
        JLabel wastedImg = new JLabel();
        JButton backButton = new JButton();

        wastedImg.setIcon(new ImageIcon("resources/wasted.png"));
        wastedImg.setPreferredSize(new Dimension(400, 400));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 100, 0);
        deadLabel.add(wastedImg, gbc);

        backButton.setText("BACK");
        backButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        deadLabel.add(backButton, gbc);

        if (this.sound) {
            this.stopSound(this.backgroundSound);
            if (this.checkGhost) {
                this.stopSound(this.fantasmaSound);
            }
            try {
                this.deadSound = AudioSystem.getClip();
                this.deadSound.open(this.deadSound());
                this.deadSound.start();
            } catch (LineUnavailableException ex) {
            } catch (IOException ex) {
            }
        }
        //UPDATE
        this.frame.remove(this);
        this.frame.add(deadLabel);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);

        backButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            if (this.sound) {
                this.stopSound(this.deadSound);
            }
            this.frame.remove(deadLabel);
            this.frame.add(this.mainPanel);
            SwingUtilities.updateComponentTreeUI(this.frame);
            this.frame.setVisible(true);
        });
    }

    public AudioInputStream deadSound() {
        AudioInputStream audioinputstream = null;
        try {
            File soundFile = new File("resources/wasted.wav");
            audioinputstream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException | IOException e) {
        }
        return audioinputstream;
    }

    public AudioInputStream fantasmaSound() {
        AudioInputStream audioinputstream = null;
        File soundFile = null;
        try {
            if (this.sound18) {
                soundFile = new File("resources/fantasma+18.wav");
            } else {
                soundFile = new File("resources/fantasma.wav");
            }
            audioinputstream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException | IOException e) {
        }
        return audioinputstream;
    }

    public ArrayUnorderedList<JButton> getPortas() {
        return portas;
    }

    public void setPortas(JPanel label) throws VertexNotFoundException {
        GridBagConstraints gbc = new GridBagConstraints();
        Iterator iterator = this.mapGraph.getAvailableDoors(this.mapGraph.getCurrentPosition()).iterator();
        JButton porta;
        int i = 0;
        while (iterator.hasNext()) {
            String stringPorta = (String) iterator.next();
            System.out.println(stringPorta);

            porta = new JButton();
            porta.setText("<html>" + stringPorta.replace(" ", "<br/>") + "</html>");
            porta.setPreferredSize(new Dimension(100, 200));
            porta.setIcon(new ImageIcon("resources/house/door.png"));
            porta.setForeground(Color.white);
            porta.setBackground(Color.black);
            porta.setHorizontalTextPosition(JButton.CENTER);
            porta.setVerticalTextPosition(JButton.CENTER);
            this.portasNomes.addToRear(stringPorta);
            this.portas.addToRear(porta);

            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 5, 0, 5);
            label.add(porta, gbc);
            i++;

            porta.addActionListener((ActionEvent event) -> {
                if (this.checkGhost) {
                    if (this.sound) {
                        this.stopSound(this.fantasmaSound);
                    }
                }
                try {
                    GamePhase game;
                    boolean checkG = false;
                    if (this.mapGraph.changePosition(stringPorta)) {
                        checkG = true;
                    }
                    if (this.mapGraph.isComplete()) {
                        if (!this.simulation) {
                            this.manager.addNewClassification(mapGraph.getPlayerName(),
                                    mapGraph.getMapName(), mapGraph.getPathTaken(),
                                    mapGraph.getHealthPoints(), mapGraph.getLevel());
                            this.winScreen();
                        } else {
                            this.simulationScreen();
                        }
                    } else if (!this.mapGraph.isAlive()) {
                        this.deadScreen();
                    } else {
                        System.out.println("atual: " + this.mapGraph.getCurrentPosition());
                        game = new GamePhase(this.frame, this.mainPanel, this.sound, this.mapGraph,
                                this, this.backgroundSound, checkG, this.help, this.sound18, this.simulation);
                    }
                } catch (VertexNotFoundException | EdgeNotFoundException | IOException ex) {
                }
            });
            if (this.simulation) {
                porta.setEnabled(false);
                if (this.simulationClick(porta)) {
                    Thread wait = null;
                    wait = new Thread(new Wait(porta));
                    wait.start();
                }
            }
        }
    }

    //Thread waits time value and then start the game
    public class Wait implements Runnable {

        private JButton porta;

        public Wait(JButton porta) {
            this.porta = porta;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                this.porta.setEnabled(true);
                this.porta.doClick();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException!");
            }
        }

    }

    public boolean simulationClick(JButton porta) {
        Iterator iteratorShortPath = null;
        try {
            iteratorShortPath = mapGraph.iteratorShortestPath(mapGraph.getCurrentPosition(), mapGraph.getEndPosition());
        } catch (EmptyCollectionException | VertexNotFoundException | PathNotFoundException e) {
            e.printStackTrace();
        }
        iteratorShortPath.next();
        String stringPorta = (String) iteratorShortPath.next();
        String portaValue = porta.getText();
        portaValue = portaValue.replace("<html>", "");
        portaValue = portaValue.replace("</html>", "");
        portaValue = portaValue.replace("<br/>", "");

        if (portaValue.equals(stringPorta)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public HauntedHouseGraph getMapGraph() {
        return mapGraph;
    }

    public void setMapGraph(HauntedHouseGraph mapGraph) {
        this.mapGraph = mapGraph;
    }

    public void setGhost() {
        if (this.mapGraph.getLevel() == 1) {
            this.ghost.setIcon(new ImageIcon("resources/gengar.gif"));
        }
        if (this.mapGraph.getLevel() == 2) {
            this.ghost.setIcon(new ImageIcon("resources/darkrai.gif"));
        }
        if (this.mapGraph.getLevel() == 3) {
            this.ghost.setIcon(new ImageIcon("resources/giratina.gif"));
        }
    }

    public void stopSound(Clip clip) {
        clip.stop();
        clip.flush();
        clip.close();
    }

    public ImageIcon setBackgroundIcon() {
        ImageIcon icon = null;
        switch ((String) this.mapGraph.getCurrentPosition()) {
            case "entrada":
                icon = new ImageIcon("resources/house/entrada.jpg");
                break;
            case "cozinha":
                icon = new ImageIcon("resources/house/cozinha.jpg");
                break;
            case "hall":
                icon = new ImageIcon("resources/house/hall.jpg");
                break;
            case "escritorio":
                icon = new ImageIcon("resources/house/escritorio.jpg");
                break;
            case "wc":
                icon = new ImageIcon("resources/house/wc.jpg");
                break;
            case "sala de jantar":
                icon = new ImageIcon("resources/house/sala de jantar.jpg");
                break;
            case "sala de estar":
                icon = new ImageIcon("resources/house/sala de estar.jpg");
                break;
            case "corredor":
                icon = new ImageIcon("resources/house/corredor.jpg");
                break;
            case "quarto":
                icon = new ImageIcon("resources/house/quarto.jpg");
                break;
            default:
                icon = new ImageIcon("resources/house/default.png");
                break;
        }
        return icon;
    }
}
