package GraphicInterface;

import Exceptions.VertexNotFoundException;
import HauntedHouse.HauntedHouseGraph;
import LinkedList.ArrayUnorderedList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.SwingUtilities;

public class GamePhase extends JLabel {

    private JFrame frame;
    private JLabel mainPanel;
    private ArrayUnorderedList<JButton> portas;
    private boolean sound;
    private HauntedHouseGraph mapGraph;
    private JLabel ghost;
    private JLabel previous;
    private Clip backgroundSound;

    public GamePhase(JFrame frame, JLabel mainPanel, boolean sound, HauntedHouseGraph mapGraph, JLabel previous) {
        this.frame = frame;
        this.mainPanel = mainPanel;
        this.sound = sound;
        this.mapGraph = mapGraph;
        this.previous = previous;

        this.setLayout(new BorderLayout());

        roomScreen();
    }

    public void roomScreen() {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel top = new JPanel();
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

        //TOP
        giveUp.setText("GIVE UP");
        giveUp.setPreferredSize(new Dimension(200, 50));
        top.add(giveUp);

        this.add(top, BorderLayout.PAGE_START);
        this.add(center, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.PAGE_END);

        if (this.sound) {
            try {
                backgroundSound = AudioSystem.getClip();
                backgroundSound.open(this.backgroundSound());
                backgroundSound.start();
                backgroundSound.loop(backgroundSound.LOOP_CONTINUOUSLY);
            } catch (LineUnavailableException ex) {
            } catch (IOException ex) {
            }
        }

        //UPDATE
        this.frame.remove(this.previous);
        this.frame.add(this);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);

        giveUp.addActionListener((ActionEvent event) -> {
            giveUpScreen(this);
        });
    }

    public void giveUpScreen(JLabel label) {
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
        this.frame.remove(label);
        this.frame.add(giveUpLabel);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);
        backButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            if (this.sound) {
                backgroundSound.stop();
                backgroundSound.flush();
                backgroundSound.close();
            }
            this.frame.remove(giveUpLabel);
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

        wastedImg.setIcon(new ImageIcon("resources/wasted.gif"));
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
            this.deadSound().start();
        }
        //UPDATE
        this.frame.remove(this);
        this.frame.add(deadLabel);
        SwingUtilities.updateComponentTreeUI(this.frame);
        this.frame.setVisible(true);

        backButton.addActionListener((ActionEvent event) -> {
            //UPDATE
            if (this.sound) {
                this.deadSound().stop();
            }
            this.frame.remove(deadLabel);
            this.frame.add(this.mainPanel);
            SwingUtilities.updateComponentTreeUI(this.frame);
            this.frame.setVisible(true);
        });
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

    public Clip deadSound() {
        Clip clip = null;
        try {
            File soundFile = new File("resources/wasted.wav");
            AudioInputStream audioinputstream = AudioSystem.getAudioInputStream(soundFile);
            try {
                clip = AudioSystem.getClip();
                clip.open(audioinputstream);
            } catch (LineUnavailableException e) {
            }
        } catch (UnsupportedAudioFileException | IOException e) {
        }

        return clip;
    }

    public ArrayUnorderedList<JButton> getPortas() {
        return portas;
    }

    public void setPortas() throws VertexNotFoundException {
        int numPortas = this.mapGraph.getAvailableDoors(this.mapGraph.getCurrentPosition()).size();
        for (int i = 0; i < numPortas - 1; i++) {
            this.portas.addToRear(new JButton());
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

    public JLabel getGhost() {
        return ghost;
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
