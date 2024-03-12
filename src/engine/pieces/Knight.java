package engine.pieces;

import engine.player.Player;
import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI,  position, drawPosition, team, sprite);
    }

    public Knight(Piece piece) {
        super(piece);
    }

    @Override
    public Piece copy() {
        return new Knight(this); // Assuming Bishop has a copy constructor
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

        int x = position.getX();
        int y = position.getY();

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

    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }

    @Override
    public String toString(){
        return "Knight " + getTeam() + ": " + getPosition();
    }
}
