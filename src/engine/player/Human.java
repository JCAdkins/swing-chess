package engine.player;

import engine.hardware.pieces.Piece;
import engine.hardware.Board;
import engine.hardware.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public class Human extends Player{

    public Human(int team, Image[] pieceImages) {
        super(team, pieceImages, false);
    }

    public Human(Player player) {
        super(player);
    }

    /**
     *
     */
    @Override
    public ArrayList<Coordinate> generateMove(ArrayList<Piece> pieces, Board b) {
        // Humans will make move through GUI no need to programmatically generate a move.
        return new ArrayList<>();
    }

    @Override
    public Player copy() {
        return new Human(this);
    }
}
