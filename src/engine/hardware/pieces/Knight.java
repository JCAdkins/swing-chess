package engine.hardware.pieces;

import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.TEAM_ONE;

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
    ArrayList<Coordinate> addAllPossibleMoves(Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();

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
    public String toString(){
        return "Knight " + getTeam() + ": " + getPosition();
    }

    /**
     * @return
     */
    @Override
    public char toLetter() {
        return getTeam() == TEAM_ONE ? 'N' :'n';
    }
}
