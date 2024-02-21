package engine.pieces;

import engine.ui.Coordinate;

import java.util.ArrayList;

public interface Movable {
    public void move();
    public ArrayList<Coordinate> getMoves();
}
