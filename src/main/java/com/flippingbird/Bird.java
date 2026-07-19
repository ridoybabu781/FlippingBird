package com.flippingbird;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bird {
    public static final int WIDTH = 34;
    public static final int HEIGHT = 24;
    private static final double GRAVITY = 0.6;
    private static final double JUMP_STRENGTH = -9.0;
    private static final double MAX_VELOCITY = 10.0;

    private double x;
    private double y;
    private double velocity;
    private double rotation;

    public Bird() {
        reset();
    }

    public void reset() {
        this.x = 100;
        this.y = 300;
        this.velocity = 0;
        this.rotation = 0;
    }

    public void update() {
        velocity += GRAVITY;
        if (velocity > MAX_VELOCITY) {
            velocity = MAX_VELOCITY;
        }
        y += velocity;

        // Calculate rotation based on velocity
        rotation = Math.toRadians(velocity * 3);
        if (rotation > Math.PI / 2) rotation = Math.PI / 2;
        if (rotation < -Math.PI / 4) rotation = -Math.PI / 4;
    }

    public void jump() {
        velocity = JUMP_STRENGTH;
        Assets.jump.play();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        AffineTransform at = new AffineTransform();
        at.translate(x + WIDTH / 2.0, y + HEIGHT / 2.0);
        at.rotate(rotation);
        at.translate(-WIDTH / 2.0, -HEIGHT / 2.0);
        
        g2d.drawImage(Assets.bird, at, null);
        g2d.dispose();
    }

    public Rectangle getBounds() {
        // Slightly smaller hitbox for fairer gameplay
        return new Rectangle((int) x + 2, (int) y + 2, WIDTH - 4, HEIGHT - 4);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}