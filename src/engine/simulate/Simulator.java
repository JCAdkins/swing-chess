package engine.simulate;

import engine.pieces.Piece;
import engine.player.Player;
import engine.ui.Board;
import engine.ui.Coordinate;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.OFF_BOARD;
import static engine.helpers.GlobalHelper.TEAM_ONE;

public class Simulator {
    private Board simBoard;
    private final Board originalBoard;
    private final Player playerOne;
    private final Player playerTwo;

    public Simulator(Board b) {
        this.simBoard = new Board(b);
        this.originalBoard = new Board(b);
        this.playerOne = simBoard.getPlayerOne();
        this.playerTwo = simBoard.getPlayerTwo();
    }

    public void reset(){
        this.simBoard = new Board(originalBoard);
    }

    public boolean pieceMoveWillResultInCheck(Piece p) {
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
                if (opposingPiece.getMoves(simBoard, false).contains(kingPosition)) {
                    retVal = true;
                    break;
                }
            }
        }

        return retVal;
    }



    public boolean isInCheckAfterMove(Coordinate move, Piece movingPiece){
        Piece copyPiece = simBoard.getPiece(movingPiece);
        copyPiece.setPosition(move);
        Coordinate kingPosition = simBoard.getPlayer(movingPiece.getTeam()).getKing().getPosition();
        ArrayList<Piece> opposingPieces = simBoard.getOtherPlayer(movingPiece.getTeam()).getPieces();

        // If move will jump piece we need to remove that piece from the board
        opposingPieces.removeIf(p -> move.equals(p.getPosition()));

        ArrayList<Coordinate> opposingPieceMoves;
        for (Piece p : opposingPieces){
            opposingPieceMoves = p.getMoves(simBoard, false);
                if (opposingPieceMoves.contains(kingPosition))
                    return true;
        }
        return false;
    }

    public void removePieces(ArrayList<Piece> piecesToRemove){
        for (Piece piece : piecesToRemove){
            simBoard.removePiece(piece);
        }
    }

    public void runPlayerCheck(int team){
        if (team == TEAM_ONE)
            playerOne.runChecks(simBoard, false);
        else
            playerTwo.runChecks(simBoard, false);

    }

}
