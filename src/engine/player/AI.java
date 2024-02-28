package engine.player;

import engine.pieces.Piece;
import engine.ui.Board;
import engine.ui.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AI extends Player{
    public AI(int team, Image[] pieceImages, boolean isAI) {
        super(team, pieceImages, isAI);
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
        System.out.println(pieceToGetMoveFrom);

        size = pieceToGetMoveFrom.getMoves(b).size();
        Coordinate from = pieceToGetMoveFrom.getPosition();
        Coordinate to = pieceToGetMoveFrom.getMoves(b).get(r.nextInt(size));
        ArrayList<Coordinate> retList = new ArrayList<>();
        retList.add(from);
        retList.add(to);
        return retList;
    }
}
