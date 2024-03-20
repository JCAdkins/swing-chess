package engine.hardware.pieces;

import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean isFirstMove;

    public Pawn(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.isFirstMove = true;
    }

    public Pawn(Piece piece) {
        super(piece);
    }

    @Override
    public Piece copy() {
        return new Pawn(this); // Assuming Bishop has a copy constructor
    }

    public void setIsFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        boolean isValid = true, isDoubleValid = true, addTopLeft = false, addTopRight = false;
        int x = position.getX();
        int y = position.getY();

        for (Piece piece : b.getPieces()){
            if (piece.getPosition().equals(new Coordinate(x, y + getTeam())))
                isValid = false;
            if (piece.getPosition().equals(new Coordinate(x, y + 2 * getTeam())))
                isDoubleValid = false;
            if (piece.getPosition().equals(new Coordinate(x + 1, y + getTeam())))
                addTopRight = true;
            if (piece.getPosition().equals(new Coordinate(x - 1, y + getTeam())))
                addTopLeft = true;
        }
        if (isValid)
            moves.add(new Coordinate(x, y + getTeam()));
        if (addTopRight)
            moves.add(new Coordinate(x + 1, y + getTeam()));
        if (addTopLeft)
            moves.add(new Coordinate(x - 1, y + getTeam()));
        if (isFirstMove && isValid && isDoubleValid)
            moves.add(new Coordinate(x, y + 2 * getTeam()));
        return moves;
    }

    @Override
    public String toString(){
        return "Pawn " + getTeam() + ": " + getPosition();
    }
}
