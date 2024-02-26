package engine.player;

import engine.pieces.*;
import engine.ui.Coordinate;
import engine.helpers.GlobalHelper;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.TEAM_ONE;

public abstract class Player {

    private ArrayList<Piece> pieces;
    private final int team;
    private boolean inCheck;
    private final boolean isAI;

    public Player(int team, Image[] pieceImages, boolean isAI) {
        this.pieces = new ArrayList<>();
        this.team = team;
        this.inCheck = false;
        this.isAI = isAI;
        for(int i = 0; i < 8; i++){
            int PAWN_START = team == TEAM_ONE ? 1 : 6;
            pieces.add(new Pawn(isAI, new Coordinate(i, PAWN_START), new Coordinate(GlobalHelper.convertToPixelX(i, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PAWN_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.PAWN]));
        }
        int PIECE_START = team == TEAM_ONE ? 0 : 7;
        pieces.add(new Rook(isAI, new Coordinate(0, PIECE_START), new Coordinate(GlobalHelper.convertToPixelX(0, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.ROOK]));
        pieces.add(new Rook(isAI,new Coordinate(7,PIECE_START), new Coordinate(GlobalHelper.convertToPixelX(7, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.ROOK]));
        pieces.add(new Knight(isAI,new Coordinate(1,PIECE_START), new Coordinate(GlobalHelper.convertToPixelX(1, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.P1_KNIGHT]));
        pieces.add(new Knight(isAI, new Coordinate(6,PIECE_START), new Coordinate(GlobalHelper.convertToPixelX(6, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.P1_KNIGHT]));
        pieces.add(new Bishop(isAI,new Coordinate(2,PIECE_START), new Coordinate(GlobalHelper.convertToPixelX(2, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.P1_BISHOP]));
        pieces.add(new Bishop(isAI,new Coordinate(5, PIECE_START), new Coordinate(GlobalHelper.convertToPixelX(5, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.P1_BISHOP]));
        pieces.add(new Queen(isAI,new Coordinate(3,PIECE_START), new Coordinate(GlobalHelper.convertToPixelX(3, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.QUEEN]));
        pieces.add(new King(isAI,new Coordinate(4,4), new Coordinate(GlobalHelper.convertToPixelX(4, GlobalHelper.BASE_WIDTH), GlobalHelper.convertToPixelY(PIECE_START, GlobalHelper.BASE_HEIGHT)), team, pieceImages[GlobalHelper.KING]));


    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public int getTeam() {
        return team;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public void addPiece(Piece pieceToAdd) {
        this.pieces.add(pieceToAdd);
    }

    public void removePiece(Piece pieceToRemove) {
        this.pieces.remove(pieceToRemove);
    }

    public boolean isAI() {
        return isAI;
    }

    public abstract void generateMove();
}
