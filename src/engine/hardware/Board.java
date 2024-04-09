package engine.hardware;

import engine.hardware.pieces.*;
import engine.player.Player;
import engine.ui.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static engine.helpers.GlobalHelper.*;

public class Board {
    private final ArrayList<Piece> pieces;
    private final Player playerOne;
    private final Player playerTwo;

    public Board( Renderer renderer, Player playerOne, Player playerTwo) {
        this.pieces = new ArrayList<>();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.pieces.addAll(playerOne.getPieces());
        this.pieces.addAll(playerTwo.getPieces());
        renderer.setBoard(this);
    }

    public Board(Board original) {
        this.pieces = new ArrayList<>();
        this.playerOne = original.playerOne.copy(); // Assuming Player subclasses implement a copy method
        this.playerTwo = original.playerTwo.copy(); // Assuming Player subclasses implement a copy method
        this.pieces.addAll(playerOne.getPieces());
        this.pieces.addAll(playerTwo.getPieces());
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

    public void removePiece(Piece deadPiece) {

        pieces.remove(deadPiece);
            deadPiece.remove();

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

    public void performCastle(Coordinate pieceMove, Piece p) {

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
            }else {
                p.setPosition(new Coordinate(ROOK_CASTLE_TWO_T1.getX(), p.getPosition().getY()));
                otherPiece.setPosition(new Coordinate(KING_CASTLE_TWO_T1.getX(), otherPiece.getPosition().getY()));
            }
            if (otherPiece instanceof King)
                ((King) otherPiece).setFirstMove(false);
            if (p instanceof  Rook)
                ((Rook) p).setFirstMove(false);
        }
    }

    public Coordinate getPieceMove(Coordinate coordinate, Piece p) {
        ArrayList<Coordinate> moves = p.getMovesShallow(this);
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

    public Piece getPieceByPosition(Coordinate coordinate, int team) {
        for (Piece p : pieces) {
            if (p.getPosition().positionEquals(coordinate) && p.getTeam() == team)
                return p;
        }
        return new Pawn(false, OFF_BOARD, OFF_BOARD, 0, new BufferedImage(1,1,1));
    }

    public void convertPawn(Player player, Pawn movingPiece, Class<? extends Piece> cl, Image image) {
        Piece newPiece = player.convertPawn(movingPiece, cl, image);
        removePiece(movingPiece);
        addPiece(player, newPiece);
    }

    private void addPiece(Player p, Piece newPiece) {
        p.addPiece(newPiece);
        pieces.add(newPiece);
    }

    public int getPlayerTurn() {
        return getPlayerOne().isPlayerTurn() ? TEAM_ONE : TEAM_TWO;
    }
}
