package engine.hardware;

import engine.hardware.pieces.Pawn;
import engine.hardware.pieces.Piece;
import java.awt.image.BufferedImage;

import static engine.helpers.GlobalHelper.OFF_BOARD;

// No longer a coordinate class, more like a class representing a move
public class Coordinate {
    private int x;
    private int y;
    private boolean isCastleMove = false;
    private final Piece pieceInvolvedInCastle;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        this.isCastleMove = false;
        this.pieceInvolvedInCastle = new Pawn(false, OFF_BOARD, OFF_BOARD, 0, new BufferedImage(1,1,1));
    }

    public Coordinate(Coordinate c, boolean bool, Piece piece){
        this.x = c.getX();
        this.y = c.getY();
        this.isCastleMove = bool;
        this.pieceInvolvedInCastle = piece;
    }

    public Coordinate(int x, int y, boolean bool){
        this.x = x;
        this.y = y;
        this.isCastleMove = bool;
        this.pieceInvolvedInCastle = new Pawn(false, OFF_BOARD, OFF_BOARD, 0, new BufferedImage(1,1,1));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean positionEquals(Coordinate c){
        return this.x == c.getX() && this.y == c.getY();
    }

    public Piece getPieceInvolvedInCastle() {
        return pieceInvolvedInCastle;
    }

    public boolean getIsCastleMove() {
        return isCastleMove;
    }

    @Override
    public boolean equals(Object o) {
        // Check if the object being compared is the same instance as this object
        if (this == o) {
            return true;
        }

        // Check if the object being compared is null or not an instance of Coordinate
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // Cast the object to a Coordinate
        Coordinate otherCoordinate = (Coordinate) o;

        // Check if the x and y coordinates are equal
        return x == otherCoordinate.x && y == otherCoordinate.y && isCastleMove == otherCoordinate.isCastleMove ;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")" + "\nisCastleMove: " + isCastleMove + "\npieceInvolvedInCastleMove: " + pieceInvolvedInCastle;
    }

    public boolean isOnBoard() {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
}
