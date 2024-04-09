package engine.hardware.pieces;

import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.*;

import static engine.helpers.GlobalHelper.*;

public class King extends Piece {
    private boolean firstMove;
    private final boolean inCheck;
    Rook rookOne;
    Rook rookTwo;

    public King(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite, Rook rookOne, Rook rookTwo) {
        super(isAI, position, drawPosition, team, sprite);
        this.inCheck = false;
        this.firstMove = true;
        this.rookOne = rookOne;
        this.rookTwo = rookTwo;
    }

    public King(King king) {
        super(king);
        this.inCheck = king.inCheck;
        this.firstMove = king.firstMove;
        this.rookOne = king.rookOne;
        this.rookTwo = king.rookTwo;
    }

    public Rook getRookOne() {
        return rookOne;
    }

    public Rook getRookTwo() {
        return rookTwo;
    }

    @Override
    public Piece copy() {
        return new King(this);
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();

        int x = position.getX();
        int y = position.getY();
        moves.add(new Coordinate(x-1, y-1));
        moves.add(new Coordinate(x, y-1));
        moves.add(new Coordinate(x+1, y-1));
        moves.add(new Coordinate(x-1, y));
        moves.add(new Coordinate(x+1, y));
        moves.add(new Coordinate(x-1, y+1));
        moves.add(new Coordinate(x, y+1));
        moves.add(new Coordinate(x+1, y+1));
        return moves;
    }

    public void setFirstMove(boolean bool){
        this.firstMove = bool;
    }
    public boolean getFirstMove(){
        return this.firstMove;
    }

    public boolean canCastleRookOne(Board b){
        Coordinate castlePosition = getTeam() == TEAM_ONE ? KING_CASTLE_ONE_T1 : KING_CASTLE_ONE_T2;
        boolean castlePositionOpen = !b.getAllPiecePositions().contains(castlePosition);
        return firstMove && castlePositionOpen && rookOne.canCastle(b);
    }

    public boolean canCastleRookTwo(Board b){
        Coordinate castlePosition = getTeam() == TEAM_ONE ? KING_CASTLE_TWO_T1 : KING_CASTLE_TWO_T2;
        boolean castlePositionOpen = !b.getAllPiecePositions().contains(castlePosition);
        return firstMove && castlePositionOpen && rookTwo.canCastle(b);
    }

    public void addCastles(ArrayList<Coordinate> moves, Board b){
        Coordinate castleOnePosition = getTeam() == TEAM_ONE ? KING_CASTLE_ONE_T1 : KING_CASTLE_ONE_T2;
        Coordinate castleTwoPosition = getTeam() == TEAM_ONE ? KING_CASTLE_TWO_T1 : KING_CASTLE_TWO_T2;

        if (canCastleRookOne(b))
            moves.add(new Coordinate(castleOnePosition, true, rookOne));
        if (canCastleRookTwo(b))
            moves.add(new Coordinate(castleTwoPosition, true, rookTwo));
    }

    @Override
    public String toString(){
        return "King " + getTeam() + ": " + getPosition();
    }

    public boolean getInCheck(){
        return inCheck;
    }

    public boolean canCastle(Rook rook, Board b) {
        Coordinate castlePosition;
        boolean castlePositionOpen;
        if (rook.equals(rookOne)) {
            castlePosition = getTeam() == TEAM_ONE ? KING_CASTLE_ONE_T1 : KING_CASTLE_ONE_T2;
        }else{
            castlePosition = getTeam() == TEAM_ONE ? KING_CASTLE_TWO_T1 : KING_CASTLE_TWO_T2;
        }
        castlePositionOpen = !b.getAllPiecePositions().contains(castlePosition);
        return firstMove && castlePositionOpen;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same object reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Different classes or null reference
        }
        Piece other = (Piece) obj; // Typecast to Person

        return isAI() == other.isAI()
                && position.positionEquals(other.position)
                && drawPosition.positionEquals(other.drawPosition)
                && getTeam() == other.getTeam()
                && getSprite().equals(other.getSprite())
                && rookOne.equals(((King) other).getRookOne())
                && rookTwo.equals(((King) other).getRookTwo())
                && firstMove == ((King) other).getFirstMove()
                && inCheck == ((King) other).getInCheck();
    }

    public void setRookTwo(Rook rookTwo) {
        this.rookTwo = rookTwo;
    }

    public void setRookOne(Rook rookOne) {
        this.rookOne = rookOne;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    /**
     * @return
     */
    @Override
    public char toLetter() {
        return getTeam() == TEAM_ONE ? 'K' :'k';
    }
}
