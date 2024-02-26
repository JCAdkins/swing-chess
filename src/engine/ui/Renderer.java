package engine.ui;

import engine.pieces.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static engine.helpers.GlobalHelper.*;

public class Renderer extends JPanel {
    private Image[] pieceImagesT1 = new BufferedImage[TEAM_SIZE];
    private Image[] pieceImagesT2 = new BufferedImage[TEAM_SIZE];
    private final ImageIcon boardImage;
    private final ArrayList<Piece> pieces = new ArrayList<>();
    Piece movingPiece;
    Coordinate previousPosition;
    Board b;

    public Renderer() {
        // Load chessboard image
        boardImage = new ImageIcon("sbs_-_2d_chess_pack/Top Down/Boards/Tops/Top - Marble 2 TD 512x520.png");
        // Extract pieces from the source image
        pieceImagesT1 = extractPieces(TEAM_ONE);
        pieceImagesT2 = extractPieces(TEAM_TWO);
        // Add a component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mousePressed(MouseEvent e) {
                Coordinate coordinate = new Coordinate(convertToColumn(e.getX()), convertToRow(e.getY()));
                movingPiece = getPieceToMove(coordinate);
                previousPosition = movingPiece.getPosition();
            }

            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (movingPiece != null && e.getX() > 0 && e.getX() < 512 && e.getY() > 0 && e.getY() < 512) {
                    int col = convertToColumn(e.getX());
                    int row = convertToRow(e.getY());
                    Coordinate coordinate = new Coordinate(col, row);
                    if (isLegalMove(movingPiece, coordinate)) {
                        movingPiece.setPosition(coordinate);
                        movingPiece.setDrawPosition(new Coordinate(convertToPixelX(col, 512), convertToPixelY(row, 512)));
                    }else {
                        resetPiece();
                    }
                    repaint();
                } else if (movingPiece != null && (e.getX() < 0 || e.getX() > 512 || e.getY() < 0 || e.getY() > 512)) {
                        resetPiece();
                        repaint();
                }


            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (movingPiece != null && e.getX() > 0 && e.getX() < 512 && e.getY() > 0 && e.getY() < 512) {
                    movingPiece.setDrawPosition(new Coordinate(e.getX() - 32, e.getY() - 32));
                    repaint();
                }
            }
        });
    }

    private void resetPiece() {
        movingPiece.setPosition(previousPosition);
        movingPiece.setDrawPosition(new Coordinate(convertToPixelX(previousPosition.getX(), 512), convertToPixelY(previousPosition.getY(), 512)));
    }

    private boolean isLegalMove(Piece movingPiece, Coordinate coordinate) {
        return movingPiece.getMoves(b).contains(coordinate);
    }

    private int convertToRow(int y) {
        int squareSizeY = getHeight() / COLUMNS;
        return  (y / squareSizeY);
    }

    private int convertToColumn(int x) {
        int squareSizeX = getWidth() / ROWS;
        return x / squareSizeX;
    }

    public void setBoard(Board b){
        this.b = b;
    }

    // Method to extract pieces from the source image
    private Image[] extractPieces(int team) {
        String teamOneFilePath = "sbs_-_2d_chess_pack/Top Down/Pieces/Black/Black - Rust 1 128x128.png";
        String teamTwoFilePath = "sbs_-_2d_chess_pack/Top Down/Pieces/White/White - Rust 1 128x128.png";
        String filePath = (team == TEAM_ONE) ? teamOneFilePath : teamTwoFilePath;
        try {
            // Load the source image
            BufferedImage sourceImage = ImageIO.read(new File(filePath));
            int backgroundColor = sourceImage.getRGB(0, 0); // Get the background color (assumed at top-left corner)
            BufferedImage imageWithoutBackground = ImageUtils.removeBackground(sourceImage, backgroundColor);
            // Extract individual pieces using PieceExtractor class
            PieceExtractor extractor = new PieceExtractor();
            BufferedImage[] sprites = extractor.extractPieces(imageWithoutBackground);
            return scaleSprites(sprites);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new BufferedImage[0];
    }

    private Image[] scaleSprites(BufferedImage[] sprites) {
        Image[] scaledImages = new Image[sprites.length];
        int sWidth = 64;
        int sHeight = 64;

        for (int i = 0; i < scaledImages.length; i++) {
            scaledImages[i] = sprites[i].getScaledInstance(sWidth, sHeight, Image.SCALE_SMOOTH);
        }
        return scaledImages;
    }

    public Image[] getPieceImages(int team) {
        return (team == 1) ? pieceImagesT1 : pieceImagesT2;
    }

    // Override the paintComponent method to draw the chessboard and pieces
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw chessboard image
        g.drawImage(boardImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        // Draw pieces
        for (Piece piece : pieces) {
            drawPiece(g, piece);
        }
    }

    private void drawPiece(Graphics g, Piece piece) {
        g.drawImage(piece.getSprite(), piece.getDrawPosition().getX(), piece.getDrawPosition().getY(), this);
    }

    @Override
    public Dimension getPreferredSize() {
        if (boardImage != null) {
            return new Dimension(boardImage.getIconWidth(), boardImage.getIconHeight());
        } else {
            return super.getPreferredSize();
        }
    }

    public void setPieces(ArrayList<Piece> list) {
        this.pieces.addAll(list);
    }

    private Piece getPieceToMove(Coordinate coordinate) {
        List<Piece> pieces1 = pieces.stream().filter(piece -> piece.getPosition().equals(coordinate)).toList();
        return pieces1.isEmpty() ? new Pawn(false, new Coordinate(-100,-100), new Coordinate(-100,-100), -1, new BufferedImage(1,1,1)) : pieces1.getFirst();
    }
}