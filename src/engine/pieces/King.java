package engine.pieces;

import engine.ui.Board;
import engine.ui.Coordinate;
import java.awt.*;
import java.util.*;
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

    @Override
    ArrayList<Coordinate> addAllPossibleMoves(int x, int y, Board b) {
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

    /**
     * @param possibleMoves - A list containing all possible legal and illegal moves a piece could make
     * @param b - The chessboard object the game is being played on
     */
    @Override
    void removeMovesPuttingPlayerInCheck(ArrayList<Coordinate> possibleMoves, Board b) {
        // Remove moves from possibleMoves where the king will be in the other teams possible moves
        // which would put himself in check
            Set<Coordinate> otherTeamMoves = new HashSet<>();
            for (Piece piece : b.getPieces()) {
                // Don't check moving teams pieces
                if (piece.getTeam() == this.getTeam())
                    continue;

                otherTeamMoves.addAll(piece.getMoves(b));
                }

            for (Coordinate mov : otherTeamMoves){
                possibleMoves.remove(mov);
            }
    }

    /**
     * @param possibleMoves
     * @param b
     */
    @Override
    void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b) {

    }
}
