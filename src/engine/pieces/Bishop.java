package engine.pieces;

import engine.player.Player;
import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.MovesHelper.addDiagonalMoves;

public class Bishop extends Piece {


    public Bishop(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
    }

    public Bishop(Piece piece) {
        super(piece);
    }

    @Override
    public Piece copy() {
        return new Bishop(this); // Assuming Bishop has a copy constructor
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
        addDiagonalMoves(moves, position, getTeam(), b); // This is different per piece

        return moves;
    }

    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    @Override
    public String toString(){
        return "Bishop " + getTeam() + ": " + getPosition();
    }
}
