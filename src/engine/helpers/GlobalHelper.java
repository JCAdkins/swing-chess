package engine.helpers;

import engine.pieces.Piece;
import engine.player.Player;
import engine.ui.Board;
import engine.ui.Coordinate;

import java.util.ArrayList;

public class GlobalHelper
{
    //=========================================
    //      Game condition variables
    public static final int CONTINUE_GAME = 0;
    public static final int DRAW = 3;
    public static final int SWITCH_TEAM = -1;
    public static final int TEAM_ONE = 1;
    public static final int TEAM_TWO = -1;
    //=========================================

    public static final int TEAM_SIZE = 6;
    public static final int SQUARE_SIZE = 64;
    public static final int BASE_WIDTH = 512;
    public static final int BASE_HEIGHT = 512;
    public static final int ROWS = 8;
    public static final int COLUMNS = 8;
    public static final Coordinate OFF_BOARD = new Coordinate(-100,-100);

    //==============================================
    //      Sprite image array index positions
    public static final int BISHOP = 1;
    public static final int KING = 8;
    public static final int KNIGHT = 5;
    public static final int PAWN = 12;
    public static final int QUEEN = 10;
    public static final int ROOK = 11;
    //==============================================

    //==============================================
    //          Piece board starts
    public static final int ROOK_ONE_START = 0;
    public static final int KNIGHT_ONE_START = 1;
    public static final int BISHOP_ONE_START = 2;
    public static final int QUEEN_START = 3;
    public static final int KING_START = 4;
    public static final int BISHOP_TWO_START = 5;
    public static final int KNIGHT_TWO_START = 6;
    public static final int ROOK_TWO_START = 7;
    //==============================================

    public static Coordinate convertToPixelCoordinate(int pieceStartX, int pieceStartY){
        return new Coordinate(convertToPixelX(pieceStartX, BASE_WIDTH), convertToPixelY(pieceStartY, BASE_HEIGHT));
    }

    public static int convertToPixelX(int x, int boardWidth) {
        // Calculate the width and height of each square on the board
        int squareWidth = boardWidth / COLUMNS;

        // Calculate the pixel coordinates of the center of the specified column and row

        // Return the pixel coordinates as a Coordinate object
        return x * squareWidth;
    }

    public static int convertToPixelY(int y, int boardHeight) {
        // Calculate the width and height of each square on the board
        int squareHeight = boardHeight / ROWS;
        // Return the pixel coordinates as a Coordinate object
        return (7 - y) * squareHeight;
    }
}
