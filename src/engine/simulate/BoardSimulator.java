package engine.simulate;

import engine.hardware.pieces.King;
import engine.hardware.pieces.Piece;
import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.OFF_BOARD;
import static engine.helpers.GlobalHelper.TEAM_ONE;

public class BoardSimulator {
    private Board simBoard;
    private final Board originalBoard;
    private final Player playerOne;
    private final Player playerTwo;

    public BoardSimulator(Board b) {
        this.simBoard = new Board(b);
        this.originalBoard = new Board(b);
        this.playerOne = simBoard.getPlayerOne();
        if (playerOne.getKing().getPosition().getX() == 5)
            System.out.println("..");
        this.playerTwo = simBoard.getPlayerTwo();
    }

    public void reset(){
        this.simBoard = new Board(originalBoard);
    }

    public boolean movingThePieceWillResultInCheck(Piece p) {
        Piece copyPiece = null;
        boolean retVal = false;

        for (Piece piece : simBoard.getPieces()){
            if (piece.equals(p)){
                copyPiece = piece;
                break;
            }
        }

        if (copyPiece != null) {
            copyPiece.setPosition(OFF_BOARD);
            Player opposingPlayer = simBoard.getOtherPlayer(copyPiece.getTeam());
            Coordinate kingPosition = simBoard.getPlayer(copyPiece.getTeam()).getKing().getPosition();

            for (Piece opposingPiece : opposingPlayer.getPieces()) {
                if (opposingPiece.getMovesDeep(simBoard, false).contains(kingPosition)) {
                    retVal = true;
                    break;
                }
            }
        }

        return retVal;
    }



    public boolean isInCheckAfterMove(Coordinate move, Piece movingPiece){
        Coordinate kingPosition = movingPiece instanceof King ?
                move : simBoard.getPlayer(movingPiece.getTeam()).getKing().getPosition();
        Piece copyPiece = simBoard.getPiece(movingPiece);
        if (copyPiece != null)
            copyPiece.setPosition(move);
        ArrayList<Piece> opposingPieces = simBoard.getOtherPlayer(movingPiece.getTeam()).getPieces();

        // If move will jump piece we need to remove that piece from the board
        opposingPieces.removeIf(p -> p.getPosition().equals(move));

        return piecesMovesContainsKing(opposingPieces, kingPosition);
    }

    public void removePieces(ArrayList<Piece> piecesToRemove){
        for (Piece piece : piecesToRemove){
            simBoard.removePiece(piece);
        }
    }

    private boolean piecesMovesContainsKing(ArrayList<Piece> opposingPieces, Coordinate kingPosition){
        for (Piece p : opposingPieces){
            if (p.getMovesDeep(simBoard, false).contains(kingPosition))
                return true;
        }
        return false;
    }

    public void runPlayerCheck(int team){
        if (team == TEAM_ONE)
            playerOne.runChecks(simBoard, false);
        else
            playerTwo.runChecks(simBoard, false);

    }

    public boolean kingIsMovingIntoCheck(Coordinate move, King king) {
        boolean retVal = false;
        Piece copyKing = simBoard.getPiece(king);

        if (copyKing != null) {
            copyKing.setPosition(move);
            Player opposingPlayer = simBoard.getOtherPlayer(copyKing.getTeam());
            Coordinate kingPosition = copyKing.getPosition();

            for (Piece opposingPiece : opposingPlayer.getPieces()) {
                if (opposingPiece.getMovesDeep(simBoard, false).contains(kingPosition)) {
                    retVal = true;
                    break;
                }
            }
        }
        return retVal;
    }
}
