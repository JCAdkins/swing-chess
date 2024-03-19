package engine.hardware;

import engine.hardware.pieces.*;
import engine.player.Player;
import engine.ui.Renderer;

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

    public Board(Board original) {
        this.pieces = new ArrayList<>();
        for (Piece piece : original.pieces) {
            this.pieces.add(piece.copy()); // Assuming each Piece subclass implements a copy method
        }

        this.playerOne = original.playerOne.copy(); // Assuming Player subclasses implement a copy method
        this.playerTwo = original.playerTwo.copy(); // Assuming Player subclasses implement a copy method
    }

    public int checkGameStatus(int playerTurn) {
        Player player = getPlayer(playerTurn);

        if (!player.canMove()) {
            if (player.isInCheck())
                return CHECK_MATE;
            else
                return DRAW;
        }
        return CONTINUE_GAME;
    }

    public Piece getPiece(Piece piece){
        for (Piece p : pieces){
            if (p.equals(piece))
                return p;
        }
        return null;
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

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
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


    public Player getOtherPlayer(int team) {
        return playerOne.getTeam() == team ? playerTwo : playerOne;
    }
    public Player getPlayer(int team) {
        return playerOne.getTeam() == team ? playerOne : playerTwo;
    }

    public void performCastle(Coordinate coordinate, Piece p) {
        Coordinate pieceMove = getPieceMove(coordinate, p);
        if (!pieceMove.getIsCastleMove())
            return;
        Piece otherPiece = pieceMove.getPieceInvolvedInCastle();
        int x = otherPiece.getPosition().getX();
        if (x == ROOK_ONE_START) {
            p.setPosition(new Coordinate(KING_CASTLE_ONE_T1.getX(), p.getPosition().getY()));
            otherPiece.setPosition(new Coordinate(ROOK_CASTLE_ONE_T1.getX(), otherPiece.getPosition().getY()));
            if (otherPiece instanceof Rook)
                ((Rook) otherPiece).setFirstMove(false);
            if (p instanceof  King)
                ((King) p).setFirstMove(false);
        } else if (x == ROOK_TWO_START){
            p.setPosition(new Coordinate(KING_CASTLE_TWO_T1.getX(), p.getPosition().getY()));
            otherPiece.setPosition(new Coordinate(ROOK_CASTLE_TWO_T1.getX(), otherPiece.getPosition().getY()));
            if (otherPiece instanceof Rook)
                ((Rook) otherPiece).setFirstMove(false);
            if (p instanceof  King)
                ((King) p).setFirstMove(false);
        }
        else {
            if (p.getPosition().getX() == ROOK_ONE_START){
                p.setPosition(new Coordinate(ROOK_CASTLE_ONE_T1.getX(), p.getPosition().getY()));
                otherPiece.setPosition(new Coordinate(KING_CASTLE_ONE_T1.getX(), otherPiece.getPosition().getY()));
                if (otherPiece instanceof King)
                    ((King) otherPiece).setFirstMove(false);
                if (p instanceof  Rook)
                    ((Rook) p).setFirstMove(false);
            }else {
                p.setPosition(new Coordinate(ROOK_CASTLE_TWO_T1.getX(), p.getPosition().getY()));
                otherPiece.setPosition(new Coordinate(KING_CASTLE_TWO_T1.getX(), otherPiece.getPosition().getY()));
                if (otherPiece instanceof King)
                    ((King) otherPiece).setFirstMove(false);
                if (p instanceof  Rook)
                    ((Rook) p).setFirstMove(false);
            }
        }
    }

    private Coordinate getPieceMove(Coordinate coordinate, Piece p) {
        ArrayList<Coordinate> moves = p.getMovesDeep(this,false);
        for (Coordinate m : moves){
            if (m.positionEquals(coordinate))
                return m;
        }
        return coordinate;
    }

    public ArrayList<Coordinate> getAllPiecePositions(){
        ArrayList<Coordinate> positionsList = new ArrayList<>();
        for (Piece p : pieces){
            positionsList.add(p.getPosition());
        }
        return positionsList;
    }

}
