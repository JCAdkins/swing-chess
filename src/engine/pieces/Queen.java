package engine.pieces;

import engine.ui.Coordinate;
import engine.ui.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
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
     * that the Queen piece can move to.
     */
    @Override
    public ArrayList<Coordinate> getMoves() {
        return null;
    }
}
