package engine.pieces;

import engine.player.Player;
import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.*;

public class King extends Piece {
    private boolean canCastle;
    private boolean inCheck;
    Rook rookOne;
    Rook rookTwo;

    public King(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite, Rook rookOne, Rook rookTwo) {
        super(isAI, position, drawPosition, team, sprite);
        this.inCheck = false;
        this.canCastle = true;
        this.rookOne = rookOne;
        this.rookTwo = rookTwo;
    }

    public King() {
        super();

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
        this.canCastle = true;
        this.rookOne = new Rook(((King) piece).rookOne);
        this.rookTwo = new Rook(((King) piece).rookTwo);

    }

    @Override
    public Piece copy() {
        return new King(this); // Assuming Bishop has a copy constructor
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(Board b, boolean check) {
        ArrayList<Coordinate> moves = new ArrayList<>();

        if (check) {
            Player player = getPlayer(b);
            if (!player.isInCheck() && pieceCannotMove(b))
                return moves;
            if (player.isInCheck())
                return getAvailableMovesInCheck(b, moves);
        }

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

    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {
    }

    boolean canCastle(){
        return canCastle;
    }

    public void setCanCastle(boolean bool){
        this.canCastle = bool;
    }

    @Override
    public void addCastles(ArrayList<Coordinate> moves, Board b){
        if (canCastle && rookOne.canCastle())
            moves.add(rookOne.getPosition());
        if (canCastle && rookTwo.canCastle())
            moves.add(rookTwo.getPosition());
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

}
