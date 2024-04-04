package engine.ui;

import engine.Engine;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import engine.hardware.pieces.*;
import engine.player.AI;
import engine.player.Player;
import engine.simulate.Move;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static engine.helpers.GlobalHelper.*;

public class Renderer extends JPanel implements KeyListener {
    private Image[] pieceImagesT1 = new BufferedImage[TEAM_SIZE];
    private Image[] pieceImagesT2 = new BufferedImage[TEAM_SIZE];
    private final ImageIcon boardImage;
    Piece movingPiece;
    Engine engine;
    Coordinate previousPosition;
    private boolean canPaintMoves;
    Board b;
    ArrayList<Move> moveHistory;
    int count;

    public Renderer(Engine engine) {
        this.count = 0;
        this.moveHistory = new ArrayList<>();
        this.engine = engine;
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

        setFocusable(true); // Ensure this component can receive key events
        addKeyListener(this);

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
                        performMove(coordinate);
                        upgradePawn();
                        removeCheckFromSelf();
                        checkAndSetCheck();
                        switchPlayers();
                        moveHistory.add(new Move(movingPiece, previousPosition, movingPiece.getPosition(), count));
                    }else {
                        resetPiece();
                    }
                } else
                        resetPiece();
                canPaintMoves = false;
                repaint();


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

    public void removeCheckFromSelf() {
        Player p = b.getPlayer(movingPiece.getTeam());
        if (!p.isInCheck())
            return;

        p.setInCheck(false);
        p.setPiecesThatHaveCheck(new ArrayList<>());
    }

    private void performMove(Coordinate coordinate) {
        performCastle(coordinate); // Will move other piece involved in castle if castle otherwise will do nothing
        removeUniqueEvents(); // This removes castles/pawn double jump if piece is moved
        removePiece(coordinate); // If a piece is jumped then remove it
        movePiece(coordinate);
    }

    private boolean isInBoardBounds(MouseEvent e) {
        return movingPiece != null && e.getX() > 0 && e.getX() < BASE_WIDTH && e.getY() > 0 && e.getY() < BASE_HEIGHT;
    }

    private void switchPlayers(){
        engine.switchPlayers();
    }

    private void checkAndSetCheck(){
        engine.runPlayerChecks();
    }

    private void removePiece(Coordinate coordinate){
        Piece pieceToRemove = null;
        for (Piece piece : b.getPieces()) {
            if (piece.getPosition().positionEquals(coordinate) && movingPiece.getTeam() != piece.getTeam()) {
                pieceToRemove = piece;
                break;
            }
        }
        if (pieceToRemove != null)
            b.removePiece(pieceToRemove);

    }

    private void movePiece(Coordinate to) {
        movingPiece.setPosition(to);
        movingPiece.setDrawPosition(new Coordinate(convertToPixelX(to.getX(), 512), convertToPixelY(to.getY(), 512)));
    }

    private void performCastle(Coordinate coordinate) {
        Coordinate pieceMove = b.getPieceMove(coordinate, movingPiece);
        b.performCastle(pieceMove, movingPiece);
    }

    // This method is called to remove the double jump from pawns after they've been moved and
    // to remove the castling ability from the Rook/King.
    private void removeUniqueEvents() {
        if (movingPiece instanceof Pawn)
            ((Pawn) movingPiece).setIsFirstMove(false);
        if (movingPiece instanceof King)
            ((King) movingPiece).setFirstMove(false);
        if (movingPiece instanceof Rook)
            ((Rook) movingPiece).setFirstMove(false);
    }

    private void resetPiece() {
        movingPiece.setPosition(previousPosition);
        movingPiece.setDrawPosition(new Coordinate(convertToPixelX(previousPosition.getX(), 512), convertToPixelY(previousPosition.getY(), 512)));
    }

    private boolean isLegalMove(Coordinate coordinate) {
        ArrayList<Coordinate> moves = movingPiece.getMovesDeep(b);
        for (Coordinate m : moves){
            if (m.positionEquals(coordinate)) {
                return true;
            }
        }
        return false;
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
            ArrayList<Coordinate> movesList = movingPiece.getMovesDeep(b);
            for(Coordinate move : movesList){
                Coordinate drawPosition = movingPiece.getDrawPosition();
                if (move.positionEquals(new Coordinate(convertToColumn(drawPosition.getX() + SQUARE_SIZE / 2), convertToRow(drawPosition.getY() + SQUARE_SIZE / 2)))) {
                    g2d.setColor(new Color(255, 255, 0, 128));
                }else{
                    g2d.setColor(new Color(123,123,123));
                }
                g2d.fillRect(convertToPixelX(move.getX(), BASE_WIDTH), convertToPixelY(move.getY(), BASE_HEIGHT), SQUARE_SIZE, SQUARE_SIZE);
                g2d.setColor(Color.YELLOW);
                g2d.drawRect(convertToPixelX(move.getX(), BASE_WIDTH), convertToPixelY(move.getY(), BASE_HEIGHT), SQUARE_SIZE, SQUARE_SIZE);

            }
            g2d.setStroke(oldStroke);
        }
        if (b !=null) {
            for (Piece piece : b.getPieces()) {
                drawPiece(g, piece);
            }
        }

    }

    private void highlightKingIfCheck(Graphics g) {
        King checkedKing;
        if (b == null)
            return;
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
            performMove(to);
            upgradePawn();
            repaint();
            engine.switchPlayers();
        }
    }

    private void upgradePawn() {
        if (movingPiece.getClass() != Pawn.class)
            return;

        // We have to invert start position here since pawns will be on opposite side of board
        int rowPos = movingPiece.getTeam() == TEAM_ONE ? PIECE_START_TEAM_2 : PIECE_START_TEAM_1;

        if (movingPiece.getPosition().getY() != rowPos)
            return;

        if (movingPiece.isAI()){
            Random r = new Random();
            Class<? extends Piece> cl;
            Image sprite;
            int i = r.nextInt(4);
            switch (i) {
                case 0 : {
                    cl = Bishop.class;
                    sprite = movingPiece.getTeam() == TEAM_ONE ? pieceImagesT1[BISHOP] : pieceImagesT2[BISHOP];
                    break;
                }
                case 1 : {
                    cl = Knight.class;
                    sprite = movingPiece.getTeam() == TEAM_ONE ? pieceImagesT1[KNIGHT] : pieceImagesT2[KNIGHT];
                    break;
                }
                case 2 : {
                    cl = Queen.class;
                    sprite = movingPiece.getTeam() == TEAM_ONE ? pieceImagesT1[QUEEN] : pieceImagesT2[QUEEN];
                    break;
                }
                default: {
                    cl = Rook.class;
                    sprite = movingPiece.getTeam() == TEAM_ONE ? pieceImagesT1[ROOK] : pieceImagesT2[ROOK];
                }
            }
            setUpgradedPawn(cl, sprite);
            return;
        }

        engine.setUpgradePawnPanelIcons(b.getPlayer(movingPiece.getTeam()).getPieceImages());
        engine.setUpgradePawnPanelVisible(true);
    }

    public void setUpgradedPawn(Class<? extends Piece> cl, Image image){
        Player player = b.getPlayer(movingPiece.getTeam());
        b.convertPawn(player, (Pawn) movingPiece, cl, image);
        repaint();
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

    private Piece getPiece(Coordinate coordinate) {
        return b.getPieceByPosition(coordinate, engine.getPlayerTurn());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle key pressed event
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_P) {
           printAppDump(b.getPlayer(engine.getPlayerTurn()));
        }
        if (keyCode == KeyEvent.VK_H) {
            engine.hideEndGameDisplay();
        }
        if (keyCode == KeyEvent.VK_N){
            ArrayList<Piece> pieces =  b.getPlayer(engine.getPlayerTurn()).getPieces();
            ArrayList<Coordinate> moves = new ArrayList<>();
            for (Piece p : pieces){
                moves.addAll(p.getMovesDeep(b));
            }
            System.out.println(moves);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handle key released event
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key typed event
    }

    private void printAppDump(Player player) {
        System.out.println("======= Player " + player.getTeam() + "=======");
        System.out.println("inCheck: " + player.isInCheck());
        System.out.println("canMove: " + player.canMove());
        System.out.println("pieces: " + player.getPieces().size());
        ArrayList<Coordinate> moves = new ArrayList<>();
        for(Piece p : player.getPieces()){
            moves.addAll(p.getMovesDeep(b));
        }
        System.out.println("available moves: " + moves.size());
    }

    public void reset() {
        movingPiece = null; // Reset the currently moving piece
        previousPosition = null; // Reset the previous position
        canPaintMoves = false; // Reset the flag for painting move highlights
        b = null; // Reset the board reference
        moveHistory.clear(); // Clear the move history
        count = 0; // Reset any other game state variables
        repaint(); // Repaint the renderer to reflect the changes
    }
}