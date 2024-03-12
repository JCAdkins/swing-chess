package engine.player;

import engine.pieces.*;
import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static engine.helpers.GlobalHelper.*;

public abstract class Player {

    private ArrayList<Piece> pieces;

    ArrayList<Piece> piecesThatHaveCheck;
    private King king = null;
    private final int team;
    private final boolean isAI;
    private boolean inCheck, canMove, isCheckMate;
    private final Image[] pieceImages;

    public Player(int team, Image[] pieceImages, boolean isAI) {
        this.pieces = new ArrayList<>();
        this.piecesThatHaveCheck = new ArrayList<>();
        this.pieceImages = pieceImages;
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

    public Player(Player player) {
        ArrayList<Piece> copyPieces = new ArrayList<>();
        this.piecesThatHaveCheck = new ArrayList<>();
        for (Piece piece : player.getPieces()){
            Piece p = null;
            if (piece instanceof Bishop) {
                p = new Bishop(piece);
                copyPieces.add(p);
            }
            if (piece instanceof King) {
                p = new King(piece);
                copyPieces.add(p);
            }
            if (piece instanceof Knight) {
                p = new Knight(piece);
                copyPieces.add(p);
            }
            if (piece instanceof Pawn) {
                p = new Pawn(piece);
                copyPieces.add(p);
            }
            if (piece instanceof Queen) {
                copyPieces.add(new Queen(piece));
            }
            if (piece instanceof Rook) {
                copyPieces.add(new Rook(piece));
            }
            if (p != null && p.isHasCheck())
                this.piecesThatHaveCheck.add(piece);
        }

        this.pieces = copyPieces;
        this.pieceImages = Arrays.copyOf(player.getPieceImages(), player.getPieceImages().length);
        this.team = player.getTeam();
        this.isAI = player.isAI();
        this.inCheck = player.isInCheck();
        this.canMove = player.canMove;
        this.isCheckMate = player.isCheckMate;

        for (Piece piece : this.pieces) {
            if (piece instanceof King && piece.getPosition().equals(player.king.getPosition())) {
                this.king = (King) piece;
                break;
            }
        }


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

    public Image[] getPieceImages() {
        return pieceImages;
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


    public boolean getIsCheckMate(){
        return isCheckMate;
    }

    public void runChecks(Board b, boolean check) {
        inCheck = isKingChecked(b, check);
        king.setInCheck(inCheck);
        canMove = canPlayerMove(b);
        isCheckMate = isPlayerMated(b);
    }

    private boolean isPlayerMated(Board b) {
        return false;
    }

    private boolean canPlayerMove(Board b) {
        for (Piece piece : pieces){
            if (!piece.getMoves(b,true).isEmpty()) {
                return true;
            }
        }
            return false;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public boolean isCheckMate() {
        return isCheckMate;
    }

    public ArrayList<Piece> getPiecesThatHaveCheck() {
        return piecesThatHaveCheck;
    }

    public void setPiecesThatHaveCheck(ArrayList<Piece> list){
        this.piecesThatHaveCheck = list;
    }

    private boolean isKingChecked(Board b, Boolean check) {
        Player otherPlayer = b.getOtherPlayer(team);
        ArrayList<Piece> opposingPieces = otherPlayer.getPieces();
        for (Piece piece : opposingPieces){
            ArrayList<Coordinate> pieceMoves = piece.getMoves(b,check); //true?
            for (Coordinate move : pieceMoves){
                if (king.getPosition().equals(move)) {
                    this.piecesThatHaveCheck.add(piece);
                    piece.setHasCheck(true);
                }
            }
        }
        return !piecesThatHaveCheck.isEmpty();
    }

    public abstract Player copy();
}
