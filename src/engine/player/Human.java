package engine.player;

import engine.pieces.Piece;
import engine.ui.Board;
import engine.ui.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

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
