package engine.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static BufferedImage removeBackground(BufferedImage image, int backgroundColor) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = result.getRGB(x, y);
                if (pixel == backgroundColor) {
                    result.setRGB(x, y, 0x00000000); // Set the pixel to fully transparent
                }
            }
        }

        return result;
    }
}

