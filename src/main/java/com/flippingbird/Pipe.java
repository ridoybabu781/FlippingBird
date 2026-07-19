package com.flippingbird;

import java.awt.*;
import java.util.Random;

public class Pipe {
    public static final int WIDTH = 52;
    public static final int HEIGHT = 320;
    public static final int GAP = 150;
    public static final int SPEED = 4;

    private int x;
    private int y; // Y position of the TOP pipe's BOTTOM edge
    private boolean passed = false;
    private static Random random = new Random();

    public Pipe(int startX) {
        this.x = startX;
        // Randomize the gap position. 
        // Window is 600 high. Ground starts at 500.
        // We want the gap to be between 100 and 400.
        this.y = random.nextInt(300) + 100;
    }

    public void update() {
        x -= SPEED;
    }

    public void draw(Graphics g) {
        // Top pipe (flipped)
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(x + WIDTH, y);
        g2d.rotate(Math.PI);
        g2d.drawImage(Assets.pipe, 0, 0, WIDTH, HEIGHT, null);
        g2d.dispose();

        // Bottom pipe
        g.drawImage(Assets.pipe, x, y + GAP, WIDTH, HEIGHT, null);
    }

    public boolean isOffScreen() {
        return x + WIDTH < 0;
    }

    public Rectangle getTopBounds() {
        return new Rectangle(x, y - HEIGHT, WIDTH, HEIGHT);
    }

    public Rectangle getBottomBounds() {
        return new Rectangle(x, y + GAP, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}