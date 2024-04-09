package engine.player;

import engine.hardware.pieces.Piece;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import engine.stockfish.AiDifficulty;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AI extends Player{

    private final AiDifficulty aiDifficulty;

    public AI(int team, Image[] pieceImages, AiDifficulty aiDifficulty) {
        super(team, pieceImages, true);
        this.aiDifficulty = aiDifficulty;
    }

    public AI(Player player) {
        super(player);
        this.aiDifficulty = ((AI) player).aiDifficulty;
    }

    public AiDifficulty getAiDifficulty() {
        return aiDifficulty;
    }

    /**
     *
     */
    @Override
    public ArrayList<Coordinate> generateSimpleAiMove(ArrayList<Piece> pieces, Board b) {
        ArrayList<Piece> pieces1 = new ArrayList<>();
        for(Piece piece : pieces){
            if (piece.canMove(b))
                pieces1.add(piece);
        }
        int size = pieces1.size();
        Random r = new Random();
        Piece pieceToGetMoveFrom = pieces1.get(r.nextInt(size));
        size = pieceToGetMoveFrom.getMovesDeep(b).size();
        Coordinate from = pieceToGetMoveFrom.getPosition();
        Coordinate to = pieceToGetMoveFrom.getMovesDeep(b).get(r.nextInt(size));
        ArrayList<Coordinate> retList = new ArrayList<>();
        retList.add(from);
        retList.add(to);
        return retList;
    }

    /**
     * @return
     */
    @Override
    public Player copy() {
        return new AI(this);
    }


    public AiDifficulty getDifficulty() {
        return aiDifficulty;
    }
}
