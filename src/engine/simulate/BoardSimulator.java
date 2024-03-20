package engine.simulate;

import engine.hardware.pieces.King;
import engine.hardware.pieces.Piece;
import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.util.ArrayList;
import java.util.List;

import static engine.helpers.GlobalHelper.SWITCH_TEAM;
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

    public boolean isInCheckAfterMove(Coordinate move, Piece movingPiece){
        Coordinate kingPosition = movingPiece instanceof King ?
                move : simBoard.getPlayer(movingPiece.getTeam()).getKing().getPosition();
        Piece copyPiece = simBoard.getPiece(movingPiece);
        if (copyPiece != null)
            copyPiece.setPosition(move);

        // If move will jump piece we need to remove that piece from the board
        List<Piece> copyOpposingPieces = List.copyOf(simBoard.getOtherPlayer(movingPiece.getTeam()).getPieces());
        for (Piece p : copyOpposingPieces){
            if (p.getPosition().equals(move)) {
                simBoard.removePiece(p);
                break;
            }
        }

        if (movingPiece instanceof King && move.positionEquals(new Coordinate(2, 7)))
            System.out.println("...");

        ArrayList<Piece> opposingPieces = simBoard.getOtherPlayer(movingPiece.getTeam()).getPieces();

        return piecesMovesContainsKing(opposingPieces, kingPosition);
    }

    private boolean piecesMovesContainsKing(ArrayList<Piece> opposingPieces, Coordinate kingPosition){
        for (Piece p : opposingPieces){
            ArrayList<Coordinate> moves = p.getMovesShallow(simBoard);
            for (Coordinate move : p.getMovesShallow(simBoard)){
                if (move.positionEquals(kingPosition))
                    return true;
            }
        }
        return false;
    }

    public void runPlayerCheck(int team){
        if (team == TEAM_ONE)
            playerOne.runChecks(simBoard);
        else
            playerTwo.runChecks(simBoard);

    }
}
