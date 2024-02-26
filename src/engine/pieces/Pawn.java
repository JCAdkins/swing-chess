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
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y, Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        moves.add(new Coordinate(x-1, y-1));
        moves.add(new Coordinate(x, y-1));
        moves.add(new Coordinate(x+1, y-1));
        moves.add(new Coordinate(x-1, y));
        moves.add(new Coordinate(x+1, y));
        moves.add(new Coordinate(x-1, y+1));
        moves.add(new Coordinate(x, y+1));
        moves.add(new Coordinate(x+1, y+1));
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
