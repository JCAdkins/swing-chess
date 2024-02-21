package engine.pieces;

import engine.ui.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
    }

    /**
     *
     */
    @Override
    public void move() {

    }

    /**
     * @return Returns a list of Coordinates that are legal board positions
     * that the Rook piece can move to.
     */
    @Override
    public ArrayList<Coordinate> getMoves() {
        return null;
    }
}
