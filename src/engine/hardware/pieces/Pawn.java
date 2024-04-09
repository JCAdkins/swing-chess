package engine.hardware.pieces;

import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static engine.helpers.GlobalHelper.SWITCH_TEAM;
import static engine.helpers.GlobalHelper.TEAM_ONE;

public class Pawn extends Piece {
    private boolean isFirstMove;

    public Pawn(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.isFirstMove = true;
    }

    public Pawn(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite, boolean firstMove) {
        super(isAI, position, drawPosition, team, sprite);
        this.isFirstMove = firstMove;
    }

    public Pawn(Piece piece) {
        super(piece);
        this.isFirstMove = ((Pawn) piece).isFirstMove;
    }

    @Override
    public Piece copy() {
        return new Pawn(this); // Assuming Bishop has a copy constructor
    }

    public void setIsFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(Board b) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        boolean isValid = true, isDoubleValid = true, addTopLeft = false, addTopRight = false;
        int x = position.getX();
        int y = position.getY();

        for (Piece piece : b.getPieces()){
            if (piece.getPosition().positionEquals(new Coordinate(x, y + getTeam())))
                isValid = false;
            if (piece.getPosition().positionEquals(new Coordinate(x, y + 2 * getTeam())) || !isValid)
                isDoubleValid = false;
            if (piece.getPosition().positionEquals(new Coordinate(x + 1, y + getTeam())) && piece.getTeam() == this.getTeam() * SWITCH_TEAM)
                addTopRight = true;
            if (piece.getPosition().positionEquals(new Coordinate(x - 1, y + getTeam())) && piece.getTeam() == this.getTeam() * SWITCH_TEAM)
                addTopLeft = true;
        }
        if (isValid)
            moves.add(new Coordinate(x, y + getTeam()));
        if (addTopRight)
            moves.add(new Coordinate(x + 1, y + getTeam()));
        if (addTopLeft)
            moves.add(new Coordinate(x - 1, y + getTeam()));
        if (isFirstMove && isValid && isDoubleValid)
            moves.add(new Coordinate(x, y + 2 * getTeam()));
        return moves;
    }

    public Piece convertToNewPiece(Class<? extends Piece> c, Image img){
        return switch (c.getSimpleName()) {
            case "Bishop" -> new Bishop(isAI(), getPosition(), getDrawPosition(), getTeam(), img);
            case "Knight" -> new Knight(isAI(), getPosition(), getDrawPosition(), getTeam(), img);
            case "Pawn" -> new Pawn(isAI(), getPosition(), getDrawPosition(), getTeam(), img, false);
            case "Rook" -> new Rook(isAI(), getPosition(), getDrawPosition(), getTeam(), img, false);
            default -> new Queen(isAI(), getPosition(), getDrawPosition(), getTeam(), img);
        };
    }

    @Override
    public String toString(){
        return "Pawn " + getTeam() + ": " + getPosition();
    }

    /**
     * @return
     */
    @Override
    public char toLetter() {
        return getTeam() == TEAM_ONE ? 'P' :'p';
    }
}
