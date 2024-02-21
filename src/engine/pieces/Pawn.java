package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean isFirstMove;

    public Pawn(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.isFirstMove = true;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    @Override
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y) {
        return null;
    }

    /**
     * @param possibleMoves
     * @param b
     */
    @Override
    void removeIllegalMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    /**
     *
     */
    @Override
    public void move() {

    }

    /**
     * @return Returns a list of Coordinates that are legal board positions
     * that the Pawn piece can move to.
     */
    @Override
    public ArrayList<Coordinate> getMoves(Board b) {
        return null;
    }
}
