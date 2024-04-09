package engine.hardware.pieces;

import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.TEAM_ONE;
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
    ArrayList<Coordinate> addAllPossibleMoves(Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        addDiagonalMoves(moves, position, getTeam(),  b);
        addHorizontalAndVerticalMoves(moves, position, getTeam(), b);
        return moves;
    }

    @Override
    public String toString(){
        return "Queen " + getTeam() + ": " + getPosition();
    }

    /**
     * @return
     */
    @Override
    public char toLetter() {
        return getTeam() == TEAM_ONE ? 'Q' :'q';
    }
}
