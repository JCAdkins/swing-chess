package engine.hardware.pieces;

import engine.player.Player;
import engine.simulate.BoardSimulator;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.*;
import java.util.List;

import static engine.helpers.GlobalHelper.*;

public class King extends Piece {
    private boolean firstMove;
    private boolean inCheck;
    Rook rookOne;
    Rook rookTwo;

    public King(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite, Rook rookOne, Rook rookTwo) {
        super(isAI, position, drawPosition, team, sprite);
        this.inCheck = false;
        this.firstMove = true;
        this.rookOne = rookOne;
        this.rookTwo = rookTwo;
    }

    public Rook getRookOne() {
        return rookOne;
    }

    public Rook getRookTwo() {
        return rookTwo;
    }

    public King(Piece piece) {
        super(piece);
        this.inCheck = false;
        this.firstMove = ((King) piece).getFirstMove();
        this.rookOne = new Rook(((King) piece).rookOne);
        this.rookTwo = new Rook(((King) piece).rookTwo);

    }

    @Override
    public Piece copy() {
        return new King(this); // Assuming Bishop has a copy constructor
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

    @Override
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

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public boolean getInCheck(){
        return inCheck;
    }

    public boolean canCastle(Rook rook, Board b) {
        Coordinate castlePosition;
        boolean castlePositionOpen;
        if (rook.equals(rookOne)) {
            castlePosition = getTeam() == TEAM_ONE ? KING_CASTLE_ONE_T1 : KING_CASTLE_ONE_T2;
            castlePositionOpen = !b.getAllPiecePositions().contains(castlePosition);
            return isAlive() && firstMove && castlePositionOpen;
        }else{
            castlePosition = getTeam() == TEAM_ONE ? KING_CASTLE_TWO_T1 : KING_CASTLE_TWO_T2;
            castlePositionOpen = !b.getAllPiecePositions().contains(castlePosition);
            return isAlive() && firstMove && castlePositionOpen;
        }
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
                && position.equals(other.position)
                && drawPosition.equals(other.drawPosition)
                && getTeam() == other.getTeam()
                && getSprite().equals(other.getSprite())
                && rookOne.equals(((King) other).getRookOne())
                && rookTwo.equals(((King) other).getRookTwo())
                && firstMove == ((King) other).getFirstMove()
                && inCheck == ((King) other).getInCheck();
    }
}
