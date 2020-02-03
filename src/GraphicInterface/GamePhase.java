package GraphicInterface;

import Exceptions.EdgeNotFoundException;
import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;
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
import javax.swing.SwingUtilities;

public class GamePhase extends JLabel {

    private int help;
    private JFrame frame;
    private JLabel mainPanel;
    private ArrayUnorderedList<JButton> portas;
    private ArrayUnorderedList<String> portasNomes;
    private boolean sound;
    private HauntedHouseGraph mapGraph;
    private JLabel ghost;
    private JLabel previous;
    private Clip backgroundSound;
    private Clip deadSound;
    private Clip fantasmaSound;
    private boolean checkGhost;
    private JProgressBar healthBar;

    public GamePhase(JFrame frame, JLabel mainPanel, boolean sound, HauntedHouseGraph mapGraph, JLabel previous, Clip backgroundSound, boolean checkGhost, int help) {
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
        top.setPreferredSize(new Dimension(700, 100));
        //top.setOpaque(false);
        top.setBackground(Color.red);
        center.setPreferredSize(new Dimension(700, 200));
        //center.setOpaque(false);
        center.setBackground(Color.green);
        bottom.setPreferredSize(new Dimension(700, 400));
        //bottom.setOpaque(false);
        bottom.setBackground(Color.blue);

        //////TOP
        //Health Bar
        healthBar.setPreferredSize(new Dimension(200, 30));
        healthBar.setValue((int) mapGraph.getHealthPoints());
        healthBar.setBackground(Color.red);
        healthBar.setForeground(Color.green);
        healthBar.setStringPainted(true);
        healthBar.setString("" + (int) mapGraph.getHealthPoints());
        gbc.insets = new Insets(0, 0, 0, 200);
        gbc.gridx = 0;
        gbc.gridy = 0;
        top.add(healthBar, gbc);
        //HelpButton
        JButton helpButton = new JButton();
        helpButton.setPreferredSize(new Dimension(100, 30));
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

        helpButton.addActionListener((ActionEvent event) -> {
            int i = 0;
            if (this.help != 0) {
                this.help--;
                Iterator portasIterator = this.portasNomes.iterator();
                Iterator iterator = null;
                try {
                    iterator = mapGraph.iteratorShortestPath(mapGraph.getCurrentPosition(), mapGraph.getEndPosition());
                } catch (EmptyCollectionException | VertexNotFoundException | PathNotFoundException e) {
                    e.printStackTrace();
                }
                iterator.next();
                String stringPorta = (String) iterator.next();
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
        giveUpLabel.setLayout(new GridBagLayout());
        JButton backButton = new JButton();

        backButton.setText("BACK");
        backButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        giveUpLabel.add(backButton, gbc);

        //UPDATE
        this.frame.remove(this);
        this.frame.add(giveUpLabel);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);
        backButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            if (this.sound) {
                if (this.checkGhost) {
                    this.fantasmaSound.stop();
                    this.fantasmaSound.flush();
                    this.fantasmaSound.close();
                }
                this.backgroundSound.stop();
                this.backgroundSound.flush();
                this.backgroundSound.close();
            }
            this.frame.remove(giveUpLabel);
            this.frame.add(this.mainPanel);
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

        JLabel winText = new JLabel();
        winText.setPreferredSize(new Dimension(200, 500));
        String text = "Congratulations " + this.mapGraph.getPlayerName() + "<br>"
                + "you didn't died";
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
            this.backgroundSound.stop();
            this.backgroundSound.flush();
            this.backgroundSound.close();
            if (this.checkGhost) {
                this.fantasmaSound.stop();
                this.fantasmaSound.flush();
                this.fantasmaSound.close();
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
            this.backgroundSound.stop();
            this.backgroundSound.flush();
            this.backgroundSound.close();
            if (this.checkGhost) {
                this.fantasmaSound.stop();
                this.fantasmaSound.flush();
                this.fantasmaSound.close();
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
                this.deadSound.stop();
                this.deadSound.flush();
                this.deadSound.close();
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
        try {
            File soundFile = new File("resources/fantasma.wav");
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
            porta.setText(stringPorta);
            this.portasNomes.addToRear(stringPorta);
            this.portas.addToRear(porta);

            porta.setPreferredSize(new Dimension(100, 200));
            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.insets = new Insets(100, 5, 0, 5);
            label.add(porta, gbc);
            i++;

            porta.addActionListener((ActionEvent event) -> {
                if (this.checkGhost) {
                    if (this.sound) {
                        this.fantasmaSound.stop();
                        this.fantasmaSound.flush();
                        this.fantasmaSound.close();
                    }
                }
                try {
                    GamePhase game;
                    boolean checkG = false;
                    if (this.mapGraph.changePosition(stringPorta)) {
                        checkG = true;
                    }
                    if (this.mapGraph.isComplete()) {
                        this.winScreen();
                    } else if (!this.mapGraph.isAlive()) {
                        this.deadScreen();
                    } else {
                        System.out.println("atual: " + this.mapGraph.getCurrentPosition());
                        game = new GamePhase(this.frame, this.mainPanel, this.sound, this.mapGraph, this, this.backgroundSound, checkG, this.help);
                    }
                } catch (VertexNotFoundException | EdgeNotFoundException ex) {
                }
            });
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
}
