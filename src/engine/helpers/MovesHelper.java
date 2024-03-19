package engine.helpers;

import engine.hardware.pieces.Piece;
import engine.hardware.Board;
import engine.hardware.Coordinate;

import java.util.ArrayList;

public class MovesHelper {
    public static void addDiagonalMoves(ArrayList<Coordinate> movesList, Coordinate pos, int team, Board b){
        Coordinate topLeft = null;
        Coordinate topRight = null;
        Coordinate bottomLeft = null;
        Coordinate bottomRight = null;

        int x = pos.getX();
        int y = pos.getY();

        for(int i = 1; i < 8; i++){
            if (topRight == null)
                topRight = checkCollision(x + i, y + i, team, b);
            if (bottomRight == null)
                bottomRight = checkCollision(x + i, y - i, team, b);
            if (topLeft == null)
                topLeft = checkCollision( x - i, y + i, team, b);
            if (bottomLeft == null)
                bottomLeft = checkCollision(x - i, y - i, team, b);
            if (topRight == null)
                movesList.add(new Coordinate(x + i, y + i));
            if (bottomRight == null)
                movesList.add(new Coordinate(x + i, y - i));
            if (topLeft == null)
                movesList.add(new Coordinate(x - i, y + i));
            if (bottomLeft == null)
                movesList.add(new Coordinate(x - i, y - i));
        }

        if (topLeft != null)
            movesList.add(topLeft);
        if (bottomLeft != null)
            movesList.add(bottomLeft);
        if (topRight != null)
            movesList.add(topRight);
        if (bottomRight != null)
            movesList.add(bottomRight);
    }

    public static void addHorizontalAndVerticalMoves(ArrayList<Coordinate> movesList, Coordinate pos, int team, Board b){
        Coordinate top = null;
        Coordinate bottom = null;
        Coordinate left = null;
        Coordinate right = null;

        int x = pos.getX();
        int y = pos.getY();

        for(int i = 1; i < 8; i++){
            if (top == null)
                top = checkCollision( x, y + i, team, b);
            if (bottom == null)
                bottom = checkCollision( x, y - i, team, b);
            if (left == null)
                left = checkCollision( x - i, y, team, b);
            if (right == null)
                right = checkCollision( x + i, y, team, b);
            if (top == null)
                movesList.add(new Coordinate(x, y + i));
            if (bottom == null)
                movesList.add(new Coordinate(x, y - i));
            if (left == null)
                movesList.add(new Coordinate(x - i, y));
            if (right == null)
                movesList.add(new Coordinate(x + i, y));
        }
        if (top != null)
            movesList.add(top);
        if (bottom != null)
            movesList.add(bottom);
        if (left != null)
            movesList.add(left);
        if (right != null)
            movesList.add(right);
    }

    private static Coordinate checkCollision(int x, int y, int team, Board b) {
        Coordinate position = new Coordinate(x, y);
        ArrayList<Piece> boardPieces = b.getPieces();
        for (Piece piece : boardPieces){
            if (piece.getPosition().equals(position)) {
                if (piece.getTeam() != team)
                    return position;
                else
                    return new Coordinate(-1, -1);
            }
        }
        return null;
    }
}
