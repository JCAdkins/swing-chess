package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI,  position, drawPosition, team, sprite);
    }

    /**
     * @param x
     * @param y
     * @return
     */
    @Override
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y, Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        moves.add(new Coordinate(x + 1, y + 2));
        moves.add(new Coordinate(x + 1, y - 2));
        moves.add(new Coordinate(x - 1, y + 2));
        moves.add(new Coordinate(x - 1, y - 2));
        moves.add(new Coordinate(x + 2, y + 1));
        moves.add(new Coordinate(x + 2, y - 1));
        moves.add(new Coordinate(x - 2, y + 1));
        moves.add(new Coordinate(x - 2, y - 1));
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
        return "Knight " + getTeam() + ": " + getPosition();
    }
}
