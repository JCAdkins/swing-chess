package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Piece implements Movable {
    private final boolean isAI;
    private boolean canMove;
    Coordinate position;
    Coordinate drawPosition;
    private final int team;
    private final Image sprite;

    public Piece(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        this.isAI = isAI;
        this.canMove = true;
        this.position = position;
        this.drawPosition = drawPosition;
        this.team = team;
        this.sprite = sprite;
    }

    public boolean isAI() {
        return isAI;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public Coordinate getDrawPosition() {
        return drawPosition;
    }

    public void setDrawPosition(Coordinate drawPosition) {
        this.drawPosition = drawPosition;
    }

    public int getTeam() {
        return team;
    }

    public Image getSprite() {
        return sprite;
    }

    public ArrayList<Coordinate> getMoves(Board b) {
        int x = position.getX();
        int y = position.getY();
        ArrayList<Coordinate> possibleMoves = addAllPossibleMoves(x, y, b);
        System.out.println("position: " + position);
        System.out.println("possibleMoves before: " + possibleMoves);
        removeIllegalMoves(possibleMoves, b);
        System.out.println("possibleMoves after: " + possibleMoves);
        return possibleMoves;
    }

    abstract ArrayList<Coordinate> addAllPossibleMoves(int x, int y, Board b);
    void removeIllegalMoves(ArrayList<Coordinate> possibleMoves, Board b){
        removeOffBoardMoves(possibleMoves, b);
        removeAllMovesOnSelf(possibleMoves, b);
        removeMovesPuttingPlayerInCheck(possibleMoves, b);
        removeAllOtherMoves(possibleMoves, b);
    }

    private void removeOffBoardMoves(ArrayList<Coordinate> possibleMoves, Board b) {
        List<Coordinate> illegalMoves = possibleMoves.stream().filter(move -> move.getX() < 0 || move.getY() < 0 || move.getX() > 7 || move.getY() > 7).toList();
        illegalMoves.forEach(possibleMoves::remove);
    }

    abstract void removeMovesPuttingPlayerInCheck(ArrayList<Coordinate> possibleMoves, Board b);
    private void removeAllMovesOnSelf(ArrayList<Coordinate> possibleMoves, Board b){
        Iterator<Coordinate> iterator = possibleMoves.iterator();
        while (iterator.hasNext()) {
            Coordinate move = iterator.next();
            for (Piece piece : b.getPieces()) {
                if (move.equals(piece.getPosition()) && this.getTeam() == piece.getTeam()) {
                    iterator.remove();
                    break; // Break after removing, not before
                }
            }
        }
    }

    abstract void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b);
}

