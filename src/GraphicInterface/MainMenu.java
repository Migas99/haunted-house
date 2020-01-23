package GraphicInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainMenu extends JFrame {

    JButton playButton;
    JButton mapButton;
    JComboBox settings;
    JLabel background = new JLabel();

    public MainMenu() {
        super("HauntedHouse");
        this.setLocationByPlatform(true);
        this.setSize(700, 700);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

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
            buttonsPanel.setPreferredSize(new Dimension(700, 80));
            map.setPreferredSize(new Dimension(700, 625));
            
            //MapsList
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(25,15,25,0);
            mapsList.setPreferredSize(new Dimension(100,30));
            buttonsPanel.add(mapsList, gbc);
            
            //Confirm button
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(25,0,25,300);
            confirmButton.setText("Confirm");
            confirmButton.setPreferredSize(new Dimension(100,30));
            buttonsPanel.add(confirmButton, gbc);
            
            //Back button
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.insets = new Insets(25,0,25,15);
            backButton.setText("Back");
            backButton.setPreferredSize(new Dimension (100,30));
            buttonsPanel.add(backButton, gbc);
            
            mainPanel.add(buttonsPanel,BorderLayout.PAGE_START);
            mainPanel.add(map,BorderLayout.PAGE_END);
            
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

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
    }
}
