package engine.hardware.pieces;

import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.MovesHelper.addDiagonalMoves;
import static engine.helpers.MovesHelper.addHorizontalAndVerticalMoves;

public class Queen extends Piece {

    public Queen(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
    }

    public Queen(Piece piece) {
        super(piece);
    }

    @Override
    public Piece copy() {
        return new Queen(this); // Assuming Bishop has a copy constructor
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(Board b, boolean check) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        if (check) {
            Player player = getPlayer(b);
            if (!player.isInCheck() && pieceMoveWillResultInCheck(b))
                return moves;
            if (player.isInCheck())
                return getAvailableMovesInCheck(b, moves);
        }
        addDiagonalMoves(moves, position, getTeam(),  b);
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

    @Override
    public String toString(){
        return "Queen " + getTeam() + ": " + getPosition();
    }
}
