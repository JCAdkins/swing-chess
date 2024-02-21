package engine.pieces;

import engine.ui.Coordinate;
import engine.ui.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Pawn extends Piece implements Movable {
    private boolean isFirstMove;

    public Pawn(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.isFirstMove = true;
    }

    /**
     *
     */
    @Override
    public void move() {

    }

    /**
     * @return Returns a list of Coordinates that are legal board positions
     * that the Pawn piece can move to.
     */
    @Override
    public ArrayList<Coordinate> getMoves() {
        return null;
    }
}
