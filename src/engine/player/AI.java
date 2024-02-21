package engine.player;

import java.awt.*;

public class AI extends Player{
    public AI(int team, Image[] pieceImages, boolean isAI) {
        super(team, pieceImages, isAI);
    }

    /**
     *
     */
    @Override
    public void generateMove() {
        System.out.println("Generating AI move...");
    }
}
