package engine.ui;

import engine.pieces.Piece;
import java.util.ArrayList;

public class Board {
    private final ArrayList<Piece> pieces;

    public Board(ArrayList<Piece> pieces, Renderer renderer) {
        this.pieces = pieces;
        renderer.setPieces(pieces);
        renderer.setBoard(this);
    }

    public boolean checkGameStatus() {
        return true;
    }

    public void addPiece(Piece newPiece){
        pieces.add(newPiece);
    }

    public void removePiece(Piece deadPiece) {
        pieces.remove(deadPiece);
    }

    public ArrayList<Piece> getPieces() {
            return pieces;
    }
}
