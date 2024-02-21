package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class King extends Piece {
    private boolean canCastle;

    public King(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        super(isAI, position, drawPosition, team, sprite);
        this.canCastle = true;
    }

    /**
     *
     */
    @Override
    public void move() {

    }

    /**
     * @return Returns a list of Coordinates that are legal board positions
     * that the King piece can move to.
     */
    @Override
    public ArrayList<Coordinate> getMoves(Board b) {
        int x = position.getX();
        int y = position.getY();
        ArrayList<Coordinate> possibleMoves = addAllPossibleMoves(x, y);
        System.out.println("possibleMoves before: " + possibleMoves);
        removeIllegalMoves(possibleMoves, b);
        System.out.println("possibleMoves after: " + possibleMoves);
        return possibleMoves;
    }

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y) {
        ArrayList<Coordinate> moves = new ArrayList<>();
        moves.add(new Coordinate(x-1, y-1));
        moves.add(new Coordinate(x, y-1));
        moves.add(new Coordinate(x+1, y-1));
        moves.add(new Coordinate(x-1, y));
        moves.add(new Coordinate(x+1, y));
        moves.add(new Coordinate(x-1, y+1));
        moves.add(new Coordinate(x, y+1));
        moves.add(new Coordinate(x+1, y+1));
        addCastles(moves);
        return moves;
    }

    private void addCastles(ArrayList<Coordinate> moves) {
    }

    @Override
    void removeIllegalMoves(ArrayList<Coordinate> possibleMoves, Board b) {
        removeMovesToTeamPieces(possibleMoves, b);
        removeMovesOffBoard(possibleMoves, b);
    }

    private void removeMovesOffBoard(ArrayList<Coordinate> possibleMoves, Board b) {
        List<Coordinate> illegalMoves = possibleMoves.stream().filter(move -> move.getX() < 0 || move.getY() < 0 || move.getX() > 7 || move.getY() > 7).toList();
        illegalMoves.forEach(possibleMoves::remove);
    }

    private void removeMovesToTeamPieces(ArrayList<Coordinate> possibleMoves, Board b) {
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
}
