package engine.ui;

import engine.pieces.Piece;
import engine.player.Player;
import java.util.ArrayList;
import java.util.Iterator;

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
        if (playerOne.getIsCheckMate())
            return TEAM_ONE;
        if (playerTwo.getIsCheckMate())
            return TEAM_TWO;
        if (!playersCanMove())
            return DRAW;
        return CONTINUE_GAME;
    }

    private boolean playersCanMove() {
        return playerOne.canMove() || playerTwo.canMove();
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
    public ArrayList<Piece> getT1Pieces() {
        ArrayList<Piece> list = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.getTeam() == playerOne.getTeam()) {
                list.add(piece);
            }
        }
        return list;
    }
    public ArrayList<Piece> getT2Pieces() {
        ArrayList<Piece> list = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.getTeam() == playerTwo.getTeam()) {
                list.add(piece);
            }
        }
        return list;
    }
}
