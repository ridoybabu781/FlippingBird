package com.flippingbird;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Assets {
    public static BufferedImage bird;
    public static BufferedImage pipe;
    public static BufferedImage background;
    
    public static Sound jump;
    public static Sound score;
    public static Sound hit;

    public static void load() {
        Utils.generateAssetsIfMissing();

        bird = loadImage("assets/bird.png");
        pipe = loadImage("assets/pipe.png");
        background = loadImage("assets/background.png");

        jump = new Sound("assets/jump.wav");
        score = new Sound("assets/score.wav");
        hit = new Sound("assets/hit.wav");
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }
}