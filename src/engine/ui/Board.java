package engine.ui;

import engine.pieces.Piece;
import engine.player.Player;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.*;

public class Board {
    private final ArrayList<Piece> pieces;
    private final Player playerOne;
    private final Player playerTwo;

    public Board(ArrayList<Piece> pieces, Renderer renderer, Player playerOne, Player playerTwo) {
        this.pieces = pieces;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        renderer.setPieces(pieces);
        renderer.setBoard(this);
    }

    public int checkGameStatus() {
        if (playerInCheckmate() == TEAM_ONE || playerInCheckmate() == TEAM_TWO)
            return playerInCheckmate();
        if (playerCannotMove())
            return DRAW;
        return CONTINUE_GAME;
    }

    private boolean playerCannotMove() {
        return false;
    }

    private int playerInCheckmate() {
        return CONTINUE_GAME;
    }

    public void addPiece(Piece newPiece){
        pieces.add(newPiece);
    }

    public void removePiece(Piece deadPiece) {

        pieces.remove(deadPiece);
        if (deadPiece.getTeam() == TEAM_ONE)
            playerOne.removePiece(deadPiece);
        else
            playerTwo.removePiece(deadPiece);
    }

    public ArrayList<Piece> getPieces() {
            return pieces;
    }
}
