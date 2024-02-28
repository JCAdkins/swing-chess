package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.MovesHelper.addDiagonalMoves;
import static engine.helpers.MovesHelper.addHorizontalAndVerticalMoves;

public class Rook extends Piece {
    private boolean canCastle;
    King king;

    public Rook(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.canCastle = true;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    @Override
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y, Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        addHorizontalAndVerticalMoves(moves, x, y, getTeam(), b);
        return moves;
    }

    /**
     * @param possibleMoves
     * @param b
     */
    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    public void addKing(King king){
        this.king = king;
    }

    public boolean canCastle(){
        return canCastle;
    }

    @Override
    public void addCastles(ArrayList<Coordinate> moves, Board b){
        if (canCastle && king.canCastle())
            moves.add(king.getPosition());
    }

    public void setCanCastle(boolean bool){
        this.canCastle = bool;
    }
}
