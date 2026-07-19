package com.flippingbird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.imageio.ImageIO;

public class Utils {

    public static void generateAssetsIfMissing() {
        File assetsDir = new File("assets");
        if (!assetsDir.exists()) {
            assetsDir.mkdirs();
        }

        saveImageIfMissing("assets/bird.png", generateBirdImage());
        saveImageIfMissing("assets/pipe.png", generatePipeImage());
        saveImageIfMissing("assets/background.png", generateBackgroundImage());

        saveWavIfMissing("assets/jump.wav", generateJumpSound());
        saveWavIfMissing("assets/score.wav", generateScoreSound());
        saveWavIfMissing("assets/hit.wav", generateHitSound());
    }

    private static void saveImageIfMissing(String path, BufferedImage image) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveWavIfMissing(String path, byte[] data) {
        File file = new File(path);
        if (!file.exists()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage generateBirdImage() {
        BufferedImage img = new BufferedImage(34, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Body
        g.setColor(new Color(255, 200, 0));
        g.fillOval(0, 0, 34, 24);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval(0, 0, 34, 24);

        // Eye
        g.setColor(Color.WHITE);
        g.fillOval(20, 4, 10, 10);
        g.setColor(Color.BLACK);
        g.drawOval(20, 4, 10, 10);
        g.fillOval(25, 7, 4, 4);

        // Beak
        g.setColor(new Color(255, 100, 0));
        int[] bx = {28, 38, 28};
        int[] by = {12, 17, 22};
        // The image is only 34 wide, so beak needs to fit or we expand.
        // Let's adjust beak to fit.
        int[] bx_adj = {26, 33, 26};
        g.fillPolygon(bx_adj, by, 3);
        g.setColor(Color.BLACK);
        g.drawPolygon(bx_adj, by, 3);

        // Wing
        g.setColor(Color.WHITE);
        g.fillOval(4, 10, 14, 10);
        g.setColor(Color.BLACK);
        g.drawOval(4, 10, 14, 10);

        g.dispose();
        return img;
    }

    public static BufferedImage generatePipeImage() {
        BufferedImage img = new BufferedImage(52, 320, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Main pipe
        g.setColor(new Color(0, 180, 0));
        g.fillRect(5, 0, 42, 320);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRect(5, 0, 42, 320);

        // Cap
        g.setColor(new Color(0, 220, 0));
        g.fillRect(0, 0, 52, 30);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 52, 30);

        // Shine/Shading
        g.setColor(new Color(255, 255, 255, 100));
        g.fillRect(10, 0, 5, 320);

        g.dispose();
        return img;
    }

    public static BufferedImage generateBackgroundImage() {
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Sky
        g.setColor(new Color(113, 197, 207));
        g.fillRect(0, 0, 800, 600);

        // Clouds (procedural)
        g.setColor(Color.WHITE);
        for (int i = 0; i < 5; i++) {
            int cx = (int) (Math.random() * 800);
            int cy = (int) (Math.random() * 200) + 50;
            g.fillOval(cx, cy, 60, 40);
            g.fillOval(cx + 20, cy - 10, 50, 40);
            g.fillOval(cx + 40, cy, 60, 40);
        }

        // Hills
        g.setColor(new Color(147, 233, 138));
        g.fillOval(-100, 400, 400, 300);
        g.fillOval(200, 450, 500, 300);
        g.fillOval(600, 420, 400, 300);

        // Ground
        g.setColor(new Color(222, 216, 149));
        g.fillRect(0, 500, 800, 100);
        g.setColor(new Color(113, 191, 46));
        g.fillRect(0, 500, 800, 15);
        g.setColor(Color.BLACK);
        g.drawLine(0, 500, 800, 500);

        g.dispose();
        return img;
    }

    private static byte[] generateJumpSound() {
        return generateWav(0.1, 440, 880, "sine");
    }

    private static byte[] generateScoreSound() {
        return generateWav(0.15, 880, 1200, "square");
    }

    private static byte[] generateHitSound() {
        return generateWav(0.3, 200, 50, "noise");
    }

    private static byte[] generateWav(double duration, double startFreq, double endFreq, String type) {
        int sampleRate = 44100;
        int numSamples = (int) (duration * sampleRate);
        byte[] pcm = new byte[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double progress = (double) i / numSamples;
            double freq = startFreq + (endFreq - startFreq) * progress;
            double sample = 0;

            if (type.equals("sine")) {
                sample = Math.sin(2 * Math.PI * freq * t);
            } else if (type.equals("square")) {
                sample = Math.sin(2 * Math.PI * freq * t) > 0 ? 1 : -1;
            } else if (type.equals("noise")) {
                sample = Math.random() * 2 - 1;
                double envelope = 1.0 - progress;
                sample *= envelope;
            }

            pcm[i] = (byte) (sample * 127);
        }

        return createWavFile(pcm, sampleRate);
    }

    private static byte[] createWavFile(byte[] pcmData, int sampleRate) {
        int fileSize = 44 + pcmData.length;
        ByteBuffer buffer = ByteBuffer.allocate(fileSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // RIFF header
        buffer.put("RIFF".getBytes());
        buffer.putInt(fileSize - 8);
        buffer.put("WAVE".getBytes());

        // fmt chunk
        buffer.put("fmt ".getBytes());
        buffer.putInt(16); // Subchunk1Size
        buffer.putShort((short) 1); // AudioFormat (PCM)
        buffer.putShort((short) 1); // NumChannels (Mono)
        buffer.putInt(sampleRate);
        buffer.putInt(sampleRate); // ByteRate
        buffer.putShort((short) 1); // BlockAlign
        buffer.putShort((short) 8); // BitsPerSample

        // data chunk
        buffer.put("data".getBytes());
        buffer.putInt(pcmData.length);
        buffer.put(pcmData);

        return buffer.array();
    }
}