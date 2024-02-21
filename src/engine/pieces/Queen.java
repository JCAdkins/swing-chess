package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
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
     * that the Queen piece can move to.
     */
    @Override
    public ArrayList<Coordinate> getMoves(Board b) {
        return null;
    }
}
