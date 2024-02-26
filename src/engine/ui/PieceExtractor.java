package engine.ui;

import java.awt.image.BufferedImage;

public class PieceExtractor {
    public BufferedImage[] extractPieces(BufferedImage sourceImage) {
            // Extract individual pieces based on tile set information
            int tileSize = 128; // Size of each tile in pixels
            int numTiles = 16; // Number of tiles in the tile set
            int cols = 4; // Number of columns in the tile set
            int rows = 4;
            BufferedImage[] pieces = new BufferedImage[numTiles];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    pieces[(i * cols) + j] = sourceImage.getSubimage(
                            j * tileSize,
                            i * tileSize,
                            tileSize,
                            tileSize
                    );
                }
            }

        return pieces;
    }
}

