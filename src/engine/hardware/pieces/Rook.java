package engine.hardware.pieces;

import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.*;
import static engine.helpers.MovesHelper.addHorizontalAndVerticalMoves;

public class Rook extends Piece {
    private boolean firstMove;
    King king;

    public Rook(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.firstMove = true;
    }

    public Rook(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite, boolean firstMove) {
        super(isAI, position, drawPosition, team, sprite);
        this.firstMove = firstMove;
    }

    public Rook(Piece piece) {
        super(piece);
        this.king = ((Rook) piece).getKing();
        this.firstMove = ((Rook) piece).getFirstMove();
    }

    private boolean getFirstMove() {
        return this.firstMove;
    }

    @Override
    public Piece copy() {
        return new Rook(this); // Assuming Bishop has a copy constructor
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        addHorizontalAndVerticalMoves(moves, position, getTeam(), b);
        return moves;
    }

    public void addKing(King king){
        this.king = king;
    }
    public King getKing(){
        return this.king;
    }

    public boolean canCastle(Board b){
        Coordinate castlePosition;
        if (this.getTeam() == TEAM_ONE){
            castlePosition = this.position.getX() == ROOK_ONE_START ? ROOK_CASTLE_ONE_T1 : ROOK_CASTLE_TWO_T1;
        }else{
            castlePosition = this.position.getX() == ROOK_ONE_START ? ROOK_CASTLE_ONE_T2 : ROOK_CASTLE_TWO_T2;
        }
        boolean castlePositionOpen = !b.getAllPiecePositions().contains(castlePosition);
        if (this.position.getX() == ROOK_ONE_START)
            return firstMove && castlePositionOpen;
        return isAlive() && firstMove && castlePositionOpen;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setKing(King king) {
        this.king = king;
    }

    public void addCastles(ArrayList<Coordinate> moves, Board b){
        if (!firstMove)
            return;

        Coordinate castlePosition;
        King king = b.getPlayer(getTeam()).getKing();
        if (king == null || !king.isFirstMove())
            return;

        if (this.getTeam() == TEAM_ONE){
            castlePosition = this.position.getX() == ROOK_ONE_START ? ROOK_CASTLE_ONE_T1 : ROOK_CASTLE_TWO_T1;
        }else{
            castlePosition = this.position.getX() == ROOK_ONE_START ? ROOK_CASTLE_ONE_T2 : ROOK_CASTLE_TWO_T2;
        }
        boolean castlePositionOpen = !b.getAllPiecePositions().contains(castlePosition);
        if (firstMove && castlePositionOpen && king.canCastle(this, b))
            moves.add(new Coordinate(castlePosition, true, king));
    }

    public void setFirstMove(boolean bool){
        this.firstMove = bool;
    }

    @Override
    public String toString(){
        return "Rook " + getTeam() + ": " + getPosition();
    }

    /**
     * @return
     */
    @Override
    public char toLetter() {
        return getTeam() == TEAM_ONE ? 'R' :'r';
    }
}

