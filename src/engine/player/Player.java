package engine.player;

import engine.GlobalVars;
import engine.pieces.*;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;

public abstract class Player {

    private ArrayList<Piece> pieces;
    private int team;
    private boolean inCheck;
    private boolean isAI;

    public Player(int team, Image[] pieceImages, boolean isAI) {
        this.pieces = new ArrayList<>();
        this.team = team;
        this.inCheck = false;
        this.isAI = isAI;
        for(int i = 0; i < 8; i++){
            int PAWN_START = team == 1 ? 1 : 6;
            pieces.add(new Pawn(isAI, new Coordinate(i, PAWN_START), new Coordinate(GlobalVars.convertToPixelX(i, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PAWN_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.PAWN]));
        }
        int PIECE_START = team == 1 ? 0 : 7;
        pieces.add(new Rook(isAI, new Coordinate(0, PIECE_START), new Coordinate(GlobalVars.convertToPixelX(0, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.ROOK]));
        pieces.add(new Rook(isAI,new Coordinate(7,PIECE_START), new Coordinate(GlobalVars.convertToPixelX(7, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.ROOK]));
        pieces.add(new Knight(isAI,new Coordinate(1,PIECE_START), new Coordinate(GlobalVars.convertToPixelX(1, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.P1_KNIGHT]));
        pieces.add(new Knight(isAI, new Coordinate(6,PIECE_START), new Coordinate(GlobalVars.convertToPixelX(6, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.P1_KNIGHT]));
        pieces.add(new Bishop(isAI,new Coordinate(2,PIECE_START), new Coordinate(GlobalVars.convertToPixelX(2, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.P1_BISHOP]));
        pieces.add(new Bishop(isAI,new Coordinate(5, PIECE_START), new Coordinate(GlobalVars.convertToPixelX(5, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.P1_BISHOP]));
        pieces.add(new Queen(isAI,new Coordinate(3,PIECE_START), new Coordinate(GlobalVars.convertToPixelX(3, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.QUEEN]));
        pieces.add(new King(isAI,new Coordinate(4,4), new Coordinate(GlobalVars.convertToPixelX(4, GlobalVars.BASE_WIDTH), GlobalVars.convertToPixelY(PIECE_START, GlobalVars.BASE_HEIGHT)), team, pieceImages[GlobalVars.KING]));


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
