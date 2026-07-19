package com.flippingbird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    public enum State { START, PLAYING, PAUSED, GAMEOVER }

    private State state = State.START;
    private Timer timer;
    private Bird bird;
    private List<Pipe> pipes;
    private int score;
    private int highScore;
    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        
        bird = new Bird();
        pipes = new ArrayList<>();
        score = 0;
        
        timer = new Timer(1000 / 60, this);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e.getKeyCode());
            }
        });
    }

    private void handleInput(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        switch (state) {
            case START:
                if (keyCode == KeyEvent.VK_SPACE) {
                    state = State.PLAYING;
                    bird.jump();
                }
                break;
            case PLAYING:
                if (keyCode == KeyEvent.VK_SPACE) {
                    bird.jump();
                } else if (keyCode == KeyEvent.VK_P) {
                    state = State.PAUSED;
                }
                break;
            case PAUSED:
                if (keyCode == KeyEvent.VK_P) {
                    state = State.PLAYING;
                }
                break;
            case GAMEOVER:
                if (keyCode == KeyEvent.VK_R) {
                    resetGame();
                    state = State.PLAYING;
                }
                break;
        }
    }

    private void resetGame() {
        bird.reset();
        pipes.clear();
        score = 0;
    }

    public void startGame() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == State.PLAYING) {
            update();
        }
        repaint();
    }

    private void update() {
        bird.update();

        // Check ground/ceiling collision
        if (bird.getY() < 0 || bird.getY() + Bird.HEIGHT > 500) {
            gameOver();
        }

        // Pipe management
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < 500) {
            pipes.add(new Pipe(800));
        }

        for (int i = pipes.size() - 1; i >= 0; i--) {
            Pipe p = pipes.get(i);
            p.update();

            // Collision detection
            if (bird.getBounds().intersects(p.getTopBounds()) || 
                bird.getBounds().intersects(p.getBottomBounds())) {
                gameOver();
            }

            // Scoring
            if (!p.isPassed() && bird.getBounds().x > p.getX() + Pipe.WIDTH) {
                p.setPassed(true);
                score++;
                Assets.score.play();
                if (score > highScore) {
                    highScore = score;
                    game.saveHighScore(highScore);
                }
            }

            if (p.isOffScreen()) {
                pipes.remove(i);
            }
        }
    }

    private void gameOver() {
        state = State.GAMEOVER;
        Assets.hit.play();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Background
        g.drawImage(Assets.background, 0, 0, 800, 600, null);

        // Pipes
        for (Pipe p : pipes) {
            p.draw(g);
        }

        // Bird
        bird.draw(g);

        // UI
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        if (state == State.START) {
            drawCenteredString(g, "PRESS SPACE TO START", 300);
        } else if (state == State.PAUSED) {
            drawCenteredString(g, "PAUSED", 300);
        } else if (state == State.GAMEOVER) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 800, 600);
            g.setColor(Color.WHITE);
            drawCenteredString(g, "GAME OVER", 250);
            drawCenteredString(g, "SCORE: " + score, 300);
            drawCenteredString(g, "BEST: " + highScore, 350);
            drawCenteredString(g, "PRESS R TO RESTART", 450);
        }

        if (state != State.GAMEOVER) {
            g.drawString("Score: " + score, 20, 40);
            g.drawString("Best: " + highScore, 20, 70);
        }
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (800 - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}