package engine.player;

import engine.hardware.pieces.*;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static engine.helpers.GlobalHelper.*;

public abstract class Player {

    private ArrayList<Piece> pieces;

    ArrayList<Piece> piecesThatHaveCheck;
    private final int team;
    private final boolean isAI;
    private boolean inCheck, canMove, isPlayerTurn;
    private final Image[] pieceImages;

    public Player(int team, Image[] pieceImages, boolean isAI) {
        this.pieces = new ArrayList<>();
        this.piecesThatHaveCheck = new ArrayList<>();
        this.pieceImages = pieceImages;
        this.team = team;
        this.isAI = isAI;
        this.inCheck = false;
        this.canMove = true;
        int PIECE_START = team == TEAM_ONE ? PIECE_START_TEAM_1 : PIECE_START_TEAM_2;
        int PAWN_START = team == TEAM_ONE ? PAWN_START_TEAM_1 : PAWN_START_TEAM_2;
        for(int i = 0; i < 8; i++){
            Pawn pawn = new Pawn(isAI, new Coordinate(i, PAWN_START),  convertToPixelCoordinate(i, PAWN_START), team, pieceImages[PAWN]);
            pieces.add(pawn);
        }
        Rook rookOne = new Rook(isAI, new Coordinate(ROOK_ONE_START, PIECE_START), convertToPixelCoordinate(ROOK_ONE_START, PIECE_START), team, pieceImages[ROOK]);
        Rook rookTwo = new Rook(isAI,new Coordinate(ROOK_TWO_START,PIECE_START),  convertToPixelCoordinate(ROOK_TWO_START, PIECE_START), team, pieceImages[ROOK]);
        pieces.add(rookOne);
        pieces.add(rookTwo);
        pieces.add(new Knight(isAI,new Coordinate(KNIGHT_ONE_START,PIECE_START), convertToPixelCoordinate(KNIGHT_ONE_START, PIECE_START), team, pieceImages[KNIGHT]));
        pieces.add(new Knight(isAI, new Coordinate(KNIGHT_TWO_START,PIECE_START),  convertToPixelCoordinate(KNIGHT_TWO_START, PIECE_START), team, pieceImages[KNIGHT]));
        pieces.add(new Bishop(isAI,new Coordinate(BISHOP_ONE_START,PIECE_START),  convertToPixelCoordinate(BISHOP_ONE_START, PIECE_START), team, pieceImages[BISHOP]));
        pieces.add(new Bishop(isAI,new Coordinate(BISHOP_TWO_START, PIECE_START),  convertToPixelCoordinate(BISHOP_TWO_START, PIECE_START), team, pieceImages[BISHOP]));
        pieces.add(new Queen(isAI,new Coordinate(QUEEN_START,PIECE_START),  convertToPixelCoordinate(QUEEN_START, PIECE_START), team, pieceImages[QUEEN]));
        King king = new King(isAI,new Coordinate(KING_START,PIECE_START),  convertToPixelCoordinate(KING_START, PIECE_START), team, pieceImages[KING], rookOne, rookTwo);
        rookOne.addKing(king);
        rookTwo.addKing(king);
        pieces.add(king);
    }

    public Player(Player player) {
        Rook rookOne = null, rookTwo = null;
        this.pieces = new ArrayList<>();
        this.piecesThatHaveCheck = new ArrayList<>();
        for (Piece piece : player.getPieces()){
            Piece p = piece.copy();
            this.pieces.add(p);
            if (p instanceof Rook)
                if (rookOne == null)
                    rookOne = (Rook) p;
                else if (rookTwo == null)
                    rookTwo = (Rook) p;
        }
        King king = null;
        for (Piece p : pieces){
            if (p instanceof King){
                if (((King) p).getRookOne().getPosition().isOnBoard())
                    ((King) p).setRookOne(rookOne);
                if (((King) p).getRookTwo().getPosition().isOnBoard())
                    ((King) p).setRookTwo(rookTwo);

                king = (King) p;
                break;
            }
        }
        for (Piece p : pieces){
            if (p instanceof Rook){
                ((Rook) p).setKing(king);
                break;
            }
        }
        this.pieceImages = Arrays.copyOf(player.getPieceImages(), player.getPieceImages().length);
        this.team = player.team;
        this.isAI = player.isAI;
        this.inCheck = player.inCheck;
        this.canMove = player.canMove;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public Piece convertPawn(Pawn pawn, Class<? extends Piece> c, Image img){
        Piece newPiece = pawn.convertToNewPiece(c, img);
        pieces.remove(pawn);
        pieces.add(newPiece);
        return newPiece;
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

    public King getKing(){
        for (Piece p : pieces){
            if (p instanceof King)
                return (King) p;
        }
        return null;
    }

    public boolean isAI() {
        return isAI;
    }

    public abstract ArrayList<Coordinate> generateMove(ArrayList<Piece> pieces, Board b);

    public boolean canMove() {
        return canMove;
    }

    public void runChecks(Board b) {
        inCheck = isKingChecked(b);
        canMove = canPlayerMove(b);
    }

    private boolean canPlayerMove(Board b) {
        for (Piece piece : pieces){
            if (!piece.getMovesDeep(b).isEmpty())
                return true;
        }
            return false;
    }

    public void setPiecesThatHaveCheck(ArrayList<Piece> list){
        this.piecesThatHaveCheck = list;
    }

    private boolean isKingChecked(Board b) {
        Player otherPlayer = b.getOtherPlayer(team);
        Coordinate kingPosition = b.getPlayer(team).getKing().getPosition();
        ArrayList<Piece> opposingPieces = otherPlayer.getPieces();
        for (Piece piece : opposingPieces){
            ArrayList<Coordinate> pieceMoves = piece.getMovesShallow(b);
            for (Coordinate move : pieceMoves){
                if (kingPosition.positionEquals(move)) {
                    this.piecesThatHaveCheck.add(piece);
                }
            }
        }
        return !piecesThatHaveCheck.isEmpty();
    }

    public abstract Player copy();

    public void addPiece(Piece newPiece) {
        pieces.add(newPiece);
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public ArrayList<Coordinate> generateStockFishMove(String bestMove) {
        int fromX = (int) bestMove.charAt(0) - ASCII_OFFSET;
        int fromY = Character.getNumericValue(bestMove.charAt(1)) - FEN_OFFSET;
        int toX = (int) bestMove.charAt(2) - ASCII_OFFSET;
        int toY = Character.getNumericValue(bestMove.charAt(3)) - FEN_OFFSET;
        Coordinate from = new Coordinate(fromX, fromY);
        Coordinate to = new Coordinate(toX,toY);
        ArrayList<Coordinate> retList = new ArrayList<>();
        retList.add(from);
        retList.add(to);
        return retList;
    }
}
