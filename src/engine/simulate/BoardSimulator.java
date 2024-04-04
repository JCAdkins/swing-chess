package engine.simulate;

import engine.hardware.pieces.Piece;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.util.ArrayList;

public class BoardSimulator {
    private Board simBoard;
    private final Board originalBoard;

    public BoardSimulator(Board b) {
        this.simBoard = new Board(b);
        this.originalBoard = new Board(b);
    }

    public void reset(){
        this.simBoard = new Board(originalBoard);
    }

    public boolean isInCheckAfterMove(Coordinate move, Piece movingPiece){
        Piece copyPiece = simBoard.getPiece(movingPiece);

        if (copyPiece != null) {
            if (move.getIsCastleMove()){
                // Get move from SimBoard so only simulated copies will be acted on,
                // not passed through references to the real chess board
                Coordinate m = getPieceMove(move, copyPiece);
                simBoard.performCastle(m, copyPiece);
            }
            else
                copyPiece.setPosition(move);

        }

        ArrayList<Piece> opposingPieces = simBoard.getOtherPlayer(movingPiece.getTeam()).getPieces();
        // If move will jump piece we need to remove that piece from the board
        opposingPieces.removeIf(piece ->  piece.getPosition().positionEquals(move));
        simBoard.getPieces().removeIf(piece ->  piece.getTeam() != movingPiece.getTeam() && piece.getPosition().positionEquals(move));
        Coordinate kingPosition = simBoard.getPlayer(movingPiece.getTeam()).getKing().getPosition();

        return piecesMovesContainsKing(opposingPieces, kingPosition);
    }

    private boolean piecesMovesContainsKing(ArrayList<Piece> opposingPieces, Coordinate kingPosition){
        for (Piece p : opposingPieces){
            for (Coordinate move : p.getMovesShallow(simBoard)){
                if (move.positionEquals(kingPosition))
                    return true;
            }
        }
        return false;
    }

    private Coordinate getPieceMove(Coordinate coordinate, Piece p) {
        ArrayList<Coordinate> moves = p.getMovesShallow(simBoard);
        for (Coordinate m : moves){
            if (m.equals(coordinate))
                return m;
        }
        return coordinate;
    }
}
