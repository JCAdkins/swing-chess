package engine.helpers;

import engine.pieces.Piece;
import engine.ui.Board;
import engine.ui.Coordinate;

import java.util.ArrayList;

public class MovesHelper {
    public static void addDiagonalMoves(ArrayList<Coordinate> movesList, int x, int y, Board b){
        boolean collisionTopRight = false;
        boolean collisionTopLeft = false;
        boolean collisionBottomRight = false;
        boolean collisionBottomLeft = false;

        for(int i = 1; i < 7; i++){
            if (!collisionTopRight)
                collisionTopRight = checkCollision(x + i, y + i, b);
            if (!collisionBottomRight)
                collisionBottomRight = checkCollision(x + i, y - i, b);
            if (!collisionTopLeft)
                collisionTopLeft = checkCollision( x - i, y + i, b);
            if (!collisionBottomLeft)
                collisionBottomLeft = checkCollision(x - i, y - i, b);
            if (!collisionTopRight)
                movesList.add(new Coordinate(x + i, y + i));
            if (!collisionBottomRight)
                movesList.add(new Coordinate(x + i, y - i));
            if (!collisionTopLeft)
                movesList.add(new Coordinate(x - i, y + i));
            if (!collisionBottomLeft)
                movesList.add(new Coordinate(x - i, y - i));
        }
    }

    public static void addHorizontalAndVerticalMoves(ArrayList<Coordinate> movesList, int x, int y, Board b){
        boolean collisionTop = false;
        boolean collisionBottom = false;
        boolean collisionRight = false;
        boolean collisionLeft = false;

        for(int i = 1; i < 7; i++){
            if (!collisionTop)
                collisionTop = checkCollision( x, y + i, b);
            if (!collisionBottom)
                collisionBottom = checkCollision( x, y - i, b);
            if (!collisionLeft)
                collisionLeft = checkCollision( x - i, y, b);
            if (!collisionRight)
                collisionRight = checkCollision( x + i, y, b);
            if (!collisionTop)
                movesList.add(new Coordinate(x, y + i));
            if (!collisionBottom)
                movesList.add(new Coordinate(x, y - i));
            if (!collisionLeft)
                movesList.add(new Coordinate(x - i, y));
            if (!collisionRight)
                movesList.add(new Coordinate(x + i, y));
        }
    }

    private static boolean checkCollision(int x, int y, Board b) {
        Coordinate position = new Coordinate(x, y);
        ArrayList<Piece> boardPieces = b.getPieces();
        for (Piece piece : boardPieces){
            if (piece.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }
}
