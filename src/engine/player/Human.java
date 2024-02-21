package engine.player;

import java.awt.*;

public class Human extends Player{

    public Human(int team, Image[] pieceImages, boolean isAI) {
        super(team, pieceImages, isAI);
    }

    /**
     *
     */
    @Override
    public void generateMove() {
        // Humans will make move through GUI no need to programmatically generate a move.
    }
}
