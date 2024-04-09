package engine.hardware.pieces;

import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import engine.simulate.BoardSimulator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static engine.helpers.GlobalHelper.TEAM_ONE;
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
    ArrayList<Coordinate> addAllPossibleMoves(Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        addDiagonalMoves(moves, position, getTeam(), b); // This is different per piece
        return moves;
    }

    /**
     * @return
     */
    @Override
    public char toLetter() {
        return getTeam() == TEAM_ONE ? 'B' :'b';
    }

    @Override
    public String toString(){
        return "Bishop " + getTeam() + ": " + getPosition();
    }
}
