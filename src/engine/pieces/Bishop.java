package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;
import static engine.helpers.MovesHelper.addDiagonalMoves;

public class Bishop extends Piece {


    public Bishop(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
    }

    /**
     * @param x - x coordinate of piece
     * @param y- y coordinate of piece
     * @return - returns a list of all possible legal and illegal moves the piece can make
     */
    @Override
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y, Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        addDiagonalMoves(moves, x, y, getTeam(), b);

        return moves;
    }

    /**
     * @param possibleMoves
     * @param b
     */
    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    @Override
    public String toString(){
        return "Bishop " + getTeam() + ": " + getPosition();
    }
}
