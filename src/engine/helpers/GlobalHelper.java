package engine.helpers;

import engine.ui.Coordinate;

public class GlobalHelper
{
    public static final int TEAM_ONE = 1;
    public static final int TEAM_TWO = -1;
    public static final int TEAM_SIZE = 6;
    public static final int BASE_WIDTH = 512;
    public static final int BASE_HEIGHT = 512;
    public static final int ROWS = 8;
    public static final int COLUMNS = 8;
    public static final int PAWN = 12;
    public static final int P1_KNIGHT = 5;
    public static final int P1_BISHOP = 1;
    public static final int P2_KNIGHT = 12;
    public static final int P2_BISHOP = 6;
    public static final int ROOK = 11;
    public static final int QUEEN = 10;
    public static final int KING = 8;

    public static Coordinate convertToPixelCoordinates(int x, int y, int boardWidth, int boardHeight) {
        // Calculate the width and height of each square on the board
        int squareWidth = boardWidth / COLUMNS;
        int squareHeight = boardHeight / ROWS;

        // Calculate the pixel coordinates of the center of the specified column and row
        int pixelX = x * squareWidth + squareWidth / 2;
        int pixelY = y * squareHeight + squareHeight / 2;

        // Return the pixel coordinates as a Coordinate object
        return new Coordinate(pixelX, pixelY);
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
        return y * squareHeight;
    }

    public static int convertToPixelXCursor(int x, int boardWidth) {
        // Calculate the width and height of each square on the board
        int squareWidth = boardWidth / COLUMNS;

        // Calculate the pixel coordinates of the center of the specified column and row

        // Return the pixel coordinates as a Coordinate object
        return x * squareWidth - squareWidth / 2;
    }

    public static int convertToPixelYCursor(int y, int boardHeight) {
        // Calculate the width and height of each square on the board
        int squareHeight = boardHeight / ROWS;

        // Return the pixel coordinates as a Coordinate object
        return y * squareHeight - squareHeight / 2;
    }
}
