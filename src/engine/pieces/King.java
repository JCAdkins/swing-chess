package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.*;

public class King extends Piece {
    private boolean canCastle;
    Rook rookOne;
    Rook rookTwo;

    public King(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite, Rook rookOne, Rook rookTwo) {
        super(isAI, position, drawPosition, team, sprite);
        this.canCastle = true;
        this.rookOne = rookOne;
        this.rookTwo = rookTwo;
    }

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


    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {
    }

    boolean canCastle(){
        return canCastle;
    }

    public void setCanCastle(boolean bool){
        this.canCastle = bool;
    }

    @Override
    public void addCastles(ArrayList<Coordinate> moves, Board b){
        if (canCastle && rookOne.canCastle())
            moves.add(rookOne.getPosition());
        if (canCastle && rookTwo.canCastle())
            moves.add(rookTwo.getPosition());
    }
}
