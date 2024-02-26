package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.MovesHelper.addDiagonalMoves;
import static engine.helpers.MovesHelper.addHorizontalAndVerticalMoves;

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
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y, Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        addDiagonalMoves(moves, x, y, b);
        addHorizontalAndVerticalMoves(moves, x,y,b);
        return moves;
    }

    /**
     * @param possibleMoves
     * @param b
     */
    @Override
    void removeIllegalMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    /**
     * @param possibleMoves
     * @param b
     */
    @Override
    void removeMovesPuttingPlayerInCheck(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    /**
     * @param possibleMoves
     * @param b
     */
    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    /**
     *
     */
    @Override
    public void move() {

    }
}
