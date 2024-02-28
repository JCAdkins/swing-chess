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
import java.util.Iterator;
import java.util.List;
import static engine.helpers.GlobalHelper.*;

public class Renderer extends JPanel {
    private Image[] pieceImagesT1 = new BufferedImage[TEAM_SIZE];
    private Image[] pieceImagesT2 = new BufferedImage[TEAM_SIZE];
    private final ImageIcon boardImage;
    private final ArrayList<Piece> pieces = new ArrayList<>();
    Piece movingPiece;
    Coordinate previousPosition;
    private boolean canPaintMoves;
    Board b;

    public Renderer() {
        this.canPaintMoves = false;
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
                canPaintMoves = true;
                repaint();
            }

            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isInBoardBounds(e)) {
                    int col = convertToColumn(e.getX());
                    int row = convertToRow(e.getY());
                    Coordinate coordinate = new Coordinate(col, row);
                    if (isLegalMove(coordinate)) {
                        removeUniqueEvents(); // This removes castles/pawn double jump if piece is moved
                        performCastle(coordinate); // Will move other piece involved in castle if castle otherwise will do nothing
                        removePiece(coordinate); // If a piece is jumped then remove it
                        movePiece(coordinate, col, row);
                    }else {
                        resetPiece();
                    }
                } else
                        resetPiece();
                repaint();
                canPaintMoves = false;

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

    private boolean isInBoardBounds(MouseEvent e) {
        return movingPiece != null && e.getX() > 0 && e.getX() < BASE_WIDTH && e.getY() > 0 && e.getY() < BASE_HEIGHT;
    }

    private void removePiece(Coordinate coordinate){
        Piece pieceToRemove = null;
        for (Piece piece : pieces) {
            if (piece.getPosition().equals(coordinate) && movingPiece.getTeam() != piece.getTeam()) {
                pieceToRemove = piece;
                break;
            }
        }
        if (pieceToRemove != null) {
            b.removePiece(pieceToRemove);
            pieces.remove(pieceToRemove);
        }
    }

    private void movePiece(Coordinate coordinate, int col, int row) {
        movingPiece.setPosition(coordinate);
        movingPiece.setDrawPosition(new Coordinate(convertToPixelX(col, 512), convertToPixelY(row, 512)));
    }

    private void performCastle(Coordinate coordinate) {
        for (Piece piece : pieces){
            if (piece.getTeam() != movingPiece.getTeam())
                continue;
            if (piece.getPosition().equals(coordinate)) {
                piece.setPosition(movingPiece.getPosition());
                if (piece instanceof Rook)
                    ((Rook) piece).setCanCastle(false);
                if (piece instanceof King)
                    ((King) piece).setCanCastle(false);
            }

        }
    }

    // This method is called to remove the double jump from pawns after they've been moved and
    // to remove the castling ability from the Rook/King.
    private void removeUniqueEvents() {
        if (movingPiece instanceof Pawn)
            ((Pawn) movingPiece).setIsFirstMove(false);
        if (movingPiece instanceof King)
            ((King) movingPiece).setCanCastle(false);
        if (movingPiece instanceof Rook)
            ((Rook) movingPiece).setCanCastle(false);
    }

    private void resetPiece() {
        movingPiece.setPosition(previousPosition);
        movingPiece.setDrawPosition(new Coordinate(convertToPixelX(previousPosition.getX(), 512), convertToPixelY(previousPosition.getY(), 512)));
    }

    private boolean isLegalMove(Coordinate coordinate) {
        return movingPiece.getMoves(b).contains(coordinate);
    }

    private int convertToRow(int y) {
        return  (y / SQUARE_SIZE);
    }

    private int convertToColumn(int x) {
        return x / SQUARE_SIZE;
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

        for (int i = 0; i < scaledImages.length; i++) {
            scaledImages[i] = sprites[i].getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
        }
        return scaledImages;
    }

    public Image[] getPieceImages(int team) {
        return (team == TEAM_ONE) ? pieceImagesT1 : pieceImagesT2;
    }

    // Override the paintComponent method to draw the chessboard and pieces
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw chessboard image
        g.drawImage(boardImage.getImage(), 0, 0, getWidth(), getHeight(), this);

        if (canPaintMoves){
            Graphics2D g2d = (Graphics2D) g;
            float thickness = 2;
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(thickness));
            g2d.setColor(Color.YELLOW);

            //
            for(Coordinate move : movingPiece.getMoves(b)){
                Coordinate drawPosition = movingPiece.getDrawPosition();
                if (move.equals(new Coordinate(convertToColumn(drawPosition.getX() + 32), convertToRow(drawPosition.getY() + 32)))) {
                    g2d.setColor(new Color(255, 255, 0, 128));
                    g2d.fillRect(convertToPixelX(move.getX(), BASE_WIDTH), convertToPixelY(move.getY(), BASE_HEIGHT), SQUARE_SIZE, SQUARE_SIZE);
                    g2d.setColor(Color.YELLOW);
                }
                g2d.drawRect(convertToPixelX(move.getX(), BASE_WIDTH), convertToPixelY(move.getY(), BASE_HEIGHT), SQUARE_SIZE, SQUARE_SIZE);
            }
            g2d.setStroke(oldStroke);
        }
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