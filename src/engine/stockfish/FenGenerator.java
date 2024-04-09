package engine.stockfish;

import engine.hardware.Board;
import engine.hardware.pieces.Piece;

import java.util.Arrays;

import static engine.helpers.GlobalHelper.OFF_BOARD;
import static engine.helpers.GlobalHelper.TEAM_ONE;

public class FenGenerator {
    private final char[][] charBoard;

    private void setBoard(Board board) {
        for(Piece p: board.getPieces()){
            if (p.getPosition().positionEquals(OFF_BOARD))
                continue;
            else {
                int x = p.getPosition().getX();
                int y = p.getPosition().getY();
                this.charBoard[y][x] = p.toLetter();
            }
        }
    }

    public FenGenerator() {
        this.charBoard = new char[8][8];
        for(int row = 0; row < 8; row++){
            Arrays.fill(this.charBoard[row], ' ');
        }
    }

    public String generate(Board board){
        setBoard(board);
        StringBuilder fen = new StringBuilder();

        // Convert each row of the board
        for (int row = 0; row < 8; row++) {
            int emptySquares = 0;

            // Convert each column of the row
            for (int col = 0; col < 8; col++) {
                char piece = charBoard[row][col];

                if (piece == ' ') {
                    emptySquares++;
                } else {
                    if (emptySquares > 0) {
                        fen.append(emptySquares);
                        emptySquares = 0;
                    }
                    fen.append(piece);
                }
            }

            if (emptySquares > 0)
                fen.append(emptySquares);

            // Add a separator between rows, except for the last row
            if (row < 7)
                fen.append('/');
        }

        if(board.getPlayerTurn() == TEAM_ONE)
            fen.append(" w");
        else
            fen.append(" b");

        if(board.getPlayerOne().getKing().isFirstMove()) {
            if (board.getPlayerOne().getKing().getRookTwo().isFirstMove())
                fen.append(" K");
            else
                fen.append(" -");
            if (board.getPlayerOne().getKing().getRookOne().isFirstMove())
                fen.append("Q");
            else
                fen.append("-");
        } else
            fen.append(" --");

        if(board.getPlayerTwo().getKing().isFirstMove()) {
            if (board.getPlayerTwo().getKing().getRookTwo().isFirstMove())
                fen.append("k");
            else
                fen.append("-");
            if (board.getPlayerOne().getKing().getRookOne().isFirstMove())
                fen.append("q");
            else
                fen.append("-");
        } else
            fen.append("--");

        fen.append(" - 10 10");


        return fen.toString();
    }
}
