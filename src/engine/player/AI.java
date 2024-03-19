package engine.player;

import engine.hardware.pieces.Piece;
import engine.hardware.Board;
import engine.hardware.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AI extends Player{
    public AI(int team, Image[] pieceImages) {
        super(team, pieceImages, true);
    }

    public AI(Player player) {
        super(player);
    }

    /**
     *
     */
    @Override
    public ArrayList<Coordinate> generateMove(ArrayList<Piece> pieces, Board b) {
        ArrayList<Piece> pieces1 = new ArrayList<>();
        for(Piece piece : pieces){
            if (piece.canMove(b))
                pieces1.add(piece);
        }
        int size = pieces1.size();
        Random r = new Random();
        Piece pieceToGetMoveFrom = pieces1.get(r.nextInt(size));
        size = pieceToGetMoveFrom.getMovesDeep(b, true).size();
        Coordinate from = pieceToGetMoveFrom.getPosition();
        Coordinate to = pieceToGetMoveFrom.getMovesDeep(b,true).get(r.nextInt(size));
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
}
