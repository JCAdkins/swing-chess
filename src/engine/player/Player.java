package engine.player;

import engine.pieces.*;
import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;
import static engine.helpers.GlobalHelper.*;

public abstract class Player {

    private ArrayList<Piece> pieces;
    private final King king;
    private final int team;
    private final boolean isAI;
    private boolean inCheck, canMove, isCheckMate;

    public Player(int team, Image[] pieceImages, boolean isAI) {
        this.pieces = new ArrayList<>();
        this.team = team;
        this.isAI = isAI;
        this.inCheck = false;
        this.canMove = true;
        this.isCheckMate = false;
        for(int i = 0; i < 8; i++){
            int PAWN_START = team == TEAM_ONE ? 1 : 6;
            pieces.add(new Pawn(isAI, new Coordinate(i, PAWN_START),  convertToPixelCoordinate(i, PAWN_START), team, pieceImages[PAWN]));
        }
        int PIECE_START = team == TEAM_ONE ? 0 : 7;
        Rook rookOne = new Rook(isAI, new Coordinate(ROOK_ONE_START, PIECE_START), convertToPixelCoordinate(ROOK_ONE_START, PIECE_START), team, pieceImages[ROOK]);
        Rook rookTwo = new Rook(isAI,new Coordinate(ROOK_TWO_START,PIECE_START),  convertToPixelCoordinate(ROOK_TWO_START, PIECE_START), team, pieceImages[ROOK]);
        this.king = new King(isAI,new Coordinate(KING_START,PIECE_START),  convertToPixelCoordinate(KING_START, PIECE_START), team, pieceImages[KING], rookOne, rookTwo);
        rookOne.addKing(this.king);
        rookTwo.addKing(this.king);
        pieces.add(rookOne);
        pieces.add(rookTwo);
        pieces.add(new Knight(isAI,new Coordinate(KNIGHT_ONE_START,PIECE_START), convertToPixelCoordinate(KNIGHT_ONE_START, PIECE_START), team, pieceImages[KNIGHT]));
        pieces.add(new Knight(isAI, new Coordinate(KNIGHT_TWO_START,PIECE_START),  convertToPixelCoordinate(KNIGHT_TWO_START, PIECE_START), team, pieceImages[KNIGHT]));
        pieces.add(new Bishop(isAI,new Coordinate(BISHOP_ONE_START,PIECE_START),  convertToPixelCoordinate(BISHOP_ONE_START, PIECE_START), team, pieceImages[BISHOP]));
        pieces.add(new Bishop(isAI,new Coordinate(BISHOP_TWO_START, PIECE_START),  convertToPixelCoordinate(BISHOP_TWO_START, PIECE_START), team, pieceImages[BISHOP]));
        pieces.add(new Queen(isAI,new Coordinate(QUEEN_START,PIECE_START),  convertToPixelCoordinate(QUEEN_START, PIECE_START), team, pieceImages[QUEEN]));
        pieces.add(this.king);


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

    public King getKing(){
        return this.king;
    }

    public boolean isAI() {
        return isAI;
    }

    public abstract ArrayList<Coordinate> generateMove(ArrayList<Piece> pieces, Board b);

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setIsCheckMate(boolean isCheckMate){
        this.isCheckMate = isCheckMate;
    }

    public boolean getIsCheckMate(){
        return isCheckMate;
    }

    public void runChecks(Board b) {
        inCheck = isKingChecked(b);
        for (Piece piece : pieces){
            if (!piece.getMoves(b).isEmpty()) {
                canMove = true;
                break;
            }
                canMove = false;
        }
    }

    private boolean isKingChecked(Board b) {
        Player otherPlayer = b.getOtherPlayer(team);
        ArrayList<Piece> opposingPieces = otherPlayer.getPieces();
        for (Piece piece : opposingPieces){
            ArrayList<Coordinate> pieceMoves = piece.getMoves(b);
            System.out.println(piece);
            for (Coordinate move : pieceMoves){
                if (king.getPosition().equals(move))
                    return true;
            }
        }
        return false;
    }
}
