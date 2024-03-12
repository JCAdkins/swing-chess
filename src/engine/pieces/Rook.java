package engine.pieces;

import engine.player.Player;
import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.MovesHelper.addHorizontalAndVerticalMoves;

public class Rook extends Piece {
    private boolean canCastle;
    King king;

    public Rook(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.canCastle = true;
    }

    public Rook(Piece piece) {
        super(piece);
    }

    @Override
    public Piece copy() {
        return new Rook(this); // Assuming Bishop has a copy constructor
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(Board b, boolean check) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        if (check) {
            Player player = getPlayer(b);
            if (!player.isInCheck() && pieceCannotMove(b))
                return moves;
            if (player.isInCheck())
                return getAvailableMovesInCheck(b, moves);
        }
        addHorizontalAndVerticalMoves(moves, position, getTeam(), b);
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

    @Override
    public String toString(){
        return "Rook " + getTeam() + ": " + getPosition();
    }
}

