package com.flippingbird;

import javax.swing.*;
import java.io.*;

public class Game {
    private JFrame frame;
    private GamePanel panel;
    private static final String HIGHSCORE_FILE = "highscore.txt";
    
    public Game() {
        Assets.load();
        
        frame = new JFrame("Flipping Bird");
        panel = new GamePanel(this);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
        
        panel.setHighScore(loadHighScore());
    }
    
    public void start() {
        panel.startGame();
    }

    public void saveHighScore(int score) {
        try (PrintWriter out = new PrintWriter(new FileWriter(HIGHSCORE_FILE))) {
            out.println(score);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadHighScore() {
        File file = new File(HIGHSCORE_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                return Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}