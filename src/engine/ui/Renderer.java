package engine.ui;

import engine.Engine;
import engine.pieces.*;
import engine.player.Player;

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
    Engine e;
    Coordinate previousPosition;
    private boolean canPaintMoves;
    Board b;

    public Renderer(Engine e) {
        this.e = e;
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
                movingPiece = getPiece(coordinate);
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
                        performMove(coordinate, col, row);
                        checkAndSetCheck();
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
                if (movingPiece != null && isInBoardBounds(e)) {
                    movingPiece.setDrawPosition(new Coordinate(e.getX() - SQUARE_SIZE / 2, e.getY() - SQUARE_SIZE / 2));
                    repaint();
                }
            }
        });
    }

    private void performMove(Coordinate coordinate, int col, int row) {
        removeUniqueEvents(); // This removes castles/pawn double jump if piece is moved
        performCastle(coordinate); // Will move other piece involved in castle if castle otherwise will do nothing
        removePiece(coordinate); // If a piece is jumped then remove it
        movePiece(coordinate, col, row);
        switchPlayers();
    }

    private boolean isInBoardBounds(MouseEvent e) {
        return movingPiece != null && e.getX() > 0 && e.getX() < BASE_WIDTH && e.getY() > 0 && e.getY() < BASE_HEIGHT;
    }

    private void switchPlayers(){
        e.switchPlayers();
    }

    private void checkAndSetCheck(){
        e.runPlayerChecks();
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
        return  7 - (y / SQUARE_SIZE);
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
            BufferedImage sourceImage = ImageIO.read(new File(filePath));
            int backgroundColor = sourceImage.getRGB(0, 0); // Get the background color (assumed at top-left corner)
            BufferedImage imageWithoutBackground = ImageUtils.removeBackground(sourceImage, backgroundColor);
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

        highlightKingIfCheck(g);

        if (canPaintMoves){
            Graphics2D g2d = (Graphics2D) g;
            float thickness = 2;
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(thickness));
            g2d.setColor(Color.YELLOW);


            for(Coordinate move : movingPiece.getMoves(b)){
                Coordinate drawPosition = movingPiece.getDrawPosition();
                if (move.equals(new Coordinate(convertToColumn(drawPosition.getX() + SQUARE_SIZE / 2), convertToRow(drawPosition.getY() + SQUARE_SIZE / 2)))) {
                    g2d.setColor(new Color(255, 255, 0, 128));
                    g2d.fillRect(convertToPixelX(move.getX(), BASE_WIDTH), convertToPixelY(move.getY(), BASE_HEIGHT), SQUARE_SIZE, SQUARE_SIZE);
                    g2d.setColor(Color.YELLOW);
                }
                g2d.drawRect(convertToPixelX(move.getX(), BASE_WIDTH), convertToPixelY(move.getY(), BASE_HEIGHT), SQUARE_SIZE, SQUARE_SIZE);
            }
            g2d.setStroke(oldStroke);
        }
        // Draw pieces
        List<Piece> piecesCopy;
        synchronized (pieces) {
            piecesCopy = new ArrayList<>(pieces);
        }
        for (Piece piece : piecesCopy) {
            drawPiece(g, piece);
        }
    }

    private void highlightKingIfCheck(Graphics g) {
        King checkedKing;
//        System.out.println("p1 checked: " + b.getPlayerOne().isInCheck());
//        System.out.println("p2 checked: " + b.getPlayerTwo().isInCheck());
        if (b.getPlayerOne().isInCheck())
            checkedKing = b.getPlayerOne().getKing();
        else if (b.getPlayerTwo().isInCheck())
            checkedKing = b.getPlayerTwo().getKing();
        else return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 0, 0, 128));
        g2d.fillRect(checkedKing.getDrawPosition().getX(), checkedKing.getDrawPosition().getY(), SQUARE_SIZE, SQUARE_SIZE);

    }

    public void performAiMove(ArrayList<Coordinate> move){
        if (!move.isEmpty()) {
            Coordinate from = move.getFirst();
            Coordinate to = move.getLast();
            movingPiece = getPiece(from);
            performMove(to, to.getX(), to.getY());
            repaint();
            e.switchPlayers();
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

    private Piece getPiece(Coordinate coordinate) {
            System.out.println("get piece at " + coordinate);
        List<Piece> pieces1 = pieces.stream().filter(piece -> piece.getPosition().equals(coordinate)).toList();
        if (pieces1.isEmpty() && e.getPlayerTurn() == TEAM_TWO)
            System.out.println("no piece");
        else
            if (!pieces1.isEmpty() && e.getPlayerTurn() == TEAM_TWO)
                System.out.println(pieces1.getFirst());

        if (pieces1.isEmpty())
            return new Pawn(false, new Coordinate(-100,-100), new Coordinate(-100,-100), -1, new BufferedImage(1,1,1));
        if (pieces1.getFirst().getTeam() != e.getPlayerTurn())
            return new Pawn(false, new Coordinate(-100,-100), new Coordinate(-100,-100), -1, new BufferedImage(1,1,1));
        return pieces1.getFirst();
    }
}